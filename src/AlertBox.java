import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class AlertBox {
    public static void alert(String title, String message) {
        Stage window = new Stage();
        VBox layout = new VBox(10);
        Button close = new Button("Ok");
        Scene alert = new Scene(layout, 300, 100);
        Label label = new Label(message);

        window.setTitle(title);
        layout.getChildren().addAll(label, close);
        layout.setAlignment(Pos.CENTER);
        window.initModality(Modality.APPLICATION_MODAL);
        close.setOnAction(e -> window.close());
        window.setScene(alert);
        window.showAndWait();
    }
}
