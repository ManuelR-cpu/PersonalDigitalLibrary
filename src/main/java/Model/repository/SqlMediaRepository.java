package Model.repository;

import Model.domain.IMediaItem;
import Model.domain.MediaType;
import Model.domain.Movie;
import Model.domain.TVShow;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.sql.DataSource;

public class SqlMediaRepository implements MediaRepository {
  private final DataSource dataSource;

  public SqlMediaRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void createMovie(Movie movie) {
    String sqlEntry = """
      INSERT INTO movie
      (id, title, release_year, genre, rating, director, duration)
      VALUES (?, ?, ?, ?, ?, ?, ?)
      """;

    try (Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sqlEntry)) {
      stmt.setObject(1, movie.getId());
      stmt.setString(2, movie.getTitle());
      stmt.setInt(3, movie.getReleaseYear());
      stmt.setString(4, movie.getGenre());
      stmt.setDouble(5, movie.getRating());
      stmt.setString(6, movie.getDirector());
      stmt.setInt(7, movie.getDuration());

      stmt.executeUpdate();

    } catch (SQLException e) {
      throw new RuntimeException("Failed to create movie", e);
    }
  }

  @Override
  public void updateMovie(Movie movie) {
    String sqlEntry = """
      UPDATE movie
      SET title = ?, release_year = ?, genre = ?, rating = ?,  director = ?, duration = ?
      WHERE id = ?
    """;

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sqlEntry)) {
      stmt.setString(1, movie.getTitle());
      stmt.setInt(2, movie.getReleaseYear());
      stmt.setString(3, movie.getGenre());
      stmt.setDouble(4, movie.getRating());
      stmt.setString(5, movie.getDirector());
      stmt.setInt(6, movie.getDuration());
      stmt.setObject(7, movie.getId());

      int rowPresent = stmt.executeUpdate();
      if (rowPresent == 0) {
        throw new IllegalStateException("No movie found with id " + movie.getId());
      }

    } catch (SQLException e) {
      throw new RuntimeException("Failed to update movie", e);
    }
  }

  @Override
  public void createTVShow(TVShow show) {
    String tvDbEntry = """
      INSERT INTO tv_show
      (id, title, release_year, genre, rating, episode_count, season_count)
      VALUES (?, ?, ?, ?, ?, ?, ?)
      """;
    String seasonEntry = """
        INSERT INTO season_rating
        (show_id, season_num, rating)
        VALUES (?, ?, ?)
        """;

    try (Connection conn = dataSource.getConnection()) {
      conn.setAutoCommit(false);

      try (PreparedStatement tvTable = conn.prepareStatement(tvDbEntry);
           PreparedStatement seasonTable = conn.prepareStatement(seasonEntry)) {
        tvTable.setObject(1, show.getId());
        tvTable.setString(2, show.getTitle());
        tvTable.setInt(3, show.getReleaseYear());
        tvTable.setString(4, show.getGenre());
        tvTable.setDouble(5, show.getRating());
        tvTable.setInt(6, show.getEpisodeCount());
        tvTable.setInt(7, show.getSeasonCount());

        tvTable.executeUpdate();

        // add the most updated version of the season ratings to the table
        for (Map.Entry<Integer, Double> entry : show.getSeasonRatings().entrySet()) {
          seasonTable.setObject(1, show.getId());
          // season #
          seasonTable.setInt(2, entry.getKey());
          // rating of that season
          seasonTable.setDouble(3, entry.getValue());
          seasonTable.addBatch();
        }

        seasonTable.executeBatch();
        conn.commit();
      } catch (SQLException e) {
        conn.rollback();
        throw e;
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to create TV show", e);
    }
  }

  @Override
  public void updateTVShow(TVShow show) {
    String sqlEntry = """
      UPDATE tv_show
      SET title = ?, release_year = ?, genre = ?, rating = ?,  episode_count = ?,  season_count = ?
      WHERE id = ?
    """;

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sqlEntry)) {

      // configure connection so it does not autocommit
      boolean originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      try {
        stmt.setString(1, show.getTitle());
        stmt.setInt(2, show.getReleaseYear());
        stmt.setString(3, show.getGenre());
        stmt.setDouble(4, show.getRating());
        stmt.setInt(5, show.getEpisodeCount());
        stmt.setInt(6, show.getSeasonCount());
        stmt.setObject(7, show.getId());

        int rows = stmt.executeUpdate();
        if (rows == 0) {
          throw new IllegalStateException("No TV show found with ID " + show.getId());
        }

        syncShowSeasons(conn, show.getId(), show.getSeasonRatings());

        conn.commit();
      } catch (SQLException | RuntimeException e) {
        try {
          // rollback happens in case of failure in the syncShowSeasons() method
          // as to ensure that the entire database is updated and not just some parts
          // while others are not due to a failure later down the line
          conn.rollback();
        } catch (SQLException rollbackEx) {
          e.addSuppressed(rollbackEx);
        }
        throw e;
      } finally {
        try {
          conn.setAutoCommit(originalAutoCommit);
        }  catch (SQLException e) {
          e.addSuppressed(e);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to update TV show", e);
    }
  }

  private void syncShowSeasons(Connection conn, UUID showId, Map<Integer, Double> seasonRatings)
      throws SQLException {
    String deleteSeasons = "DELETE FROM season_rating WHERE show_id = ?";
    String seasonEntry = """
        INSERT INTO season_rating
        (show_id, season_num, rating)
        VALUES (?, ?, ?)
        """;


    // perform the actual changes to the season_rating table
    try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSeasons);
         PreparedStatement insertStmt = conn.prepareStatement(seasonEntry)) {

      // delete all entries related to the ID of the show
      deleteStmt.setObject(1, showId);
      deleteStmt.executeUpdate();

      // add the most updated version of the season ratings to the table
      for (Map.Entry<Integer, Double> entry : seasonRatings.entrySet()) {
        insertStmt.setObject(1, showId);
        insertStmt.setInt(2, entry.getKey());
        insertStmt.setDouble(3, entry.getValue());
        insertStmt.addBatch();
      }

      insertStmt.executeBatch();
    }
  }

  @Override
  public boolean delete(UUID id, MediaType type) {
    String sqlEntry = switch (type) {
      case MOVIE -> "DELETE FROM movie WHERE id = ?";
      case TV_SERIES -> "DELETE FROM tv_show WHERE id = ?";
    };

    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sqlEntry)) {

      ps.setObject(1, id);
      return ps.executeUpdate() > 0;

    } catch (SQLException e) {
      throw new RuntimeException("Failed to delete media item", e);
    }
  }

  private record TvRow(
      UUID id,
      String title,
      int releaseYear,
      String genre,
      double rating,
      int episodeCount,
      int seasonCount
  ) {}

  @Override
  public List<IMediaItem> findAll() {
    List<IMediaItem> items = new ArrayList<>();
    String movieSqlEntry = """
      SELECT id, title, release_year, genre, rating, director, duration
      FROM movie;
      """;

    String tvSqlEntry = """
      SELECT id, title, release_year, genre, rating, episode_count, season_count
      FROM tv_show;
      """;

    try (Connection conn = dataSource.getConnection();
         PreparedStatement psMovie = conn.prepareStatement(movieSqlEntry);
         ResultSet rsMovie = psMovie.executeQuery();
         PreparedStatement psTv = conn.prepareStatement(tvSqlEntry);
         ResultSet rsTv = psTv.executeQuery()) {
      while (rsMovie.next()) {
        items.add(mapMovieRowToItem(rsMovie));
      }

      List<TvRow> tvRows = new ArrayList<>();
      List<UUID> showIds = new ArrayList<>();

      while (rsTv.next()) {
        UUID id = rsTv.getObject("id", UUID.class);

        tvRows.add(new TvRow(
            id,
            rsTv.getString("title"),
            rsTv.getInt("release_year"),
            rsTv.getString("genre"),
            rsTv.getDouble("rating"),
            rsTv.getInt("episode_count"),
            rsTv.getInt("season_count")
        ));

        showIds.add(id);
      }

      Map<UUID, Map<Integer, Double>> seasonRatings = seasonsMap(conn, showIds);

      for (TvRow row : tvRows) {
        Map<Integer, Double> ratings = seasonRatings.getOrDefault(row.id(), Map.of());
        items.add(new TVShow(
            row.id(), row.title(), row.releaseYear(), row.genre(), row.rating(),
            MediaType.TV_SERIES, row.episodeCount(), row.seasonCount(), ratings
        ));
      }

    } catch (SQLException e) {
      throw
          new RuntimeException("Failed to find all movies", e);
    }

    return items;
  }

  private IMediaItem mapMovieRowToItem(ResultSet rs) throws SQLException {
    UUID id = rs.getObject("id", UUID.class);
    String title = rs.getString("title");
    int releaseYear = rs.getInt("release_year");
    String genre = rs.getString("genre");
    double rating = rs.getDouble("rating");
    String director = rs.getString("director");
    int duration = rs.getInt("duration");

    return new Movie(id, title, releaseYear, genre, rating, MediaType.MOVIE, director, duration);
  }

  private Map<UUID, Map<Integer, Double>> seasonsMap(Connection conn, List<UUID> showIds) {
    // exit as cannot have empty parentheses in seasonSql string for the IN statement
    if (showIds.isEmpty()) {
      return Map.of();
    }

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < showIds.size(); i++) {
      sb.append("?");
      if (i < showIds.size() - 1) {
        sb.append(", ");
      }
    }
    String placeholders = sb.toString();

    String seasonSql = """
      SELECT show_id, season_num, rating
      FROM season_rating
      WHERE show_id IN (%s)
      ORDER BY show_id, season_num;
      """.formatted(placeholders);

    Map<UUID, Map<Integer, Double>> seasonRatings = new HashMap<>();

    try (PreparedStatement ps = conn.prepareStatement(seasonSql)) {
      for (int i = 0; i < showIds.size(); i++) {
        ps.setObject(i + 1, showIds.get(i));
      }


      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          UUID id = rs.getObject("show_id", UUID.class);

          // computeIfAbsent will return the HashMap for that shows seasons and will create a new
          // HashMap for that show if it does exist for that corresponding ID and then adds the
          // rating
          seasonRatings
              .computeIfAbsent(id, k -> new HashMap<>())
              .put(rs.getInt("season_num"), rs.getDouble("rating"));
        }
      }

    } catch (SQLException e) {
      throw new RuntimeException("Failed to find seasons", e);
    }

    return seasonRatings;
  }
}
