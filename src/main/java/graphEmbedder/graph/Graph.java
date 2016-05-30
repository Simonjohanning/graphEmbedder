package graphEmbedder.graph;

import java.util.HashSet;
import java.util.Set;
import java.util.Collection;
import graphEmbedder.graph.Node;
import graphEmbedder.graph.Edge;
import graphEmbedder.graph.Component;

public class Graph{
    HashSet<Node> nodes;
    HashSet<Edge> edges;
    HashSet<Component> components;

    public Graph(HashSet<Node> nodes, HashSet<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
        initializeComponents();
    }

    public Graph(Collection<Node> nodes, HashSet<Edge> edges) {
        HashSet<Node> nodeSet = new HashSet<>();
        for(Node currentNode : nodes){
            nodeSet.add(currentNode);
        }
        this.nodes = nodeSet;
        this.edges = edges;
        initializeComponents();
    }

    public Graph(Collection<Node> nodes, HashSet<Edge> edges, HashSet<Component> components) {
        HashSet<Node> nodeSet = new HashSet<>();
        for(Node currentNode : nodes){
            nodeSet.add(currentNode);
        }
        this.nodes = nodeSet;
        this.edges = edges;
        this.components = components;
    }

    public HashSet<Node> getNodes() {

        return nodes;
    }

    public HashSet<Edge> getEdges() {
        return edges;
    }

    public HashSet<Component> getComponents() {
        return components;
    }

    public void addEdge(Edge edgeToAdd) throws IllegalArgumentException{
        if(!nodes.contains(edgeToAdd.getSource()))throw new IllegalArgumentException ("Source node of the edge "+edgeToAdd.getSource().getId()+" is not in the graph yet. Edge is rejected!! Consider adding the node!!");
        else if(!nodes.contains(edgeToAdd.getTarget())) throw new IllegalArgumentException ("Target node of the edge "+edgeToAdd.getTarget().getId()+" is not in the graph yet. Edge is rejected!! Consider adding the node!!");
        if(edgeToAdd.getSource().getAssociatedComponent() != edgeToAdd.getTarget().getAssociatedComponent()){
            Component newComponent = GraphHelper.unifyComponents(edgeToAdd.getSource().getAssociatedComponent(), edgeToAdd.getTarget().getAssociatedComponent());
            components.remove(edgeToAdd.getSource().getAssociatedComponent());
            components.remove(edgeToAdd.getTarget().getAssociatedComponent());
            edgeToAdd.getSource().setAssociatedComponent(newComponent);
            edgeToAdd.getTarget().setAssociatedComponent(newComponent);
            components.add(newComponent);
        }
        edges.add(edgeToAdd);
    }

    private void initializeComponents(){
        components = new HashSet<Component>(nodes.size());
        //initialize components for empty graph
        for(Node node : nodes){
            HashSet<Node> singleNodeComponentNodeSet = new HashSet<>(1);
            singleNodeComponentNodeSet.add(node);
            Component singleNodeComponent = new Component(singleNodeComponentNodeSet);
            components.add(singleNodeComponent);
            node.setAssociatedComponent(singleNodeComponent);
        }
        //connect components via edge
        for(Edge edge : edges){
            if(edge.getSource().getAssociatedComponent() != edge.getTarget().getAssociatedComponent()){
                Component newComponent = GraphHelper.unifyComponents(edge.getSource().getAssociatedComponent(), edge.getTarget().getAssociatedComponent());
                components.remove(edge.getSource().getAssociatedComponent());
                components.remove(edge.getTarget().getAssociatedComponent());
                edge.getSource().setAssociatedComponent(newComponent);
                edge.getTarget().setAssociatedComponent(newComponent);
                components.add(newComponent);
            }
        }
    }

}