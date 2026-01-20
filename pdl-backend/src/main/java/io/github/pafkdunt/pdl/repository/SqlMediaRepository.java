package io.github.pafkdunt.pdl.repository;

import io.github.pafkdunt.pdl.domain.IMediaItem;
import io.github.pafkdunt.pdl.domain.MediaType;
import io.github.pafkdunt.pdl.domain.Movie;
import io.github.pafkdunt.pdl.domain.TVShow;
import io.github.pafkdunt.pdl.exception.NotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SqlMediaRepository implements MediaRepository {
  private final JdbcTemplate jdbc;
  private final NamedParameterJdbcTemplate namedJdbc;

  public SqlMediaRepository(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
    this.namedJdbc = new NamedParameterJdbcTemplate(jdbc);
  }

  @Override
  public void createMovie(Movie movie) {
    String sqlEntry = """
      INSERT INTO movie
      (id, title, release_year, genre, rating, director, duration)
      VALUES (?, ?, ?, ?, ?, ?, ?)
      """;

    jdbc.update(sqlEntry,
        movie.getId(),
        movie.getTitle(),
        movie.getReleaseYear(),
        movie.getGenre(),
        movie.getRating(),
        movie.getDirector(),
        movie.getDuration());
  }

  @Override
  public void updateMovie(Movie movie) {
    String sqlEntry = """
      UPDATE movie
      SET title = ?, release_year = ?, genre = ?, rating = ?,  director = ?, duration = ?
      WHERE id = ?
    """;

    int rowPresent = jdbc.update(sqlEntry,
        movie.getTitle(),
        movie.getReleaseYear(),
        movie.getGenre(),
        movie.getRating(),
        movie.getDirector(),
        movie.getDuration(),
        movie.getId());

    if (rowPresent == 0) {
      throw new NotFoundException("No movie found with ID: " + movie.getId());
    }
  }

  @Override
  public void createTVShow(TVShow show) {
    String sqlEntry = """
      INSERT INTO tv_show
      (id, title, release_year, genre, rating, episode_count, season_count)
      VALUES (?, ?, ?, ?, ?, ?, ?)
      """;

    jdbc.update(sqlEntry,
        show.getId(),
        show.getTitle(),
        show.getReleaseYear(),
        show.getGenre(),
        show.getRating(),
        show.getEpisodeCount(),
        show.getSeasonCount());

    insertSeasonRatings(show.getId(), show.getSeasonRatings());
  }

  @Override
  public void updateTVShow(TVShow show) {
    String sqlEntry = """
      UPDATE tv_show
      SET title = ?, release_year = ?, genre = ?, rating = ?,  episode_count = ?,  season_count = ?
      WHERE id = ?
    """;

    int rows = jdbc.update(sqlEntry,
        show.getTitle(),
        show.getReleaseYear(),
        show.getGenre(),
        show.getRating(),
        show.getEpisodeCount(),
        show.getSeasonCount(),
        show.getId()
    );

    if (rows == 0) {
      throw new NotFoundException("No TV show found with ID: " + show.getId());
    }

    syncShowSeasons(show.getId(), show.getSeasonRatings());
  }

  private void insertSeasonRatings(UUID showId, Map<Integer, Double> seasonRatings) {
    String seasonRatingsEntry = """
        INSERT INTO season_rating
        (show_id, season_num, rating)
        VALUES (?, ?, ?)
        """;

    List<Object[]> batchArgs = new ArrayList<>();
    for (Map.Entry<Integer, Double> e : seasonRatings.entrySet()) {
      batchArgs.add(new Object[]{showId, e.getKey(), e.getValue()});
    }

    jdbc.batchUpdate(seasonRatingsEntry, batchArgs);
  }

  private void syncShowSeasons(UUID showId, Map<Integer, Double> seasonRatings) {
    String deleteSeasons = "DELETE FROM season_rating WHERE show_id = ?";
    jdbc.update(deleteSeasons, showId);
    insertSeasonRatings(showId, seasonRatings);
  }

  @Override
  public void delete(UUID id, MediaType type) {
    String sqlEntry = switch (type) {
      case MOVIE -> "DELETE FROM movie WHERE id = ?";
      case TV_SERIES -> "DELETE FROM tv_show WHERE id = ?";
    };

    int row = jdbc.update(sqlEntry, id);
    if (row == 0) {
      throw new NotFoundException("No media item found with ID: " + id);
    }
  }

  private record TVRow(
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
    items.addAll(findAllMovies());
    items.addAll(findAllTVShows());
    return items;
  }

  @Override
  public List<Movie> findAllMovies() {
    String sqlEntry = """
      SELECT id, title, release_year, genre, rating, director, duration
      FROM movie
      """;

    return jdbc.query(sqlEntry, (rs, rowNum) -> mapMovieRowToItem(rs));
  }

  @Override
  public List<TVShow> findAllTVShows() {
    List<TVShow> tvShows = new ArrayList<>();
    String tvSqlEntry = """
      SELECT id, title, release_year, genre, rating, episode_count, season_count
      FROM tv_show
      """;

    List<TVRow> tvRows = jdbc.query(tvSqlEntry, (rs, rowNum) -> new TVRow(
        rs.getObject("id", UUID.class),
        rs.getString("title"),
        rs.getInt("release_year"),
        rs.getString("genre"),
        rs.getDouble("rating"),
        rs.getInt("episode_count"),
        rs.getInt("season_count")
    ));

    List<UUID> showIds = tvRows.stream().map(TVRow::id).toList();
    Map<UUID, Map<Integer, Double>> seasonRatings = seasonsMap(showIds);

    for (TVRow row : tvRows) {
      Map<Integer, Double> ratings = seasonRatings.getOrDefault(row.id(), Map.of());
      tvShows.add(new TVShow(
          row.id(), row.title(), row.releaseYear(), row.genre(), row.rating(),
          MediaType.TV_SERIES, row.episodeCount(), row.seasonCount(), ratings
      ));
    }

    return tvShows;
  }

  private Movie mapMovieRowToItem(ResultSet rs) throws SQLException {
    UUID id = rs.getObject("id", UUID.class);
    String title = rs.getString("title");
    int releaseYear = rs.getInt("release_year");
    String genre = rs.getString("genre");
    double rating = rs.getDouble("rating");
    String director = rs.getString("director");
    int duration = rs.getInt("duration");

    return new Movie(id, title, releaseYear, genre, rating, MediaType.MOVIE, director, duration);
  }

  private Map<UUID, Map<Integer, Double>> seasonsMap(List<UUID> showIds) {
    if (showIds == null || showIds.isEmpty()) {
      return Map.of();
    }

    String seasonSql = """
      SELECT show_id, season_num, rating
      FROM season_rating
      WHERE show_id IN (:ids)
      ORDER BY show_id, season_num
      """;

    // Bind the :ids in the SQL statement to the inputted list of UUIDS.
    MapSqlParameterSource params = new MapSqlParameterSource("ids", showIds);
    Map<UUID, Map<Integer, Double>> seasonRatings = new HashMap<>();

    // computeIfAbsent() will return the HashMap for that shows seasons and will create a new
    // HashMap for that show if it does exist for that corresponding ID and then adds the rating.
    namedJdbc.query(seasonSql, params, rs -> {
      UUID id = rs.getObject("show_id", UUID.class);
      seasonRatings
          .computeIfAbsent(id, k -> new HashMap<>())
          .put(rs.getInt("season_num"), rs.getDouble("rating"));
    });

    return seasonRatings;
  }
}
