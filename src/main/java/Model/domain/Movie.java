package Model.domain;

import java.util.UUID;

public class Movie implements IMediaItem {
  private final UUID id;
  private final String title;
  private final int releaseYear;
  private final String genre;
  private final double rating;
  private final MediaType mediaType;

  // for constructing new Movie objects
  public Movie(String title, int releaseYear, String genre, double rating, MediaType mediaType) {
    this.id = UUID.randomUUID();
    this.title = title;
    this.releaseYear = releaseYear;
    this.genre = genre;
    this.rating = rating;
    this.mediaType = mediaType;
  }

  // for constructing Movie objects already in the database
  public Movie(UUID id, String title, int releaseYear, String genre, double rating, MediaType mediaType) {
    this.id = id;
    this.title = title;
    this.releaseYear = releaseYear;
    this.genre = genre;
    this.rating = rating;
    this.mediaType = mediaType;
  }

  @Override
  public String toString() {
    return String.format("%s (%d) - Genre: %s, Rating: %.1f/10.0", title, releaseYear, genre, rating);
  }

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public int getReleaseYear() {
    return releaseYear;
  }

  @Override
  public String getGenre() {
    return genre;
  }

  @Override
  public double getRating() {
    return rating;
  }

  @Override
  public MediaType getMediaType() {
    return mediaType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    IMediaItem that = (IMediaItem) o;
    return id.equals(that.getId());
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
