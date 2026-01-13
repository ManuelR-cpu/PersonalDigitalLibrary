package Model.service;

import Model.domain.IMediaItem;
import Model.domain.MediaType;
import java.util.List;
import java.util.UUID;

public interface LibraryService {
  /**
   * Adds a media item to the library.
   *
   * @param item either a Model.domain.Movie or Model.domain.TVShow.
   */
  void addItem(IMediaItem item);

  /**
   * Displays all the media items that have been added to the library.
   */
  List<IMediaItem> getAllItems();

  /**
   * Filters out media items by the type of media it is and returns the certain type in a list.
   * @param type the type of media to be filtered.
   * @return a list of media items of a specific media type.
   */
  List<IMediaItem> getItemsByType(MediaType type);

  /**
   * Searches a specific media item in the library.
   *
   * @param title the name of the item that is to be searched up.
   * @return returns a list of media items that contain the given string in their name.
   */
  List<IMediaItem> searchForItems(String title);

  /**
   * Deletes specified items off the library.
   *
   * @param uuid uuid of the media item to be deleted
   * @param type type of the media item to be deleted
   * @return returns true if the item was successfully deleted, false if the media item was not found.
   */
  boolean deleteItem(UUID uuid, MediaType type);

  void modifyItem(UUID uuid, IMediaItem item);
}
