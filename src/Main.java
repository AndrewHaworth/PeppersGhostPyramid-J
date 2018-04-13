import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.opencv.core.Core;
import org.opencv.videoio.VideoCapture;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main extends Application {

    @FXML
    public BorderPane anchor;
    @FXML
    private ChoiceBox<String> choiceBox;
    @FXML
    public ImageView mainFrame;
    @FXML
    private Button exitButton;

    private VideoCapture videoCapture = new VideoCapture();
    private Camera camera = new Camera();
    private Animation animate = new Animation();

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }

    @Override
    public void start(Stage window) {
        window.setTitle("Pepper's Ghost Pyramid");
        try {
            Scene scene = new Scene(new FXMLLoader(getClass().getResource("Markup.fxml")).load());
            scene.getStylesheets().add("Design.css");
            window.setScene(scene);
            window.setMaximized(true);
            window.initStyle(StageStyle.UNDECORATED);
            window.show();

        } catch (IOException e) {
            AlertBox.alert("Error", "Whoops! Something went wrong!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    @FXML
    public void buttonPress(ActionEvent actionEvent) {
        if (!videoCapture.isOpened())
            videoCapture.open(0);
        Runnable thread;
        new Thread(thread = () -> {
            String choice = choiceBox.getSelectionModel().getSelectedItem();
            if (choice.equals("Standard")) {
                camera.standard(videoCapture, mainFrame, anchor);
            } else if (choice.equals("Animation")) {
                animate.standard(videoCapture, mainFrame, anchor);
            } else if (actionEvent.getSource() == exitButton) {
                exit();
            } else
                AlertBox.alert("Warning!", "For some reason there is an error with the button press?");
        }).start();
        ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(thread, 0, 33, TimeUnit.MILLISECONDS);
    }

    @FXML
    private void exit() {
        AlertBox.alert("Bye!", "Thanks for using Pepper's Ghost Pyramid!");
        this.videoCapture.release();
        System.exit(0);
    }
}
