import javafx.scene.image.Image;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.videoio.VideoCapture;


public class Animation extends VideoOutput {
    private Mat frame = new Mat();

    public Image animate(VideoCapture capture) {
        return createImage(duplicate(grabFrame(capture)));
    }

    public Mat grabFrame(VideoCapture capture) {
        if (capture.isOpened())
            capture.read(frame);
        else
            System.out.println("No camera!");
        if (!frame.empty()) {
            return frame;
        } else {
            System.out.println("Oh no! The frame is empty. Sending blank.");
            return new Mat(1920, 1080, CvType.CV_8U, new Scalar(128, 128, 128));
        }
    }
}
