import java.util.List;
import java.util.ArrayList;

/**
 * Library that manages all the media that is inputted into the catalog.
 */
public class Library implements InterfaceLibrary {
  private List<IMediaItem> items;

  public Library() {
    this.items = new ArrayList<>();
  }

  @Override
  public void addItem(IMediaItem item) {
    this.items.add(item);
    System.out.println("Item added to library: " + item.getTitle());
  }

  @Override
  public void viewAllItems() {
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
}

