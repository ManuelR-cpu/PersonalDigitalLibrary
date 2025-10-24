public class Movie implements IMediaItem {
  private final String title;
  private final int releaseYear;
  private final String genre;
  private final double rating;
  private final MediaType mediaType;

  Movie(String title, int releaseYear, String genre, double rating, MediaType mediaType) {
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
