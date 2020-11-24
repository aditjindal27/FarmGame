import javafx.scene.image.Image;
import javafx.scene.text.Text;

public class LoseScreen extends EndScreen {

    public LoseScreen() {
        super();
        super.message = new Text("You lost the game.");
        super.image = new Image("https://thumbs.dreamstime.com/b/disappointed-cartoon-farmer-field-vector-design-disappointed-cartoon-farmer-field-vector-102546158.jpg");
    }

}
