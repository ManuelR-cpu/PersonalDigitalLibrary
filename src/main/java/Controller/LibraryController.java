package Controller;

import Model.domain.IMediaItem;
import Model.domain.MediaType;
import Model.service.LibraryService;
import View.IView;
import App.Main;

import java.util.List;
import java.util.UUID;

public class LibraryController implements ControllerInterface {
  private final LibraryService library;
  private IView view;
  private final Main sceneManager;

  public LibraryController(LibraryService library, Main sceneManager) {
    this.library = library;
    this.sceneManager = sceneManager;
  }

  @Override
  public void setView(IView view) {
    this.view = view;
  }

  @Override
  public void deleteSelectedItem(UUID selectedItemId, MediaType selectedItemType) {
    boolean success = library.deleteItem(selectedItemId, selectedItemType);

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

  @Override
  public void updateItem(UUID uuid, IMediaItem item) {
    if (item != null) {
      library.modifyItem(uuid, item);
    } else {
      view.showMessage("No item to update.");
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

  @Override
  public void requestSearch(String title) {
    List<IMediaItem> results = library.searchForItems(title);
    sceneManager.showLibraryView(results);
    if (results.isEmpty()) {
      view.showMessage("No items found with " + title + ".");
    }
  }
}