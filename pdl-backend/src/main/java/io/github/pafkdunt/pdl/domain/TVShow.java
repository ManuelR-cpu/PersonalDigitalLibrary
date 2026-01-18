package io.github.pafkdunt.pdl.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TVShow extends AbstractMediaItem {
  private Integer episodeCount;
  private Integer seasonCount;
  private Map<Integer, Double> seasonRatings;

  // for constructing new Movie objects
  public TVShow(String title, int releaseYear, String genre, double rating, MediaType mediaType, Integer episodeCount,  Integer seasonCount) {
    super(title, releaseYear, genre, rating,  mediaType);
    this.episodeCount = episodeCount;
    this.seasonCount = seasonCount;
    this.seasonRatings = new HashMap<>();
  }

  // for constructing TVShow objects already in the database
  @JsonCreator
  public TVShow(
      @JsonProperty("id") UUID id,
      @JsonProperty("title") String title,
      @JsonProperty("releaseYear") int releaseYear,
      @JsonProperty("genre") String genre,
      @JsonProperty("rating") double rating,
      @JsonProperty("mediaType") MediaType mediaType,
      @JsonProperty("episodeCount") Integer episodeCount,
      @JsonProperty("seasonCount") Integer seasonCount,
      @JsonProperty("seasonRatings") Map<Integer, Double> seasonRatings
  ) {
    super(id, title, releaseYear, genre, rating, mediaType);
    this.episodeCount = episodeCount;
    this.seasonCount = seasonCount;
    this.seasonRatings = seasonRatings;
  }

  @Override
  public String toString() {
    return String.format("%s (%d) - Genre: %s, Rating: %.1f/10.0, episodeCount: %d", title, releaseYear, genre, rating, episodeCount);
  }

  public int getEpisodeCount() {
    return episodeCount;
  }

  public int getSeasonCount() {
    return seasonCount;
  }

  public Map<Integer, Double> getSeasonRatings() {
    return seasonRatings == null ? null : new HashMap<>(seasonRatings);
  }
}
