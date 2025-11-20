import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenuView {
  private final LibraryController controller;
  private final Scene menuScene;

  public MainMenuView(LibraryController controller) {
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
      System.out.println("Viewing All Library");
    });

    viewMoviesButton.setOnAction(event -> {
      System.out.println("Viewing Movies");
    });

    viewTVShowsButton.setOnAction(event -> {
      System.out.println("Viewing TV Shows");
    });

    exitButton.setOnAction(event -> {
      System.out.println("Exiting Application");
    });

    root.getChildren().addAll(viewAllButton, viewMoviesButton, viewTVShowsButton, exitButton);

    return new Scene(root, 400, 400);
  }
}
