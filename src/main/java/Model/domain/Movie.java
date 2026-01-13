package Model.domain;

import java.util.UUID;

public class Movie extends AbstractMediaItem {
  private final String director;
  private final int duration;

  // for constructing new Movie objects
  public Movie(String title, int releaseYear, String genre, double rating, MediaType mediaType,  String director, int duration) {
    super(title, releaseYear, genre, rating, mediaType);
    this.director = director;
    this.duration = duration;
  }

  // for constructing Movie objects already in the database
  public Movie(UUID id, String title, int releaseYear, String genre, double rating, MediaType mediaType, String director, int duration) {
    super(id, title, releaseYear, genre, rating,  mediaType);
    this.director = director;
    this.duration = duration;
  }

  @Override
  public String toString() {
    return String.format("%s (%d) - Genre: %s, Rating: %.1f/10.0", title, releaseYear, genre, rating);
  }

  public String getDirector() {
    return director;
  }

  public int getDuration() {
    return duration;
  }
}
