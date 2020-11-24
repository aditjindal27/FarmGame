import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class WelcomeScreen extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {

        // Create main layout pane (root), create VBox and add it to root
        VBox vbox = new VBox(20);
        StackPane root = new StackPane(vbox);
        vbox.setAlignment(Pos.CENTER);

        // Create welcome message
        Label welcomeMessage = new Label("Welcome to the farm!");
        welcomeMessage.setStyle("-fx-font-weight: bold"); // customize as you wish
        welcomeMessage.setFont(new Font("Arial", 24)); // customize as you wish
        welcomeMessage.setAlignment(Pos.CENTER);

        // Create start button
        Button startButton = new Button("Get started");
        startButton.setAlignment(Pos.CENTER);

        // Add welcome message and start button to VBox
        vbox.getChildren().addAll(welcomeMessage, startButton);
        startButton.setOnAction(e -> {
            (new InitialConfigurationScreen()).start(primaryStage);
        });

        // Add root to scene
        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
