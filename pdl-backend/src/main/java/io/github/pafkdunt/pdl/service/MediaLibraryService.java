package io.github.pafkdunt.pdl.service;

import io.github.pafkdunt.pdl.domain.IMediaItem;
import io.github.pafkdunt.pdl.domain.MediaType;
import io.github.pafkdunt.pdl.domain.Movie;
import io.github.pafkdunt.pdl.domain.TVShow;
import io.github.pafkdunt.pdl.repository.MediaRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MediaLibraryService {
  private final MediaRepository mediaRepository;

  public MediaLibraryService(MediaRepository mediaRepository) {
    this.mediaRepository = mediaRepository;
  }

  @Transactional
  public void addItem(IMediaItem item) {
    if (item instanceof Movie) {
      mediaRepository.createMovie((Movie) item);
    } else {
      mediaRepository.createTVShow((TVShow) item);
    }
  }

  public List<IMediaItem> getAllItems() {
    return mediaRepository.findAll();
  }

  public List<Movie> getAllMovies() {
    List<Movie> movies = new ArrayList<>();
    // Made one db query here instead of calling findAll() in loop
    // as to not flood unnecessary requests to db each loop.
    // A good future change may be to implement filter logic in the query itself.
    List<IMediaItem> allItems = mediaRepository.findAll();

    for (IMediaItem item : allItems) {
      if (item.getMediaType() == MediaType.MOVIE) {
        movies.add((Movie) item);
      }
    }

    return movies;
  }

  public List<TVShow> getAllShows() {
    List<TVShow> shows = new ArrayList<>();
    // Made one db query here instead of calling findAll() in loop
    // as to not flood unnecessary requests to db each loop.
    // A good future change may be to implement filter logic in the query itself.
    List<IMediaItem> allItems = mediaRepository.findAll();

    for (IMediaItem item : allItems) {
      if (item.getMediaType() == MediaType.TV_SERIES) {
        shows.add((TVShow) item);
      }
    }

    return shows;
  }

  public List<Movie> searchForMovies(String title) {
    if (title == null || title.isBlank()) {
      return List.of();
    }

    List<Movie> foundItems = new ArrayList<>();
    List<Movie> movies = mediaRepository.findAllMovies();

    for (Movie item : movies) {
      if (item.getTitle().toLowerCase().contains(title.toLowerCase())) {
        foundItems.add(item);
      }
    }

    return foundItems;
  }

  public List<TVShow> searchForTVShows(String title) {
    if (title == null || title.isBlank()) {
      return List.of();
    }

    List<TVShow> foundItems = new ArrayList<>();
    List<TVShow> shows = mediaRepository.findAllTVShows();

    for (TVShow item : shows) {
      if (item.getTitle().toLowerCase().contains(title.toLowerCase())) {
        foundItems.add(item);
      }
    }

    return foundItems;
  }

  @Transactional
  public void deleteItem(UUID uuid, MediaType type) {
    mediaRepository.delete(uuid, type);
  }

  @Transactional
  public void modifyItem(UUID uuid, IMediaItem item) {
    if (item instanceof Movie) {
      mediaRepository.updateMovie((Movie) item);
    } else {
      mediaRepository.updateTVShow((TVShow) item);
    }
  }
}
