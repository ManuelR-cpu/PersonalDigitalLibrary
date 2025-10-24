import java.util.List;
import java.util.Scanner;

public class LibraryController {
  private final InterfaceLibrary library;
  private ViewInterface view;
  private boolean running;

  public LibraryController(Library library) {
    this.library = library;
    this.view = new LibraryView();
    this.running = true;
  }

  public void run() {
    while (running) {

      int choice = view.showMenuAndGetChoice();

      switch (choice) {
        case 1:
          IMediaItem newItem = view.askForNewMediaItem();
          if (newItem != null) {
            library.addItem(newItem);
            view.showMessage("Item added to library");
          } else {
            view.showMessage("No item added to library");
          }
          break;
        case 2:
          List<IMediaItem> allItems = library.getAllItems();
          view.showAllItems(allItems);
          break;
        case 3:
          String searchTitle = view.askForTitle("to search for");
          List<IMediaItem> results = library.searchForItems(searchTitle);
          view.showAllItems(results);
          break;
        case 4:
          String deleteTitle = view.askForTitle("to delete");
          boolean success = library.deleteItem(deleteTitle);
          if (success) {
            view.showMessage("Item deleted successfully");
          } else {
            view.showMessage("Item not deleted successfully");
          }
          break;
        case 5:
          running = false;
          view.showMessage("Goodbye!");
          break;
        default:
          view.showMessage("Invalid choice");
      }
    }
  }

  public void setView(ViewInterface view) {
    this.view = view;
  }

  public void deleteSelectedItem(String title){
    boolean success = library.deleteItem(title);

    if (success) {
      view.showMessage("Item deleted successfully.");
      refreshView();
    } else {
      view.showMessage("No item with that title was found.");
    }
  }

  public void refreshView() {
    List<IMediaItem> allItems = library.getAllItems();
    view.showAllItems(allItems);
  }

  public void initializeView() {
    refreshView();
  }
}