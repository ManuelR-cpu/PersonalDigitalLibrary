import java.util.List;
import java.util.Scanner;

public class LibraryView implements ViewInterface {

  private final Scanner input;

  public LibraryView() {
    this.input = new Scanner(System.in);
  }

  @Override
  public int showMenuAndGetChoice() {
    System.out.println("Options:");
    System.out.println("1. Add a new Movie/TVShow");
    System.out.println("2. View entire library");
    System.out.println("3. Search for Movie/TVShow");
    System.out.println("4. Delete Movie/TVShow");
    System.out.println("5. Exit");
    System.out.print("Enter your choice: ");
    int choice = input.nextInt();
    input.nextLine();
    return choice;
  }

  @Override
  public void showAllItems(List<IMediaItem> items) {
    if (items.isEmpty()) {
      System.out.println("No items in library");
      return;
    }
    System.out.println("\n--- Your library ---");
    for (IMediaItem item : items) {
      System.out.println(item.toString());
    }
    System.out.println("------------\n");
  }

  @Override
  public void showMessage(String message) {
    System.out.println(message);
  }

  @Override
  public String askForTitle(String purpose) {
    System.out.println("Enter title: " + purpose + ": ");
    return this.input.nextLine();
  }

  @Override
  public IMediaItem askForNewMediaItem() {
    System.out.print("Enter title: ");
    String title = input.nextLine();

    System.out.print("Enter year: ");
    int year = input.nextInt();
    input.nextLine();

    System.out.print("Enter genre: ");
    String genre = input.nextLine();

    System.out.print("Enter rating: ");
    double rating = input.nextDouble();
    input.nextLine();

    System.out.print("Enter media type: ");
    String mediaType = input.nextLine();

    if (mediaType.equalsIgnoreCase("Movie")) {
      IMediaItem newItem = new Movie(title, year, genre, rating, MediaType.MOVIE);
      return newItem;
    } else if (mediaType.equalsIgnoreCase("TVShow")) {
      System.out.print("Enter episode count: ");
      int episodeCount = input.nextInt();
      input.nextLine();

      IMediaItem newItem = new TVShow(title, year, genre, rating, MediaType.TV_SERIES, episodeCount);
      return newItem;
    } else {
      System.out.print("Invalid media type. Returning null.");
      return null;
    }
  }
}
