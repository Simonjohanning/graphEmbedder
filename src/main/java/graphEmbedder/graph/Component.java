package graphEmbedder.graph;

import java.util.Set;
import java.util.Collection;
import graphEmbedder.graph.Node;

public class Component{

    private Set<Node> nodesInComponent;

    public Set<Node> getNodesInComponent() {
        return nodesInComponent;
    }

    public Component(Set<Node> nodesInComponent) {

        this.nodesInComponent = nodesInComponent;
    }

    public boolean nodeInComponent(Node nodeToCheck){
        Collection<Node> nodeCollection = nodesInComponent;
        return nodeCollection.contains(nodeToCheck);
    }
}