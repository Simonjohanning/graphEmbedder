package graphEmbedder.helper;

import graphEmbedder.graph.SdfEdge;
import graphEmbedder.graph.SdfNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by sim on 25.07.16.
 */
public class SdfParseHelper {

    private static Logger importLogger = LogManager.getLogger("importLogger");

    public static SdfNode parseNode(String line, int nodeID) {
        double x = 0.0,y = 0.0,z = 0.0;
        String label = "";
        //TODO: optionally put in other arguments as well

        String[] lineContent = line.split(" ");
        //specifies the position within the line of the non-empty entries (non-whitespace)
        int inLinePosition = 0;
        for (String content : lineContent) {
            if (content.length() > 0) {
                importLogger.debug("In node row: {} with {} entries", line, lineContent.length);
                switch (inLinePosition) {
                    //first one is x-coordinate
                    case 0:
                        x = Double.valueOf(content);
                        break;
                    //second one is y-coordinate
                    case 1:
                        y = Double.valueOf(content);
                        break;
                    //third one is z-coordinate
                    case 2:
                        z = Double.valueOf(content);
                        break;
                    //fourth one is label
                    case 3:
                        label = content;
                        break;
                    //default: no one cares
                    default:
                }
            inLinePosition++;
            }

        }
        importLogger.info("Parse node {} of type {} with coordinates ({},{},{}).",nodeID,label,x,y,z);
        return new SdfNode(x,y,z,label, nodeID);
    }

    public static SdfEdge parseEdge(String line) {
        int sourceID=0,targetID=0,numBonds=0;
        //TODO: optionally put in other arguments as well

        String[] lineContent = line.split(" ");
        //specifies the position within the line of the non-empty entries (non-whitespace)
        int inLinePosition = 0;
        for (String content : lineContent) {
            if (content.length() > 0) {
                importLogger.debug("In edge row: {} with {} entries", line, lineContent.length);
                switch (inLinePosition) {
                    //first one is sourceID
                    case 0:
                        sourceID = Integer.valueOf(content);
                        break;
                    //second one is targetID
                    case 1:
                        targetID = Integer.valueOf(content);
                        break;
                    //third one is bondType
                    case 2:
                        numBonds = Integer.valueOf(content);
                        break;
                    //default: no one cares
                    default:
                }
            inLinePosition++;
            }
        }
        importLogger.info("Parse edge ({},{}) with {} bonds.",sourceID,targetID,numBonds);
        return new SdfEdge(sourceID, targetID, numBonds);
    }
}
