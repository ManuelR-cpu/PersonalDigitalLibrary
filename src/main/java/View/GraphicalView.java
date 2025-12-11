package View;

import Controller.ControllerInterface;
import Controller.LibraryController;
import Model.IMediaItem;
import Model.MediaType;
import Model.Movie;
import Model.TVShow;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;

import java.util.Optional;

import java.util.List;

public class GraphicalView implements IView {

  private final ControllerInterface controller;
  private final Scene mainScene;

  private ListView<IMediaItem> itemListView;

  public GraphicalView(ControllerInterface controller) {
    this.controller = controller;
    this.mainScene = initUI();
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

    Button addButton = new Button("Add Item");
    Button deleteButton = new Button("Delete");

    addButton.setOnAction(event -> {
      Optional<IMediaItem> result = showAddItemDialog();
      result.ifPresent(newItem -> {
        controller.addNewItem(newItem);
      });

    });
    deleteButton.setOnAction(event -> {
      IMediaItem selectedItem = itemListView.getSelectionModel().getSelectedItem();
      if (selectedItem != null) {
        controller.deleteSelectedItem(selectedItem.getTitle());
      } else {
        showMessage("Please select an item to delete");
      }
    });

    VBox buttonBox = new VBox(10);
    buttonBox.getChildren().addAll(addButton, deleteButton);
    root.setRight(buttonBox);

    HBox topBar = new HBox();
    topBar.setPadding(new Insets(10, 10, 10, 10));
    topBar.setSpacing(10);

    TextField searchField = new TextField();
    searchField.setPromptText("Search");

    Button searchButton = new Button("Search");
    searchButton.setOnAction(event -> {
      String searchText = searchField.getText();
      controller.requestSearch(searchText);
    });

    Button backButton = new Button("Back To Main Menu");
    backButton.setOnAction(event -> {controller.requestMainMenu();});

    topBar.getChildren().addAll(backButton, searchField, searchButton);
    topBar.setAlignment(Pos.CENTER_LEFT);
    root.setTop(topBar);

    return new Scene(root, 600, 400);
  }

  private Optional<IMediaItem> showAddItemDialog() {
    Dialog<IMediaItem> dialog = new Dialog<>();
    dialog.setTitle("Add New Media Item");
    dialog.setHeaderText("Enter the details for the new item");

    ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20, 150, 10, 10));

    ComboBox<MediaType> typeComboBox = new ComboBox<>();
    typeComboBox.getItems().setAll(MediaType.MOVIE, MediaType.TV_SERIES);
    typeComboBox.setValue(MediaType.MOVIE);

    TextField titleField = new TextField();
    titleField.setPromptText("Title");
    TextField yearField = new TextField();
    yearField.setPromptText("Year");
    TextField genreField = new TextField();
    genreField.setPromptText("Genre");
    TextField ratingField = new TextField();
    ratingField.setPromptText("Rating (1.0 - 10.0)");

    Label episodeCountLabel = new Label("Episode Count");
    TextField episodeCountField = new TextField();
    episodeCountField.setPromptText("Episode Count");
    episodeCountLabel.setVisible(false);
    episodeCountField.setVisible(false);

    typeComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
      boolean isTVShow = (newValue == MediaType.TV_SERIES);
      episodeCountLabel.setVisible(isTVShow);
      episodeCountField.setVisible(isTVShow);
    });

    grid.add(new Label("Type:"), 0, 0);
    grid.add(typeComboBox, 1, 0);
    grid.add(new Label("Title:"), 0, 1);
    grid.add(titleField, 1, 1);
    grid.add(new Label("Year:"), 0, 2);
    grid.add(yearField, 1, 2);
    grid.add(new Label("Genre:"), 0, 3);
    grid.add(genreField, 1, 3);
    grid.add(new Label("Rating:"), 0, 4);
    grid.add(ratingField, 1, 4);
    grid.add(episodeCountLabel, 0, 5);
    grid.add(episodeCountField, 1, 5);

    dialog.getDialogPane().setContent(grid);

    dialog.setResultConverter(dialogButton -> {
      if (dialogButton == okButtonType) {
        try {
          String title = titleField.getText();
          int year = Integer.parseInt(yearField.getText());
          String genre = genreField.getText();
          double rating = Double.parseDouble(ratingField.getText());
          MediaType mediaType = typeComboBox.getValue();

          if (mediaType == MediaType.MOVIE) {
            return new Movie(title, year, genre, rating, mediaType);
          } else {
            int episodes = Integer.parseInt(episodeCountField.getText());
            return new TVShow(title, year, genre, rating, mediaType, episodes);
          }
        } catch (NumberFormatException e) {
          showMessage("Invalid input. Please check your numbers");
          return null;
        }
      }
      return null;
    });
    return dialog.showAndWait();
  }

  @Override
  public void showAllItems(List<IMediaItem> items) {
    itemListView.getItems().clear();
    itemListView.getItems().addAll(items);
  }

  @Override
  public void showMessage(String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Model.Library Notification");
    alert.setHeaderText(null);
    alert.setContentText(message);

    alert.showAndWait();
  }

  public void updateList(List<IMediaItem> items) {
    itemListView.getItems().clear();
    itemListView.getItems().addAll(items);
  }
}
