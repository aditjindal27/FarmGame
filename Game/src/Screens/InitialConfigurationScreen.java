import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class InitialConfigurationScreen extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {

        // Create main layout pane (root), create HBoxes and VBoxes and add it to root
        /*
         *             mainVBox:
         *  ________________________________
         * |        "Get Started:"          |
         *  ________________________________
         * |           mainHBox             |
         * |                                |
         * |      leftVBox | rightVBox      |
         * |               |                |
         * |       "name:" | TextField      |
         * | "difficulty:" | ComboBox       |
         * |           ... | ...            |
         *  –––––––––––––––––––––––––––––––––
         * |        submit button           |
         *  ————————————————————————————————
         */
        VBox mainVBox = new VBox(30);
        HBox mainHBox = new HBox(20);
        mainHBox.setAlignment(Pos.CENTER);
        mainHBox.setMaxWidth(400);
        mainHBox.setStyle("-fx-padding: 20; -fx-border-style: solid; "
                + "-fx-border-width: 1; -fx-border-radius: 5; "
                + "-fx-border-color: gray;");
        mainVBox.setAlignment(Pos.CENTER);

        VBox leftVBox = new VBox(30);
        VBox rightVBox = new VBox(20);
        leftVBox.setAlignment(Pos.CENTER_RIGHT);
        rightVBox.setAlignment(Pos.CENTER_LEFT);
        mainHBox.getChildren().addAll(leftVBox, rightVBox);

        BorderPane root = new BorderPane();
        root.setCenter(mainVBox);

        // Create title of page, add it to root
        Label title = new Label("Get Started");
        title.setStyle("-fx-font-weight: bold"); // customize as you wish
        title.setFont(new Font("Arial", 24));
        title.setAlignment(Pos.CENTER);

        // Create text field to enter name
        Label nameLabel = new Label("Name: ");
        leftVBox.getChildren().add(nameLabel); // add "Name: " to leftVBox
        TextField enterName = new TextField();
        enterName.setId("enterName"); // for JoyceTests in M2
        rightVBox.getChildren().add(enterName); // add name text field to rightVBox

        // Create drop down box to select difficulty
        Label difficultyLabel = new Label("Difficulty: ");
        leftVBox.getChildren().add(difficultyLabel);
        ObservableList<String> difficultyLevels =
                FXCollections.observableArrayList(
                        "Easy",
                        "Medium",
                        "Hard"
                );
        ComboBox difficultyComboBox = new ComboBox(difficultyLevels);
        difficultyComboBox.setId("difficultyComboBox"); // for ExtraInitialConfigTests in M2
        difficultyComboBox.getSelectionModel().selectFirst(); // default to easy
        rightVBox.getChildren().add(difficultyComboBox);

        // Create drop down box to select starting seed type
        Label seedLabel = new Label("Starting Seed Type: ");
        leftVBox.getChildren().add(seedLabel);
        ObservableList<String> seedTypes =
                FXCollections.observableArrayList(
                        "Wheat",
                        "Apple",
                        "Corn"
                );
        ComboBox seedComboBox = new ComboBox(seedTypes);
        seedComboBox.setId("seedComboBox"); // for ExtraInitialConfigTests in M2
        seedComboBox.getSelectionModel().selectFirst(); // default to wheat
        rightVBox.getChildren().add(seedComboBox);

        // Create drop down box to select season
        Label seasonLabel = new Label("Starting Season: ");
        leftVBox.getChildren().add(seasonLabel);
        ObservableList<String> seasonList =
                FXCollections.observableArrayList(
                        "Spring",
                        "Summer",
                        "Fall",
                        "Winter"
                );
        ComboBox seasonComboBox = new ComboBox(seasonList);
        seasonComboBox.setId("seasonComboBox"); // for ExtraInitialConfigTests in M2
        seasonComboBox.getSelectionModel().selectFirst(); // default to spring
        rightVBox.getChildren().add(seasonComboBox);

        // Create submit button
        Button submitButton = new Button("Submit");
        submitButton.setAlignment(Pos.CENTER);
        submitButton.setId("submitButton"); // for JoyceTests in M2

        // Add all elements to main VBox
        mainVBox.getChildren().addAll(title, mainHBox, submitButton);

        // When submit button clicked
        submitButton.setOnAction(e ->  {

            int difficultyLevel;
            Season s;
            String name;

            if (difficultyComboBox.getValue() == "Hard") {
                difficultyLevel = 3;
            } else if (difficultyComboBox.getValue() == "Medium") {
                difficultyLevel = 2;
            } else {
                difficultyLevel = 1; // default to 1 if nothing selected
            }
            if (seasonComboBox.getValue() == "Winter") {
                s = Season.WINTER;
            } else if (seasonComboBox.getValue() == "Summer") {
                s = Season.SUMMER;
            } else if (seasonComboBox.getValue() == "Fall") {
                s = Season.FALL;
            } else {
                s = Season.SPRING; // default to spring if nothing selected
            }
            if (enterName.getText() == null || enterName.getText().trim().isEmpty()) {
                Alert invalidNameAlert = new Alert(Alert.AlertType.ERROR);
                invalidNameAlert.
                        setContentText("No name has been picked. You are Player 1!");
                invalidNameAlert.
                        showAndWait(); /* alert pop-up telling user that
                        they have been defaulted to Player 1*/
                name = "Player 1";
                enterName.insertText(0, "Player 1");
            } else {
                name = enterName.getText().trim();
            }

            new GameState(difficultyLevel, s);
            new Player(name, difficultyLevel, (String) seedComboBox.getValue());

            (new FarmUI()).start(primaryStage);

        });

        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}