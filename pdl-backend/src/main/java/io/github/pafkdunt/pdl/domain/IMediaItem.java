package io.github.pafkdunt.pdl.domain;

import java.util.UUID;

/**
 * Represents a MediaItem.
 */
public interface IMediaItem {
  /**
   * Gets the UUID of an item.
   *
   * @return the UUID of an item.
   */
  UUID getId();

  /**
   * Gets the title of a mediaItem.
   *
   * @return string representing the title of a piece of media.
   */
  String getTitle();

  /**
   * Gets the year an item was released.
   *
   * @return int representing the year item was released.
   */
  int getReleaseYear();

  /**
   * Gets the genre of an item.
   *
   * @return string representing the genre of an item.
   */
  String getGenre();

  /**
   * Gets rating of an item.
   *
   * @return double representing an items rating.
   */
  double getRating();

  /**
   * Gets the media of an item.
   *
   * @return one of the types being MOVIE or TV_SERIES
   */
  MediaType getMediaType();
}
