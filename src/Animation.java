import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;

import java.io.ByteArrayInputStream;


public class Animation {
    private CascadeClassifier faceCascade = new CascadeClassifier("src/faceDetector.xml");
    private int faceSize = 0;
    private Mat frame = new Mat(), gray = new Mat();
    private MatOfRect faces = new MatOfRect();
    private MatOfByte buffer = new MatOfByte();

    public void standard(VideoCapture capture, ImageView currentFrame, BorderPane anchor) {
        SubScene sub = new SubScene(anchor.getParent(), 1, 1);
        anchor.setRight(sub);
        new Thread(() -> createScene(sub)).start();
        currentFrame.setImage(createImage(detect(grabFrame(capture))));
    }

    private void createScene(SubScene sideFrame) {
        Sphere sphere = new Sphere(200);
        PhongMaterial phongMaterial = new PhongMaterial(Color.ALICEBLUE);
        sphere.setMaterial(phongMaterial);
    }

    private Mat grabFrame(VideoCapture capture) {
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

    private Image createImage(Mat frame) {
        Imgcodecs.imencode(".bmp", frame, buffer);
        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }
}
