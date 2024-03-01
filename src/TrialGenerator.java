import java.util.ArrayList;
import java.util.List;

public class TrialGenerator {
    
    public List<Trial> generateTrials() {
        List<Trial> trialList = new ArrayList<>();
        double[] ids = {2};
        double baseWidth = 25; // Set a base width for all trials

        for (double id : ids) {
            double distance = calculateDistanceForID(id, baseWidth);
            trialList.add(new Trial(baseWidth, distance, id));
            
            double increasedWidth = baseWidth * 1.5;
            trialList.add(new Trial(increasedWidth, distance, id));
            
            double newDistance = distance * 0.75;
            trialList.add(new Trial(increasedWidth, newDistance, id));
        }

        return trialList;
    }

    private double calculateDistanceForID(double id, double width) {
        return (Math.pow(2, id) - 1) * width;
    }
}
