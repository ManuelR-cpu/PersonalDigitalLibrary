import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {

  private Stage primaryStage;
  private LibraryController controller;
  private MainMenuView mainMenuView;
  private GraphicalView guiView;

  @Override
  public void start(Stage primaryStage) throws Exception {
    this.primaryStage = primaryStage;

    Library myLibrary = new Library();

    LibraryController controller = new LibraryController(myLibrary, this);

    this.mainMenuView = new MainMenuView(controller);
    this.guiView = new GraphicalView(controller);

    primaryStage.setTitle("My Digital Library");

    showMainMenu();
  }

  public void showMainMenu() {
    primaryStage.setScene(mainMenuView.getScene());
    primaryStage.show();
  }

  public void showLibraryView(List<IMediaItem> items) {
    controller.setView(guiView);

    guiView.updateList(items);

    primaryStage.setScene(guiView.getScene());
    primaryStage.show();
  }

  public void
  public static void main(String[] args) {
    launch(args);
  }
}
