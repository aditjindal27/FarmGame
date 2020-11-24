import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public abstract class EndScreen extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    protected Text message;
    protected Image image;

    public void start(Stage primaryStage) {

        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);

        Text endGameMessage = message;
        endGameMessage.setFont(new Font("Arial Bold", 36));
        endGameMessage.setId("endGameMessage");

        Image endGameImage = image;
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(300);

        Button restartButton = new Button("Restart Game");
        restartButton.setOnAction(e -> {
            GameState.resetDay();
            (new WelcomeScreen()).start(primaryStage);
        });

        root.getChildren().addAll(message, imageView, restartButton);

        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
