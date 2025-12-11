package View;

import Controller.ControllerInterface;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class MainMenuView {
  private final ControllerInterface controller;
  private final Scene menuScene;

  public MainMenuView(ControllerInterface controller) {
    this.controller = controller;
    this.menuScene = initUI();
  }

  public Scene getScene() {
    return menuScene;
  }

  private Scene initUI() {
    VBox root = new VBox();
    root.setPadding(new Insets(50));
    root.setAlignment(Pos.CENTER);

    Button viewAllButton = new Button("View All Library");
    viewAllButton.setPrefSize(200, 50);

    Button viewMoviesButton = new Button("View Movies");
    viewMoviesButton.setPrefSize(200, 50);

    Button viewTVShowsButton = new Button("View TV Shows");
    viewTVShowsButton.setPrefSize(200, 50);

    Button exitButton = new Button("Exit");
    exitButton.setPrefSize(200, 50);

    viewAllButton.setOnAction(event -> {
      controller.requestAllItemsView();
    });

    viewMoviesButton.setOnAction(event -> {
      controller.requestMoviesView();
    });

    viewTVShowsButton.setOnAction(event -> {
      controller.requestTVShowsView();
    });

    exitButton.setOnAction(event -> {
      controller.requestExit();
    });

    root.getChildren().addAll(viewAllButton, viewMoviesButton, viewTVShowsButton, exitButton);

    return new Scene(root, 400, 400);
  }
}
