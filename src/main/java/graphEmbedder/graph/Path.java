package graphEmbedder.graph;

import java.util.ArrayList;

/**
 * Created by sim on 31.05.16.
 */
public class Path {

    private Node pathOrigin;
    private Node pathTarget;

    public Node getPathOrigin() {
        return pathOrigin;
    }

    public Node getPathTarget() {
        return pathTarget;
    }

    public ArrayList<Edge> getEdgesOnPath() {
        return edgesOnPath;
    }

    public Path(Node pathOrigin, Node pathTarget, ArrayList<Edge> edgesOnPath) {

        this.pathOrigin = pathOrigin;
        this.pathTarget = pathTarget;
        this.edgesOnPath = edgesOnPath;
    }

    private ArrayList<Edge> edgesOnPath;
}
