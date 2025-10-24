import java.util.List;
public interface InterfaceLibrary {

  /**
   * Adds a media item to the library.
   * @param item either a Movie or TVShow.
   */
  public void addItem(IMediaItem item);

  /**
   * Displays all the media items that have been added to the library.
   */
  public List<IMediaItem> getAllItems();

  /**
   * Searches a specific media item in the library.
   * @param title the name of the item that is to be searched up.
   * @return returns the media item that was searched up.
   */
  public List<IMediaItem> searchForItems(String title);

  /**
   * Deletes specified items off the library.
   * @param title title of the media item to be
   * @return returns true if the item was successfully deleted, false if the media item was not found.
   */
  public boolean deleteItem(String title);

}
