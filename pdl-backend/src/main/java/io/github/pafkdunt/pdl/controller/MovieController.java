package io.github.pafkdunt.pdl.controller;

import io.github.pafkdunt.pdl.domain.IMediaItem;
import io.github.pafkdunt.pdl.domain.MediaType;
import io.github.pafkdunt.pdl.domain.Movie;
import io.github.pafkdunt.pdl.service.MediaLibraryService;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MovieController {
  private final MediaLibraryService libraryService;

  public MovieController(MediaLibraryService libraryService) {
    this.libraryService = libraryService;
  }

  @GetMapping("/movies")
  public List<Movie> movies() {
    return libraryService.getAllMovies();
  }

  @PostMapping("/movies")
  public ResponseEntity<Void> addMovie(@RequestBody Movie movie) {
    libraryService.addItem(movie);
    return ResponseEntity.created(URI.create("/movies/" + movie.getId())).build(); // 201
  }

  @PutMapping("/movies/{id}")
  public ResponseEntity<Movie> updateMovie(@PathVariable UUID id, @RequestBody Movie movie) {
    if (movie == null) {
      return ResponseEntity.badRequest().build(); // 400
    }

    libraryService.modifyItem(id, movie);
    return ResponseEntity.ok(movie);
  }

  @DeleteMapping("/movies/{id}")
  public ResponseEntity<Void> deleteMovie(@PathVariable UUID id) {
    libraryService.deleteItem(id, MediaType.MOVIE);
    return ResponseEntity.noContent().build(); // 204
  }


  @GetMapping("/movies-search")
  public List<Movie> search(@RequestParam String title) {
    return libraryService.searchForMovies(title);
  }
}
