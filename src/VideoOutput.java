import javafx.scene.image.Image;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import java.io.ByteArrayInputStream;

/**
 * Created by Andrew Haworth.
 */
public class VideoOutput {
    private Mat res = new Mat(), frame90 = new Mat(), frame180 = new Mat(), frame270 = new Mat(), frame = new Mat();
    private MatOfByte buffer = new MatOfByte();

    public Mat duplicate(Mat frame) {
        res.create(new Size(frame.width() * 3, frame.height() * 3), frame.type());
        Core.rotate(frame, frame90, Core.ROTATE_90_CLOCKWISE);
        Core.rotate(frame, frame180, Core.ROTATE_180);
        Core.rotate(frame, frame270, Core.ROTATE_90_COUNTERCLOCKWISE);

        //Normal
        frame.copyTo(res
                .rowRange(0, frame.rows())
                .colRange(frame.cols(), frame.cols() * 2));

        //90 Degrees on the right
        frame90.copyTo(res
                .rowRange(frame90.rows(), frame90.rows() * 2)
                .colRange(frame90.cols() * 2, frame90.cols() * 3));

        //180 degrees on the bottom
        frame180.copyTo(res
                .rowRange(frame180.rows() * 2, frame180.rows() * 3)
                .colRange(frame180.cols(), frame180.cols() * 2));

        //270 degrees on the left
        frame270.copyTo(res.
                rowRange(frame270.rows(), frame270.rows() * 2)
                .colRange(0, frame270.cols()));

        return res;
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

    public Image createImage(Mat frame) {
        Imgcodecs.imencode(".bmp", frame, buffer);
        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }
}
