public class TVShow implements IMediaItem {
  private String title;
  private int releaseYear;
  private String genre;
  private double rating;
  private int episodeCount;
  private MediaType mediaType;

  TVShow(String title, int releaseYear, String genre, double rating, MediaType mediaType ,int episodeCount) {
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
}
