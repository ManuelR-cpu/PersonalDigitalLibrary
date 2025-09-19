/**
 * Represents a MediaItem.
 */
public interface IMediaItem {
  /**
   * Gets the title of a mediaItem.
   * @return string representing the title of a piece of media.
   */
  public String getTitle();

  /**
   * Gets the year an item was released.
   * @return int representing the year item was released.
   */
  public int getReleaseYear();

  /**
   * Gets the genre of an item.
   * @return string representing the genre of an item.
   */
  public String getGenre();

  /**
   * Gets rating of an item.
   * @return double representing an items rating.
   */
  public double getRating();

  /**
   * Gets the media of an item.
   * @return one of the types being movie or tv_series
   */
  public MediaType getMediaType();
}
