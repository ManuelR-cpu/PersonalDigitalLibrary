package io.github.pafkdunt.pdl.controller;

import io.github.pafkdunt.pdl.domain.MediaType;
import io.github.pafkdunt.pdl.domain.Movie;
import io.github.pafkdunt.pdl.domain.TVShow;
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
public class TVShowController {
  private final MediaLibraryService libraryService;

  public TVShowController(MediaLibraryService libraryService) {
    this.libraryService = libraryService;
  }

  @GetMapping("/tv-shows")
  public List<TVShow> shows() {
    return libraryService.getAllShows();
  }

  @PostMapping("/tv-shows")
  public ResponseEntity<Void> addShow(@RequestBody TVShow show) {
    libraryService.addItem(show);
    return ResponseEntity.created(URI.create("/tv-shows/" + show.getId())).build(); // 201
  }

  @PutMapping("/tv-shows/{id}")
  public ResponseEntity<TVShow> updateMovie(@PathVariable UUID id, @RequestBody TVShow show) {
    if (show == null) {
      return ResponseEntity.badRequest().build(); // 400
    }

    libraryService.modifyItem(id, show);
    return ResponseEntity.ok(show);
  }

  @DeleteMapping("/tv-shows/{id}")
  public ResponseEntity<Void> deleteShow(@PathVariable UUID id) {
    libraryService.deleteItem(id, MediaType.TV_SERIES);
    return ResponseEntity.noContent().build(); // 204
  }

  @GetMapping("/tv-shows-search")
  public List<TVShow> search(@RequestParam String title) {
    return libraryService.searchForTVShows(title);
  }
}
