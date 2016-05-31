package graphEmbedder.helper;

import graphEmbedder.graph.Edge;
import graphEmbedder.graph.Graph;
import graphEmbedder.graph.Node;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by sim on 31.05.16.
 */
public class GraphHelper {

    public static int numberNeighbours(Graph graph, Node node){
        HashSet<Node> neighbourNodes = new HashSet<>();
        for(Edge currentEdge : graph.getEdges()){
            if(currentEdge.getSource() == node){
                if(!neighbourNodes.contains(currentEdge.getTarget())) neighbourNodes.add(currentEdge.getTarget());
            }
        }
        return neighbourNodes.size();
    }

    public static HashSet<Node> getNeighbours(Graph graph, Node node){
        HashSet<Node> neighbourNodes = new HashSet<>();
        for(Edge currentEdge : graph.getEdges()){
            if(currentEdge.getSource() == node){
                if(!neighbourNodes.contains(currentEdge.getTarget())) neighbourNodes.add(currentEdge.getTarget());
            }
        }
        return neighbourNodes;
    }

    public static ArrayList<Node> getNeighboursAsArrayList(Graph graph, Node node){
        HashSet<Node> neighbourNodes = new HashSet<>();
        for(Edge currentEdge : graph.getEdges()){
            if(currentEdge.getSource() == node){
                if(!neighbourNodes.contains(currentEdge.getTarget())) neighbourNodes.add(currentEdge.getTarget());
            }
        }
        ArrayList<Node> neighboursAsAL = new ArrayList<>(neighbourNodes.size());
        neighboursAsAL.addAll(neighbourNodes);
        return neighboursAsAL;
    }
}
