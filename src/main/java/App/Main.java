package App;

import Controller.ControllerInterface;
import Controller.LibraryController;
import Model.IMediaItem;
import Model.InterfaceLibrary;
import Model.Library;
import View.GraphicalView;
import View.MainMenuView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {
  private Stage primaryStage;
  private ControllerInterface controller;
  private MainMenuView mainMenuView;
  private GraphicalView guiView;

  @Override
  public void start(Stage primaryStage) throws Exception {
    this.primaryStage = primaryStage;

    InterfaceLibrary myLibrary = new Library();

    this.controller = new LibraryController(myLibrary,this);

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

  public void exitApp() {
    primaryStage.close();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
