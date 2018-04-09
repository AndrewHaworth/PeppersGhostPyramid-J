import javafx.scene.image.Image;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

/**
 * Created by Andrew Haworth.
 */
public class Camera extends VideoOutput {
    private Mat gray = new Mat(), thresholdImg = new Mat(), hsv = new Mat();

    public Image standard(VideoCapture capture) {
        return createImage(duplicate(removeBackground(grabFrame(capture))));
    }

    private Mat removeBackground(Mat frame) {
        hsv.create(frame.size(), CvType.CV_8U);
        Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.adaptiveThreshold(gray, thresholdImg, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV,
                frame.height() > frame.width() ? (frame.height() * 2) + 1 : (frame.width() * 2) + 1, 17);
        Mat foreground = new Mat(frame.size(), CvType.CV_8UC3, new Scalar(0, 0, 0));
        frame.copyTo(foreground, thresholdImg);
        return foreground;
    }
}
