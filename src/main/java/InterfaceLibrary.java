import java.util.List;

public interface InterfaceLibrary {

  /**
   * Adds a media item to the library.
   *
   * @param item either a Movie or TVShow.
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
   * @return returns the media item that was searched up.
   */
  List<IMediaItem> searchForItems(String title);

  /**
   * Deletes specified items off the library.
   *
   * @param title title of the media item to be
   * @return returns true if the item was successfully deleted, false if the media item was not found.
   */
  boolean deleteItem(String title);
}
