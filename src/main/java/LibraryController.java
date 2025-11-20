import java.util.List;

public class LibraryController implements ControllerInterface {
  private final InterfaceLibrary library;
  private IView view;
  private Main sceneManager;

  public LibraryController(Library library, Main sceneManager) {
    this.library = library;
    this.sceneManager = sceneManager;
  }

  @Override
  public void setView(IView view) {
    this.view = view;
  }

  @Override
  public void deleteSelectedItem(String title) {
    boolean success = library.deleteItem(title);

    if (success) {
      view.showMessage("Item deleted successfully.");
      refreshView();
    } else {
      view.showMessage("No item with that title was found.");
    }
  }

  @Override
  public void addNewItem(IMediaItem item) {
    if (item != null) {
      library.addItem(item);
      view.showMessage("Item added to library successfully.");
      refreshView();
    } else {
      view.showMessage("No item to add to library.");
    }
  }

  private void refreshView() {
    List<IMediaItem> allItems = library.getAllItems();
    view.showAllItems(allItems);
  }

  @Override
  public void initializeView() {
    refreshView();
  }

  @Override
  public void requestAllItemsView() {
    List<IMediaItem> allItems = library.getAllItems();
    sceneManager.showLibraryView(allItems);
  }

  @Override
  public void requestMoviesView() {
    List<IMediaItem> movies = library.getItemsByType(MediaType.MOVIE);
    sceneManager.showLibraryView(movies);
  }

  @Override
  public void requestTVShowsView() {
    List<IMediaItem> shows = library.getItemsByType(MediaType.TV_SERIES);
    sceneManager.showLibraryView(shows);
  }

  @Override
  public void requestMainMenu() {
    sceneManager.showMainMenu();
  }

  @Override
  public void requestExit() {
    sceneManager.exitApp();
  }
}