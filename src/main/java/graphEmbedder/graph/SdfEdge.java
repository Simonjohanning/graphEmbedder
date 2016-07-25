package graphEmbedder.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sim on 25.07.16.
 */
public class SdfEdge {

    private int sourceID;
    private int targetID;
    private int numBonds;
    List<Integer> otherArgs;

    public SdfEdge(int sourceID, int targetID, int numBonds, Integer... leftoverArgs){
        this.sourceID = sourceID;
        this.targetID = targetID;
        this.numBonds = numBonds;
        otherArgs = new ArrayList<>();
        for(Integer arg : leftoverArgs){
            otherArgs.add(arg);
        }

    }

    public int getSourceID() {
        return sourceID;
    }

    public int getTargetID() {
        return targetID;
    }

    public int getNumBonds() {
        return numBonds;
    }

    public List<Integer> getOtherArgs() {
        return otherArgs;
    }
}
