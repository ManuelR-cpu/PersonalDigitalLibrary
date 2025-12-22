package Model.repository;

import Model.domain.IMediaItem;
import Model.domain.MediaType;
import Model.domain.Movie;
import Model.domain.TVShow;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.sql.DataSource;

public class SqlMediaRepository implements MediaRepository {
  private final DataSource dataSource;

  public SqlMediaRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void save(IMediaItem item) {
    String sqlEntry = """
      INSERT INTO media_items
      (id, title, release_year, genre, rating, media_type, episode_count)
      VALUES (?, ?, ?, ?, ?, ?, ?)
      """;

    try(Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sqlEntry)) {
      stmt.setObject(1, item.getId());
      stmt.setString(2, item.getTitle());
      stmt.setInt(3, item.getReleaseYear());
      stmt.setString(4, item.getGenre());
      stmt.setDouble(5, item.getRating());
      stmt.setString(6, item.getMediaType().name());

      if (item instanceof TVShow show) {
        stmt.setInt(7, show.getEpisodeCount());
      } else {
        stmt.setNull(7, Types.INTEGER);
      }

      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Failed to save media item", e);
    }
  }

  @Override
  public boolean delete(UUID id) {
    String sqlEntry = "DELETE FROM media_items WHERE id = ?;";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sqlEntry)) {
      ps.setObject(1, id);
      return ps.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new RuntimeException("Failed to delete media item", e);
    }
  }

  @Override
  public List<IMediaItem> findAll() {
    List<IMediaItem> items = new ArrayList<>();
    String sqlEntry = """
      SELECT id, title, release_year, genre, rating, media_type, episode_count
      FROM media_items;
      """;

    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sqlEntry);
         ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        items.add(mapRowToItem(rs));
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to find all media items", e);
    }

    return items;
  }

  private IMediaItem mapRowToItem(ResultSet rs) throws SQLException {
    UUID id = rs.getObject("id", UUID.class);
    String title = rs.getString("title");
    int releaseYear = rs.getInt("release_year");
    String genre = rs.getString("genre");
    double rating = rs.getDouble("rating");
    String mediaType = rs.getString("media_type");
    int epCount = rs.getObject("episode_count", Integer.class);

    MediaType type = MediaType.valueOf(mediaType);

    return switch (type) {
      case MOVIE -> new Movie(id, title, releaseYear, genre, rating, MediaType.MOVIE);
      case TV_SERIES -> new TVShow(id, title, releaseYear, genre, rating, MediaType.TV_SERIES, epCount);
    };
  }
}
