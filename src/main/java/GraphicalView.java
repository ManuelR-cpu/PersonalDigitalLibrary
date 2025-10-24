import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class GraphicalView implements ViewInterface {

  private final LibraryController controller;
  private final Scene mainScene;

  private ListView<IMediaItem> itemListView;
  private Button addButton;
  private Button deleteButton;

  public GraphicalView(LibraryController controller) {
    this.controller = controller;
    this.mainScene = initUI();

    controller.initializeView();
  }

  public Scene getScene() {
    return this.mainScene;
  }

  private Scene initUI() {
    BorderPane root = new BorderPane();

    itemListView = new ListView<>();
    itemListView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldValue, newValue) -> {
              if (newValue != null) {
                System.out.println("Clicked on" + newValue.getTitle());
              }
            });
    root.setCenter(itemListView);

    addButton = new Button("Add Item");
    deleteButton = new Button("Delete Selected");

    addButton.setOnAction(event -> {
      System.out.println("Add button clicked");
    });
    deleteButton.setOnAction(event -> {
      IMediaItem selectedItem = itemListView.getSelectionModel().getSelectedItem();
      if (selectedItem != null) {
        controller.deleteSelectedItem(selectedItem.getTitle());
      }
      else {
        System.out.println("Please select an item to delete.");
      }
    });

    VBox buttonBox = new VBox(10);
    buttonBox.getChildren().addAll(addButton, deleteButton);
    root.setRight(buttonBox);

    return new Scene(root, 600, 400);
  }

  @Override
  public int showMenuAndGetChoice() {
    return 0;
  }

  @Override
  public void showAllItems(List<IMediaItem> items) {
    itemListView.getItems().clear();
    itemListView.getItems().addAll(items);
  }

  @Override
  public void showMessage(String message) {
    System.out.println("MESSAGE: " + message);
  }

  @Override
  public String askForTitle(String purpose) {
    return null;
  }

  @Override
  public IMediaItem askForNewMediaItem() {
    return null;
  }
}
