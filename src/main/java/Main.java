import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Library myLibrary = new Library();
    Scanner input = new Scanner(System.in);
    boolean running = true;

    while (true) {
      System.out.println("Options:");
      System.out.println("1. Add a new Movie/TVShow");
      System.out.println("2. View entire library");
      System.out.println("3. Exit");
      System.out.print("Enter your choice: ");
      int choice = input.nextInt();
      input.nextLine();

      switch (choice) {
        case 1:
          System.out.print("Enter title: ");
          String title = input.nextLine();
          System.out.print("Enter year: ");
          int year = input.nextInt();
          System.out.print("Enter genre: ");
          String genre = input.nextLine();
          input.nextLine();
          System.out.print("Enter rating: ");
          double rating = input.nextDouble();
          System.out.print("Enter media type: ");
          String mediaType = input.nextLine();
          if (mediaType.equalsIgnoreCase("Movie")) {
            IMediaItem newItem = new Movie(title, year, genre, rating, MediaType.MOVIE);
            myLibrary.addItem(newItem);
          }
          if (mediaType.equalsIgnoreCase("TVShow")) {
            System.out.print("Enter episode count ");
            int episodeCount = input.nextInt();

            IMediaItem newItem = new TVShow(title, year, genre, rating, MediaType.TV_SERIES, episodeCount);
            myLibrary.addItem(newItem);
          }
          input.nextLine();
          break;
        case 2:
          myLibrary.viewAllItems();
          break;
          case 3:
            running = false;
            System.out.println("Goodbye!");
            break;
            default:
              System.out.println("Invalid choice");
      }
    }

  }
}
