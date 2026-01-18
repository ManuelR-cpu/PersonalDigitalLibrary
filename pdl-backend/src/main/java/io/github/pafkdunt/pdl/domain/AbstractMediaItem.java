package io.github.pafkdunt.pdl.domain;


import java.util.UUID;

public class AbstractMediaItem implements IMediaItem {
  protected UUID id;
  protected String title;
  protected int releaseYear;
  protected String genre;
  protected double rating;
  protected MediaType mediaType;

  // for constructing new Media objects
  public AbstractMediaItem(String title, int releaseYear, String genre, double rating, MediaType mediaType) {
    this.id = UUID.randomUUID();
    this.title = title;
    this.releaseYear = releaseYear;
    this.genre = genre;
    this.rating = rating;
    this.mediaType = mediaType;
  }

  // for constructing Media objects already in the database
  public AbstractMediaItem(UUID id, String title, int releaseYear, String genre, double rating, MediaType mediaType) {
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
