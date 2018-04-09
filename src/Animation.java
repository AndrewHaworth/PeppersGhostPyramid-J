import javafx.scene.image.Image;
import org.opencv.videoio.VideoCapture;


/**
 * Created by Andrew Haworth.
 */
public class Animation extends VideoOutput {
    public Image animate(VideoCapture capture) {
        return createImage(duplicate(grabFrame(capture)));
    }
}
