package Model.domain;

import java.util.UUID;

public class TVShow implements IMediaItem {
  private final UUID id;
  private String title;
  private int releaseYear;
  private String genre;
  private double rating;
  private Integer episodeCount;
  private MediaType mediaType;

  // for constructing new Movie objects
  public TVShow(String title, int releaseYear, String genre, double rating, MediaType mediaType, Integer episodeCount) {
    this.id = UUID.randomUUID();
    this.title = title;
    this.releaseYear = releaseYear;
    this.genre = genre;
    this.rating = rating;
    this.mediaType = mediaType;
    this.episodeCount = episodeCount;
  }

  // for constructing TVShow objects already in the database
  public TVShow(UUID id, String title, int releaseYear, String genre, double rating, MediaType mediaType, Integer episodeCount) {
    this.id = id;
    this.title = title;
    this.releaseYear = releaseYear;
    this.genre = genre;
    this.rating = rating;
    this.mediaType = mediaType;
    this.episodeCount = episodeCount;
  }

  @Override
  public String toString() {
    return String.format("%s (%d) - Genre: %s, Rating: %.1f/10.0, episodeCount: %d", title, releaseYear, genre, rating, episodeCount);
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

  public int getEpisodeCount() {
    return episodeCount;
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
