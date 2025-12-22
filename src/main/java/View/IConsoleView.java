package View;

import Model.domain.IMediaItem;

import java.util.List;

public interface IConsoleView extends IView {

  /**
   * Prints the choices the user can make and get the users input.
   * @return numbered choice the user made.
   */
  int showMenuAndGetChoice();

  /**
   * Prints out all the items in the list of media.
   * @param items list of media items.
   */
  void showAllItems(List<IMediaItem> items);

  /**
   * Displays a message for the user.
   * @param message the message to be displayed.
   */
  void showMessage(String message);

  /**
   * Prompts user for title for whatever purpose is being used for.
   * @param purpose purpose fo which title will be used for.
   * @return a string asking user for title and the purpose of the title.
   */
  String askForTitle(String purpose);

  /**
   * Prompts user for the details of new item.
   * @return new media item.
   */
  IMediaItem askForNewMediaItem();
}
