import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class UIUtils {
    public static void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void pulseSquare(Rectangle square) {
    	
        Color originalColor = Color.LIGHTGRAY; 
        Color darkerColor = originalColor.darker();

        square.setFill(originalColor);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(square.fillProperty(), darkerColor)),
                new KeyFrame(Duration.millis(200), new KeyValue(square.fillProperty(), originalColor))
        );

        timeline.play();
        
    }
}
