package Model;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

import com.google.gson.Gson;

/**
 * A library implementation that manages the inputted media in the catalog.
 */
public class Library implements InterfaceLibrary {
  private final List<IMediaItem> items;
  private static final String LIBRARY_FILE_NAME = "library.json";
  private final Gson gson = new Gson();

  //Private helper class whose purpose is to be a simple data container for saving/loading.
  private static class LibraryData {
    List<Movie> movies = new ArrayList<>();
    List<TVShow> tvShows = new ArrayList<>();
  }

  public Library() {
    this.items = loadFromFile();
  }

  @Override
  public void addItem(IMediaItem item) {
    this.items.add(item);
    saveToFile();
  }

  @Override
  public List<IMediaItem> getAllItems() {
    return this.items;
  }

  @Override
  public List<IMediaItem> getItemsByType(MediaType type) {
    List<IMediaItem> filteredItems = new ArrayList<>();

    for (IMediaItem item : this.items) {
      if (item.getMediaType().equals(type)) {
        filteredItems.add(item);
      }
    }
    return filteredItems;
  }


  @Override
  public List<IMediaItem> searchForItems(String title) {
    ArrayList<IMediaItem> foundItems = new ArrayList<>();

    for (IMediaItem item : this.items) {
      if (item.getTitle().equalsIgnoreCase(title)) {
        foundItems.add(item);
      }
    }
    return foundItems;
  }

  @Override
  public boolean deleteItem(String title) {
    boolean removed = this.items.removeIf(item -> item.getTitle().equalsIgnoreCase(title));

    if (removed) {
      saveToFile();
    }
    return removed;
  }

  private void saveToFile() {
    LibraryData libraryData = new LibraryData();

    for (IMediaItem item : this.items) {
      if (item instanceof Movie) {
        libraryData.movies.add((Movie) item);
      } else if (item instanceof TVShow) {
        libraryData.tvShows.add((TVShow) item);
      }
    }

    try (Writer writer = new FileWriter(LIBRARY_FILE_NAME)) {
      gson.toJson(libraryData, writer);
    } catch (IOException e) {
    }
  }

  private List<IMediaItem> loadFromFile() {
    try (Reader reader = new FileReader(LIBRARY_FILE_NAME)) {
      LibraryData libraryData = gson.fromJson(reader, LibraryData.class);

      if (libraryData != null) {
        List<IMediaItem> combinedList = new ArrayList<>();
        if (libraryData.movies != null) {
          combinedList.addAll(libraryData.movies);
        }
        if (libraryData.tvShows != null) {
          combinedList.addAll(libraryData.tvShows);
        }
        return combinedList;
      }
    } catch (FileNotFoundException e) {
    } catch (IOException e) {
    }
    return new ArrayList<>();
  }
}

