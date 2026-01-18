package App;

import Controller.ControllerInterface;
import Controller.LibraryController;
import Model.domain.IMediaItem;
import Model.domain.api.ApiClient;
import View.GraphicalView;
import View.MainMenuView;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpClient;
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

    HttpClient httpClient = HttpClient.newHttpClient();
    ObjectMapper mapper = new ObjectMapper();

    ApiClient apiClient = new ApiClient(httpClient, mapper, "http://localhost:8080");

    this.controller = new LibraryController(apiClient,this);

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
