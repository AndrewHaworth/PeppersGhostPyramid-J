import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

public class Camera extends VideoOutput {
    private Mat gray = new Mat(), thresholdImg = new Mat(), frame = new Mat();

    public void standard(VideoCapture capture, ImageView currentFrame, BorderPane anchor) {
        Platform.runLater(() -> anchor.setCenter(currentFrame));
        currentFrame.setImage(createImage(duplicate(removeBackground(grabFrame(capture)))));
    }

    private Mat removeBackground(Mat frame) {
        Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.adaptiveThreshold(gray, thresholdImg, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV,
                frame.height() > frame.width() ? (frame.height() * 2) + 1 : (frame.width() * 2) + 1, 7);
        Mat foreground = new Mat(frame.size(), CvType.CV_8UC3, new Scalar(0, 0, 0));
        frame.copyTo(foreground, thresholdImg);
        return foreground;
    }

    public Mat grabFrame(VideoCapture capture) {
        if (capture.isOpened())
            capture.read(frame);
        else
            System.out.println("No camera!");
        if (!frame.empty()) {
            if (frame.rows() <= frame.cols())
                frame = new Mat(frame, new Rect((frame.cols() - frame.rows()) / 2, 0,
                        frame.rows(), frame.rows()));
            else
                frame = new Mat(frame, new Rect(0, (frame.rows() - frame.cols()) / 2,
                        frame.cols(), frame.cols()));
            return frame;
        } else {
            System.out.println("Oh no! The frame is empty. Sending blank.");
            return new Mat(1920, 1080, CvType.CV_8U, new Scalar(128, 128, 128));
        }
    }
}
