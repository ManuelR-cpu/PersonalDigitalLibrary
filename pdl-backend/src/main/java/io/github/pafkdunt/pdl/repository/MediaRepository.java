package io.github.pafkdunt.pdl.repository;

import io.github.pafkdunt.pdl.domain.IMediaItem;
import io.github.pafkdunt.pdl.domain.MediaType;
import io.github.pafkdunt.pdl.domain.Movie;
import io.github.pafkdunt.pdl.domain.TVShow;
import java.util.List;
import java.util.UUID;

public interface MediaRepository {
  void createMovie(Movie movie);
  void updateMovie(Movie movie);
  void createTVShow(TVShow show);
  void updateTVShow(TVShow show);
  void delete(UUID id, MediaType type);
  List<IMediaItem> findAll();
  List<Movie> findAllMovies();
  List<TVShow> findAllTVShows();
}
