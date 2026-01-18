package Controller;

import Model.domain.IMediaItem;
import Model.domain.MediaType;

import Model.domain.Movie;
import Model.domain.TVShow;
import Model.domain.api.ApiClient;
import View.IView;
import App.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.management.relation.RoleUnresolved;

public class LibraryController implements ControllerInterface {
  private final ApiClient apiClient;
  private IView view;
  private final Main sceneManager;

  public LibraryController(ApiClient apiClient, Main sceneManager) {
    this.apiClient = apiClient;
    this.sceneManager = sceneManager;
  }

  @Override
  public void setView(IView view) {
    this.view = view;
  }

  @Override
  public void deleteSelectedItem(UUID selectedItemId, MediaType selectedItemType) {
    if (selectedItemId == null || selectedItemType == null) {
      throw new IllegalArgumentException("Arguments cannot be null.");
    }

    try {
      if (selectedItemType.equals(MediaType.MOVIE)) {
        apiClient.deleteMovie(selectedItemId);
      } else {
        apiClient.deleteTVShow(selectedItemId);
      }

      view.showMessage("Item deleted successfully.");
      refreshView();
    } catch (RuntimeException e) {
      view.showMessage("No item with that title was found.");
    }
  }

  @Override
  public void addNewItem(IMediaItem item) {
    if (item != null) {
      if (item.getMediaType().equals(MediaType.MOVIE)) {
        apiClient.createMovie((Movie) item);
      } else {
        apiClient.createTVShow((TVShow) item);
      }
      view.showMessage("Item added to library successfully.");
      refreshView();
    } else {
      view.showMessage("No item to add to library.");
    }
  }

  @Override
  public void updateItem(UUID uuid, IMediaItem item) {
    if (item != null) {
      if (item.getMediaType().equals(MediaType.MOVIE)) {
        apiClient.updateMovie((Movie) item);
      } else {
        apiClient.updateTVShow((TVShow) item);
      }
    } else {
      view.showMessage("No item to update.");
    }
  }

  private void refreshView() {
    List<IMediaItem> allItems = apiClient.getAllItems();
    view.showAllItems(allItems);
  }

  @Override
  public void initializeView() {
    refreshView();
  }

  @Override
  public void requestAllItemsView() {
    List<IMediaItem> allItems = apiClient.getAllItems();
    sceneManager.showLibraryView(allItems);
  }

  @Override
  public void requestMoviesView() {
    List<IMediaItem> movies = new ArrayList<>();
    movies.addAll(apiClient.getMovies());
    sceneManager.showLibraryView(movies);
  }

  @Override
  public void requestTVShowsView() {
    List<IMediaItem> shows = new ArrayList<>();
    shows.addAll(apiClient.getTVShows());
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
    List<IMediaItem> results = new ArrayList<>();
    results.addAll(apiClient.getMoviesByTitle(title));
    results.addAll(apiClient.getTVShowsByTitle(title));

    sceneManager.showLibraryView(results);
    if (results.isEmpty()) {
      view.showMessage("No items found with " + title + ".");
    }
  }
}