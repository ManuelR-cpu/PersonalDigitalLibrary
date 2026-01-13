package View;

import Controller.ControllerInterface;
import Model.domain.IMediaItem;
import Model.domain.MediaType;
import Model.domain.Movie;
import Model.domain.TVShow;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;

import java.util.Comparator;
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
    Label emptyLabel = new Label("No items in library");
    emptyLabel.setStyle("\"-fx-text-fill: gray; -fx-font-size: 14px;\"");
    itemListView.setPlaceholder(emptyLabel);

    itemListView.setCellFactory(param -> new ListCell<IMediaItem>() {
      @Override
      protected void updateItem(IMediaItem item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
          setText(null);
          setGraphic(null);
        } else {
          VBox container = new VBox(3); // 3px spacing between lines

          // bolds title/year
          String typeStr = (item.getMediaType() == MediaType.MOVIE) ? "[Movie]" : "[TV]";
          Label titleLabel = new Label(typeStr + " " + item.getTitle() + " (" + item.getReleaseYear() + ")");
          titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

          // line for details on the items
          Label detailsLabel = new Label("Genre: " + item.getGenre() + " | Rating: " + item.getRating() + "/10");
          detailsLabel.setStyle("-fx-text-fill: #555555;"); // dark gray color

          container.getChildren().addAll(titleLabel, detailsLabel);

          setText(null);
          setGraphic(container);
        }
      }
    });

    itemListView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldValue, newValue) -> {
              if (newValue != null) {
                System.out.println("Clicked on " + newValue.getTitle());
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this item?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
          controller.deleteSelectedItem(selectedItem.getId(), selectedItem.getMediaType());
        }
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
    backButton.setOnAction(event -> {
      controller.requestMainMenu();
    });

    ComboBox<String> sortBox = new ComboBox<>();
    sortBox.getItems().addAll("Sort by Title", "Sort by Year", "Sort by Rating");
    sortBox.setPromptText("Sort By...");

    sortBox.setOnAction(event -> {
      String criteria = sortBox.getValue();
      if (criteria == null) return;

      switch (criteria) {
        case "Sort by Title":
          // Sort A-Z
          itemListView.getItems().sort(Comparator.comparing(IMediaItem::getTitle));
          break;
        case "Sort by Year":
          // Sort Newest First (b - a)
          itemListView.getItems().sort((item1, item2) ->
                  Integer.compare(item2.getReleaseYear(), item1.getReleaseYear()));
          break;
        case "Sort by Rating":
          // Sort Highest Rated First (b - a)
          itemListView.getItems().sort((item1, item2) ->
                  Double.compare(item2.getRating(), item1.getRating()));
          break;
      }
    });

    topBar.getChildren().addAll(backButton, searchField, searchButton, sortBox);
    topBar.setAlignment(Pos.CENTER_LEFT);
    root.setTop(topBar);

    return new Scene(root, 600, 400);
  }

  private Optional<IMediaItem> showAddItemDialog() {
    Dialog<IMediaItem> dialog = new Dialog<>();
    dialog.setTitle("Add New Media Item");
    dialog.setHeaderText("Enter the details for the new item");

    dialog.setResizable(true);
    dialog.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

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

    TextField[] tvFields = createTVFields();
    TextField episodesField = tvFields[0];
    TextField seasonsField = tvFields[1];

    TextField[] movieFields = createMovieFields();
    TextField directorField = movieFields[0];
    TextField durationField = movieFields[1];

    Label episodesLabel = new Label("Episodes:");
    Label seasonsLabel = new Label("Seasons:");
    Label directorLabel = new Label("Director:");
    Label durationLabel = new Label("Duration:");

    Button addSeasonRating = new Button("Add Season Rating");
    addSeasonRating.setDisable(true);

    VBox seasonRatingsBox = new VBox(8);
    seasonRatingsBox.setFillWidth(true);

    // usage of LinkedHashMap here to maintain  order of the seasons in the
    Map<Integer, TextField> seasonRatingFields = new LinkedHashMap<>();

    Label seasonRatingsLabel = new Label("Season Ratings:");

    seasonRatingsLabel.setVisible(false);
    addSeasonRating.setVisible(false);
    seasonRatingsBox.setVisible(false);

    seasonRatingsLabel.managedProperty().bind(seasonRatingsLabel.visibleProperty());
    addSeasonRating.managedProperty().bind(addSeasonRating.visibleProperty());
    seasonRatingsBox.managedProperty().bind(seasonRatingsBox.visibleProperty());

    // helper to parse # of seasons from the Field
    java.util.function.IntSupplier seasonsCount = () -> {
      try {
        int n = Integer.parseInt(seasonsField.getText().trim());
        return (n > 0) ? n : -1;
      } catch (Exception e) {
        return -1;
      }
    };

    seasonsField.textProperty().addListener((observable, oldValue, newValue) -> {
      int n = seasonsCount.getAsInt();
      // TODO: why dont you just check >= 0?
      boolean validSeasonCount = n > 0 && seasonRatingFields.size() < n;
      // not a valid season count means disable the button from being clicked
      addSeasonRating.setDisable(!validSeasonCount);

      if (n > 0 && seasonRatingFields.size() > n) {
        for (int i = seasonRatingFields.size(); i > n; i--) {
          // remove from LinkedHashMap (here we see purpose of map being in insertion order)
          // and remove the TextFields from the seasonVBox
          TextField tf = seasonRatingFields.remove(i);

          seasonRatingsBox.getChildren().removeIf(node -> node instanceof HBox && ((HBox) node).getChildren().contains(tf));
        }
      }
    });

    addSeasonRating.setOnAction(event -> {
      int n = seasonsCount.getAsInt();
      if (n <= 0) return;

      int nextSeason = seasonRatingFields.size() + 1;

      if (nextSeason > n) {
        addSeasonRating.setDisable(true);
        return;
      }

      Label seasonRatingLabel = new Label("Season " + nextSeason + ":");
      TextField seasonRatingField = new TextField();
      seasonRatingField.setPromptText("Rating (1.0 - 10.0)");
      seasonRatingLabel.setMinWidth(80);

      HBox row = new HBox(10, seasonRatingLabel, seasonRatingField);

      seasonRatingsBox.getChildren().add(row);
      seasonRatingFields.put(nextSeason, seasonRatingField);

      addSeasonRating.setDisable(seasonRatingFields.size() >= n);

      dialog.getDialogPane().applyCss();
      dialog.getDialogPane().layout();
      dialog.getDialogPane().getScene().getWindow().sizeToScene();
    });

    episodesLabel.setVisible(false);
    episodesField.setVisible(false);
    seasonsLabel.setVisible(false);
    seasonsField.setVisible(false);

    directorLabel.setVisible(true);
    directorField.setVisible(true);
    durationLabel.setVisible(true);
    durationField.setVisible(true);

    directorLabel.managedProperty().bind(directorLabel.visibleProperty());
    directorField.managedProperty().bind(directorField.visibleProperty());
    durationLabel.managedProperty().bind(durationLabel.visibleProperty());
    durationField.managedProperty().bind(durationField.visibleProperty());

    episodesLabel.managedProperty().bind(episodesLabel.visibleProperty());
    episodesField.managedProperty().bind(episodesField.visibleProperty());
    seasonsLabel.managedProperty().bind(seasonsLabel.visibleProperty());
    seasonsField.managedProperty().bind(seasonsField.visibleProperty());

    typeComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
      boolean isMovie = (newValue == MediaType.MOVIE);

      directorField.setVisible(isMovie);
      directorLabel.setVisible(isMovie);
      durationLabel.setVisible(isMovie);
      durationField.setVisible(isMovie);

      episodesLabel.setVisible(!isMovie);
      episodesField.setVisible(!isMovie);
      seasonsLabel.setVisible(!isMovie);
      seasonsField.setVisible(!isMovie);

      seasonRatingsLabel.setVisible(!isMovie);
      addSeasonRating.setVisible(!isMovie);
      seasonRatingsBox.setVisible(!isMovie);

      if (isMovie) {
        seasonRatingFields.clear();
        seasonRatingsBox.getChildren().clear();
        addSeasonRating.setDisable(true);
      } else {
        int n = seasonsCount.getAsInt();
        addSeasonRating.setDisable(!(n > 0 && seasonRatingFields.size() < n));
      }

      dialog.getDialogPane().applyCss();
      dialog.getDialogPane().layout();
      dialog.getDialogPane().getScene().getWindow().sizeToScene();
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

    grid.add(directorLabel, 0, 5);
    grid.add(directorField, 1, 5);
    grid.add(durationLabel, 0, 6);
    grid.add(durationField, 1, 6);

    grid.add(episodesLabel, 0, 5);
    grid.add(episodesField, 1, 5);
    grid.add(seasonsLabel, 0, 6);
    grid.add(seasonsField, 1, 6);

    grid.add(seasonRatingsLabel, 0, 7);
    grid.add(addSeasonRating, 1, 7);

    grid.add(seasonRatingsBox, 0, 8, 2, 1);

    dialog.getDialogPane().setContent(grid);

    dialog.getDialogPane().applyCss();
    dialog.getDialogPane().layout();
    dialog.getDialogPane().getScene().getWindow().sizeToScene();

    dialog.setResultConverter(dialogButton -> {
      if (dialogButton == okButtonType) {
        try {
          String title = titleField.getText();
          int year = Integer.parseInt(yearField.getText());
          String genre = genreField.getText();
          double rating = Double.parseDouble(ratingField.getText());

          if (typeComboBox.getValue() == MediaType.MOVIE) {
            String director = directorField.getText();
            int duration = Integer.parseInt(durationField.getText());

            return new Movie(title, year, genre, rating, MediaType.MOVIE, director, duration);
          } else {
            int episodes = Integer.parseInt(episodesField.getText());
            int seasons = Integer.parseInt(seasonsField.getText());

            TVShow show = new TVShow(title, year, genre, rating, MediaType.TV_SERIES, episodes, seasons);

            for (Map.Entry<Integer, TextField> entry : seasonRatingFields.entrySet()) {
              String text = entry.getValue().getText().trim();
              if (text.isEmpty()) continue;
              Double seasonRatingVal = Double.parseDouble(text);

              show.getSeasonRatings().put(entry.getKey(), seasonRatingVal);
            }

            return show;
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

  private TextField[] createMovieFields() {
    TextField directorField = new TextField();
    directorField.setPromptText("Director");

    TextField durationField = new TextField();
    durationField.setPromptText("Duration (minutes)");

    return new TextField[] { directorField, durationField };
  }

  private TextField[] createTVFields() {
    TextField episodesField = new TextField();
    episodesField.setPromptText("Episode Count");

    TextField seasonsField = new TextField();
    seasonsField.setPromptText("Season Count");

    return new TextField[] { episodesField, seasonsField };
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
