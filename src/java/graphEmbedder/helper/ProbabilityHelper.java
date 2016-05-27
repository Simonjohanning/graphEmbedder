package irpact.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by sim on 5/6/16.
 */
public class ProbabilityHelper {

    private static final Logger debugLogger = LogManager.getLogger("debugLogger");

    public static Integer chooseItem(HashMap<Integer, Double> probabilityList) throws Exception{
        //calculate scaling factor
        double scalingFactor = 0.0;
        for(Double currentProb : probabilityList.values()){
            debugLogger.debug("currentProb is {}",currentProb);
            if(currentProb < 0) debugLogger.warn("Probability shouldn't be negative!!");
            scalingFactor += currentProb;
        }
        double randomChoice = Math.random() * scalingFactor;
        double cumulatedProbability = 0.0;
        //Check which interval of [0, scalingFactor] the generated value lies in
        for(Integer currentIndex : probabilityList.keySet()){
            cumulatedProbability += probabilityList.get(currentIndex);
            //if interval found, return found integer
            if(randomChoice <= cumulatedProbability) return currentIndex;
            else debugLogger.debug("randomChoice is {}, where cumulatedProbability is {}",randomChoice, cumulatedProbability);
        }
        //this case can't ever happen really, since in the last iteration of the for-loop randomChoice <= cumulatedProbability
        debugLogger.warn("This shouldn't happen!! {} (randomChoice) should be smaller than {} (cumulatedProbability)!! there have been {} candidate nodes", randomChoice, cumulatedProbability, probabilityList.size());
        throw new Exception("This case can't happen. Some critical error must have occurred in chooseItem");
    }

    public static String chooseItemString(HashMap<String, Double> probabilityList) throws Exception{
        //calculate scaling factor
        double scalingFactor = 0.0;
        for(Double currentProb : probabilityList.values()){
            debugLogger.debug("currentProb is {}",currentProb);
            scalingFactor += currentProb;
        }
        double randomChoice = Math.random() * scalingFactor;
        double cumulatedProbability = 0.0;
        //Check which interval of [0, scalingFactor] the generated value lies in
        for(String currentIndex : probabilityList.keySet()){
            cumulatedProbability += probabilityList.get(currentIndex);
            //if interval found, return found integer
            if(randomChoice <= cumulatedProbability) return currentIndex;
            else debugLogger.debug("randomChoice is {}, where cumulatedProbability is {}",randomChoice, cumulatedProbability);
        }
        //this case can't ever happen really, since in the last iteration of the for-loop randomChoice <= cumulatedProbability
        throw new Exception("This case can't happen. Some critical error must have occurred in chooseItem");
    }

    public static Integer chooseUniformItem(Set<Integer> items){
        ArrayList<Integer> itemList= new ArrayList<>();
        itemList.addAll(items);
        int randomIndex = (int) Math.floor(Math.random() * itemList.size());
        return itemList.get(randomIndex);
    }
}
