public interface ControllerInterface {
  /**
   * Sets which view class for the controller to use.
   *
   * @param view the type of view to use.
   */
  void setView(IView view);

  /**
   * From the library it gets the item to be deleted.
   *
   * @param title name of the item to be deleted.
   */
  void deleteSelectedItem(String title);

  /**
   * Adds item to the list for the view.
   *
   * @param item item to add to the list.
   */
  void addNewItem(IMediaItem item);

  /**
   * Initializes the view of the application.
   */
  void initializeView();

  /**
   * Request from scene manager to show library with all items.
   */
  void requestAllItemsView();

  /**
   * Request from scene manager to show library with only movies.
   */
  void requestMoviesView();

  /**
   * Request from scene manager to show library with only TVShows.
   */
  void requestTVShowsView();

  /**
   * Request from scene manager to return to the main menu.
   */
  void requestMainMenu();

  /**
   * Request from the scene manager to exit the application completely.
   */
  void requestExit();
}
