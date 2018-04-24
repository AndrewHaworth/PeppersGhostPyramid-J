import javafx.application.Platform;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;


public class Animation {
    private CascadeClassifier faceCascade;
    private int faceSize;
    private Mat frame = new Mat(), gray = new Mat();
    private MatOfRect faces;
    private SubScene scene;
    private Group group;
    private Box topBox;
    private Box rightBox;
    private Box bottomBox;
    private Box leftBox;
    private int currentXRotation;
    private int currentYRotation;

    public Animation() {
        currentXRotation = 0;
        currentYRotation = 0;
        faceCascade = new CascadeClassifier("src/faceDetector.xml");
        faceSize = 0;
        faces = new MatOfRect();
        group = new Group();
        scene = new SubScene(group, 800, 800, false, SceneAntialiasing.BALANCED);
        createBoxes();
    }


    public void standard(VideoCapture capture, ImageView currentFrame, BorderPane anchor) {
        Platform.runLater(() -> anchor.setCenter(scene));
        scene.heightProperty().bind(anchor.heightProperty());
        scene.widthProperty().bind(anchor.heightProperty());
        currentFrame.setImage(null);
        scene.setRoot(group);
        manipulate(detectX(grabFrame(capture)), detectY(grabFrame(capture)));
    }

    private void manipulate(int faceX, int faceY) {
        System.out.println((faceX - 200) + " " + (faceY - 100));

        double getX = (currentXRotation - faceX) / 100;
        double getY = (currentYRotation - faceY) / 100;



        topBox.getTransforms().add(new Rotate(getX, new Point3D(0, 1, 0)));
        topBox.getTransforms().add(new Rotate(getY, new Point3D(1, 0, 0)));

        rightBox.getTransforms().add(new Rotate(getY, new Point3D(0, 1, 0)));
        rightBox.getTransforms().add(new Rotate(getX * -1, new Point3D(1, 0, 0)));

        bottomBox.getTransforms().add(new Rotate(getX * -1, new Point3D(0, 1, 0)));
        bottomBox.getTransforms().add(new Rotate(getY * -1, new Point3D(1, 0, 0)));

        leftBox.getTransforms().add(new Rotate(getY * -1, new Point3D(0, 1, 0)));
        leftBox.getTransforms().add(new Rotate(getX, new Point3D(1, 0, 0)));
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
            return 100;
    }

    private void createBoxes() {
        final int SIZE = 200;
        topBox = new Box(SIZE, SIZE, SIZE);
        rightBox = new Box(SIZE, SIZE, SIZE);
        bottomBox = new Box(SIZE, SIZE, SIZE);
        leftBox = new Box(SIZE, SIZE, SIZE);
        PhongMaterial phongMaterial = new PhongMaterial(Color.RED);
        topBox.setMaterial(phongMaterial);
        rightBox.setMaterial(phongMaterial);
        bottomBox.setMaterial(phongMaterial);
        leftBox.setMaterial(phongMaterial);
        topBox.setLayoutX(550);
        topBox.setLayoutY(250);
        rightBox.setLayoutX(850);
        rightBox.setLayoutY(550);
        bottomBox.setLayoutX(550);
        bottomBox.setLayoutY(850);
        leftBox.setLayoutX(250);
        leftBox.setLayoutY(550);
        group.getChildren().add(topBox);
        group.getChildren().add(rightBox);
        group.getChildren().add(bottomBox);
        group.getChildren().add(leftBox);
    }
}
