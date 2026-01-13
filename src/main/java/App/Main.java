package App;

import Controller.ControllerInterface;
import Controller.LibraryController;
import Model.domain.IMediaItem;
import Model.repository.DataSourceFactory;
import Model.repository.MediaRepository;
import Model.repository.SqlMediaRepository;
import Model.service.LibraryService;
import Model.service.MediaLibraryService;
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

    var ds = DataSourceFactory.createDataSource();
    MediaRepository mediaRepository = new SqlMediaRepository(ds);
    LibraryService myLibrary = new MediaLibraryService(mediaRepository);

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
