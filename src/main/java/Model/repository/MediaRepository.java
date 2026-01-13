package Model.repository;

import Model.domain.IMediaItem;
import Model.domain.MediaType;
import Model.domain.Movie;
import Model.domain.TVShow;
import java.util.List;
import java.util.UUID;

public interface MediaRepository {
  void createMovie(Movie movie);
  void updateMovie(Movie movie);
  void createTVShow(TVShow show);
  void updateTVShow(TVShow show);
  boolean delete(UUID id, MediaType type);
  List<IMediaItem> findAll();
}
