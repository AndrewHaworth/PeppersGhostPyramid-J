import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.SceneAntialiasing;
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
    private CascadeClassifier faceCascade;
    private int faceSize;
    private Mat frame = new Mat(), gray = new Mat();
    private MatOfRect faces;
    private MatOfByte buffer;
    private SubScene scene;
    private Group group;
    private Sphere sphere;

    public Animation() {
        faceCascade = new CascadeClassifier("src/faceDetector.xml");
        faceSize = 0;
        faces = new MatOfRect();
        buffer = new MatOfByte();
        group = new Group();
        scene = new SubScene(group, 400, 400, false, SceneAntialiasing.BALANCED);
        sphere = new Sphere(200);
        PhongMaterial phongMaterial = new PhongMaterial(Color.ALICEBLUE);
        sphere.setMaterial(phongMaterial);
        group.getChildren().add(sphere);
    }

    public void standard(VideoCapture capture, ImageView currentFrame, BorderPane anchor) {
        createScene(capture, currentFrame, anchor);
    }

    private void createScene(VideoCapture capture, ImageView currentFrame, BorderPane anchor) {
        Platform.runLater(() -> anchor.setCenter(scene));
        currentFrame.setImage(null);
        scene.setRoot(group);

        int x = detectX(grabFrame(capture));
        int y = detectY(grabFrame(capture));
        sphere.setLayoutX(x);
        sphere.setLayoutY(y);
        System.out.println(x + " " + y);
    }

    private int detectY(Mat frame) {
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
        if (faceArray.length != 0)
            return faceArray[0].y;
        else
            return 200;
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

    private int detectX(Mat frame) {
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
        if (faceArray.length != 0)
            return faceArray[0].x;
        else
            return 200;
    }

}
