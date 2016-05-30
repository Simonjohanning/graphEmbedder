package graphEmbedder.graph;

import java.util.HashSet;

/**
 * Created by sim on 30.05.16.
 */
public class GraphHelper {

    public static Component unifyComponents(Component firstComponent, Component secondComponent){
        HashSet<Node> componentNodeList = new HashSet<>();
        componentNodeList.addAll(firstComponent.getNodesInComponent());
        componentNodeList.addAll(secondComponent.getNodesInComponent());
        return new Component(componentNodeList);
    }
}
