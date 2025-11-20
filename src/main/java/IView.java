import java.util.List;

public interface IView {
  /**
   * Displays a list of media items.
   * @param items list of media items.
   */
  void showAllItems(List<IMediaItem> items);

  /**
   * Displays a message to the user.
   * @param message the message to be displayed.
   */
  void showMessage(String message);
}
