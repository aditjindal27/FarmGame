import javafx.scene.image.Image;
import javafx.scene.text.Text;

public class WinScreen extends EndScreen {

    public WinScreen() {
        super();
        super.message = new Text("You won the game!");
        super.image = new Image("https://previews.123rf.com/images/memoangeles/memoangeles1910/memoangeles191000018/132739542-cartoon-happy-farmer-girl-jumping-up-clip-art-vector-illustration-with-simple-gradients-all-in-a-sin.jpg");
    }
}
