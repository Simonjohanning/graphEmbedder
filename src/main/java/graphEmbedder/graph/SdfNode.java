package graphEmbedder.graph;

import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sim on 25.07.16.
 */
public class SdfNode {

    //coordinates of atom in Angström
    private Point3D coordinates;
    //(atomic) label of node
    private String atom;
    //all the other stuff in sdf
    private List<Integer> otherArgs;
    //nodeID in order to get the bonds right
    private int nodeID;

    public SdfNode(double x, double y, double z, String atom, int nodeID, Integer... leftoverArgs){
        this.coordinates = new Point3D(x,y,z);
        this.atom = atom;
        this.nodeID = nodeID;
        otherArgs = new ArrayList<Integer>();
        for(Integer arg : leftoverArgs){
            otherArgs.add(arg);
        }
    }

    public List<Integer> getOtherArgs() {
        return otherArgs;
    }

    public Point3D getCoordinates() {
        return coordinates;
    }

    public String getAtom() {
        return atom;
    }

    public int getNodeID() {
        return nodeID;
    }

}
