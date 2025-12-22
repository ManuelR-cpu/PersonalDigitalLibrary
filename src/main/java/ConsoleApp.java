import Model.domain.IMediaItem;
import Model.Library;
import View.IConsoleView;
import View.LibraryView;

import java.util.List;

public class ConsoleApp {
  public static void main(String[] args) {
    Library myLibrary = new Library();

    IConsoleView view = new LibraryView();

    boolean running = true;
    while (running) {

      int choice = view.showMenuAndGetChoice();

      switch (choice) {
        case 1:
          IMediaItem newItem = view.askForNewMediaItem();
          if (newItem != null) {
            myLibrary.addItem(newItem);
            view.showMessage("Item added to Model.Library");
          } else {
            view.showMessage("No item added to Model.Library");
          }
          break;
        case 2:
          List<IMediaItem> allItems = myLibrary.getAllItems();
          view.showAllItems(allItems);
          break;
        case 3:
          String searchTitle = view.askForTitle("to search for");
          List<IMediaItem> results = myLibrary.searchForItems(searchTitle);
          view.showAllItems(results);
          break;
        case 4:
          String deleteTitle = view.askForTitle("to delete");
          boolean success = myLibrary.deleteItem(deleteTitle);
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
}
