import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.animation.Timeline;

import java.util.List;

public class ExperimentController {
    private Stage stage;
    private List<Trial> trialList;
    private ExperimentModel model;
    private Timeline timer;
    private long lastClickTime;


    public ExperimentController(Stage stage) {
        this.stage = stage;
        this.trialList = new TrialGenerator().generateTrials();
        this.model = new ExperimentModel();
    }

    public void startExperiment() {
        model.setCurrentTrial(0);
        showStartScreen();
    }

    private void showStartScreen() {
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);

        String trialInfo = "No more trials.";
        if (model.getCurrentTrial() < trialList.size()) {
            Trial currentTrial = trialList.get(model.getCurrentTrial());
            double currentID = currentTrial.getId(); 
            trialInfo = String.format("Get ready for Trial %d of ID %.0f. Click 'Start' when ready.",
                    (model.getCurrentTrial() % 3) + 1,
                    currentID);
        }

        Text welcomeText = new Text(trialInfo);
        Button startButton = new Button("Start");
        vbox.getChildren().addAll(welcomeText, startButton);

        startButton.setOnAction(e -> {
            if (model.getCurrentTrial() < trialList.size()) {
                startTrial(trialList.get(model.getCurrentTrial()));
            } else {
                showCompletionScreen("Experiment Completed. Thank you!");
            }
        });

        Scene scene = new Scene(vbox, 300, 200);
        stage.setScene(scene);
        stage.show();
    }


    private void startTrial(Trial trial) {
	    Pane pane = new Pane();

	    Rectangle leftSquare = new Rectangle(50, 100, trial.getWidth(), trial.getWidth());
	    leftSquare.setFill(Color.LIGHTGRAY);
	    Rectangle rightSquare = new Rectangle(50 + trial.getDistance(), 100, trial.getWidth(), trial.getWidth());
	    rightSquare.setFill(Color.LIGHTGRAY);

	    Text textOne = new Text("1");
	    textOne.setX(leftSquare.getX() + leftSquare.getWidth() / 2 - textOne.getLayoutBounds().getWidth() / 2);
	    textOne.setY(leftSquare.getY() + leftSquare.getHeight() / 2 + textOne.getLayoutBounds().getHeight() / 4); 
	    textOne.setFill(Color.BLACK);
	    
	    textOne.setMouseTransparent(true);

	    Text textTwo = new Text("2");
	    textTwo.setX(rightSquare.getX() + rightSquare.getWidth() / 2 - textTwo.getLayoutBounds().getWidth() / 2);
	    textTwo.setY(rightSquare.getY() + rightSquare.getHeight() / 2 + textTwo.getLayoutBounds().getHeight() / 4);
	    textTwo.setFill(Color.BLACK);
	    
	    textTwo.setMouseTransparent(true);

	    pane.getChildren().addAll(leftSquare, rightSquare, textOne, textTwo);
	    
	    double furthestRightEdge = Math.max(leftSquare.getX() + leftSquare.getWidth(), rightSquare.getX() + rightSquare.getWidth());
	    double bottomEdge = Math.max(leftSquare.getY() + leftSquare.getHeight(), rightSquare.getY() + rightSquare.getHeight());

	    double padding = 50;
	    pane.setPrefSize(furthestRightEdge + padding, bottomEdge + padding);

        model.resetClickCount();
        Text clickCounterText = new Text("Clicks: 0");
        clickCounterText.setX(10);
        clickCounterText.setY(40);

        Text trialInfoText = new Text();
        trialInfoText.setX(10); 
        trialInfoText.setY(20); 
        updateTrialInfoText(trialInfoText);

        pane.getChildren().addAll(clickCounterText, trialInfoText);

        Scene scene = new Scene(pane);
        stage.setScene(scene);



        setupClickHandlers(leftSquare, rightSquare, clickCounterText);
    }

    private void setupClickHandlers(Rectangle leftSquare, Rectangle rightSquare, Text clickCounterText) {
        leftSquare.setOnMouseClicked(e -> {
            handleSquareClick(true, clickCounterText);
            UIUtils.pulseSquare(leftSquare);
        });
        rightSquare.setOnMouseClicked(e -> {

            handleSquareClick(false, clickCounterText);
            UIUtils.pulseSquare(rightSquare);
        });
    }


    
    private void updateTrialInfoText(Text trialInfoText) {
        
        int totalTrialsPerID = 3;
        int currentIDTrialNumber = (model.getCurrentTrial() % totalTrialsPerID) + 1;
        double currentID = trialList.get(model.getCurrentTrial()).getId();
        trialInfoText.setText(String.format("Trial: %d of ID %.0f", currentIDTrialNumber, currentID));
    }
    
    private void showClickIntervals() {
        StringBuilder sb = new StringBuilder("Click intervals (seconds):");
        for (Long interval : model.getTrialCompletionTimes()) {
            double intervalSeconds = interval / 1000.0; 
            sb.append(String.format("\n%.3f", intervalSeconds)); 
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Trial Results");
        alert.setHeaderText("Time between clicks");
        alert.setContentText(sb.toString());
        alert.showAndWait();
        
    }

    private void handleSquareClick(boolean isLeftSquare, Text clickCounterText) {
        if (model.isLastClickLeft() != isLeftSquare) {
            model.setLastClickLeft(isLeftSquare);
            long currentTime = System.currentTimeMillis();
            

            if (lastClickTime > 0) {
                
                long interval = currentTime - lastClickTime;
                
                model.getTrialCompletionTimes().add(interval);
   
                lastClickTime = 0; 
            } else {
         
                lastClickTime = currentTime;
            }

            model.incrementClickCount();
            clickCounterText.setText("Clicks: " + model.getClickCount() / 2);

            if (model.getClickCount() >= ExperimentModel.CLICKS_REQUIRED * 2) {
                if (timer != null) {
                    timer.stop();
                }

                showClickIntervals(); 
                model.getTrialCompletionTimes().clear(); 
   
                showCompletionScreen(String.format("Trial: %d of ID %.0f completed.", (model.getCurrentTrial() % 3) + 1, trialList.get(model.getCurrentTrial()).getId()));
                
                model.incrementCurrentTrial();

                if (model.getCurrentTrial() < trialList.size()) {
                    showStartScreen(); 
                } else {
                    showCompletionScreen("Experiment Completed. Thank you!"); 
                }
            }
        }
    }

    private void showCompletionScreen(String message) {
    	
    	String title = "Trial Completion";
    	
    	if (model.getCurrentTrial() >= trialList.size()) {
    		title = "Experiment Completed";
    	}
    	
        UIUtils.showAlert(title, null, message);
        if (model.getCurrentTrial() >= trialList.size()) {
            Platform.exit();
        } else {
            showStartScreen();
        }
    }
}
