import java.util.ArrayList;
import java.util.List;

public class ExperimentModel {
    public static final int CLICKS_REQUIRED = 10; 
    private int currentTrial = 0; 
    private int clickCount = 0; 
    private boolean lastClickLeft = false;
    private List<Long> trialCompletionTimes = new ArrayList<>(); 

    public ExperimentModel() {
    }

    public void incrementCurrentTrial() {
        currentTrial++;
    }

    public int getCurrentTrial() {
        return currentTrial;
    }

    public void setCurrentTrial(int currentTrial) {
        this.currentTrial = currentTrial;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void resetClickCount() {
        clickCount = 0;
    }

    public void incrementClickCount() {
        clickCount++;
    }

    public boolean isLastClickLeft() {
        return lastClickLeft;
    }

    public void setLastClickLeft(boolean lastClickLeft) {
        this.lastClickLeft = lastClickLeft;
    }

    public void recordTrialCompletion(long completionTime) {
        trialCompletionTimes.add(completionTime);
    }

    public List<Long> getTrialCompletionTimes() {
        return trialCompletionTimes;
    }
}
