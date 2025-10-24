import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    Library myLibrary = new Library();
    LibraryController controller = new LibraryController(myLibrary);
    GraphicalView guiView = new GraphicalView(controller);

    controller.setView(guiView);
    controller.initializeView();

    primaryStage.setScene(guiView.getScene());
    primaryStage.setTitle("My Digital Library");
    primaryStage.show();
  }
  public static void main(String[] args) {
    launch(args);
  }
}
