import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        ExperimentController controller = new ExperimentController(primaryStage);
        controller.startExperiment();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

