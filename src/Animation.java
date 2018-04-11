import javafx.scene.image.Image;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;


public class Animation extends VideoOutput {
    private CascadeClassifier faceCascade = new CascadeClassifier();
    private int faceSize = 0;
    private Mat frame = new Mat(), gray = new Mat();
    private MatOfRect faces = new MatOfRect();

    public Animation(){
        faceCascade.load("faceDetector.xml");
    }

    public Image animate(VideoCapture capture) {
        return createImage(detect(grabFrame(capture)));
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

    private Mat detect(Mat frame) {
        int height = frame.rows();
        Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(gray, gray);
        if (faceSize == 0)
            if (Math.round(height * 0.2) > 0)
                faceSize = (int) Math.round(height * 0.2);
        faceCascade.detectMultiScale(gray, faces, 1.1, 2, Objdetect.CASCADE_SCALE_IMAGE, new Size(faceSize, faceSize), new Size());
        Rect[] faceArray = faces.toArray();
        for (Rect face : faceArray) {
            Imgproc.rectangle(frame, face.tl(), face.br(), new Scalar(255, 0, 0), 1);
        }
        return frame;
    }
}
