public interface ControllerInterface {

  /**
   * Enables the logic for running main application.
   */
  void run();

  /**
   * Sets which view class for the controller to use.
   *
   * @param view the type of view to use.
   */
  void setView(ViewInterface view);

  /**
   * From the library it gets the item to be deleted.
   *
   * @param title name of the item to be deleted.
   */
  void deleteSelectedItem(String title);

  /**
   * Adds item to the list for the view.
   * @param item item to add to the list.
   */
  void addNewItem(IMediaItem item);

  /**
   * Initializes the view of the application.
   */
  void initializeView();
}
