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
    HashSet<Components> components;

    public Graph(Set<Node> nodes, Set<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
        initializeComponents();
    }

    public HashSet<Node> getNodes() {

        return nodes;
    }

    public HashSet<Edge> getEdges() {
        return edges;
    }

    public HashSet<Components> getComponents() {
        return components;
    }

    public void addEdge(Edge edgeToAdd) throws IllegalArgumentException{
        if(!nodes.contains(edgeToAdd.getSource()))throw new IllegalArgumentException ("Source node of the edge "+edgeToAdd.getSource().getId()+" is not in the graph yet. Edge is rejected!! Consider adding the node!!");
        else if(!nodes.contains(edgeToAdd.getTarget())) throw new IllegalArgumentException ("Target node of the edge "+edgeToAdd.getTarget().getId()+" is not in the graph yet. Edge is rejected!! Consider adding the node!!");
        if(edgeToAdd.getSource().getComponent() != edgeToAdd.getTarget().getComponent()){
            HashSet<Node> joinedSet = new HashSet<Node>();
            joinedSet.addAll(edge.getSource().getAssociatedComponent());
            joinedSet.addAll(edge.getTarget().getAssociatedComponent());
            Component newComponent = new Component(joinedSet);
            components.remove(edge.getSource().getAssociatedComponent());
            components.remove(edge.getTarget().getAssociatedComponent());
            edge.getSource().setAssociatedComponent(newComponent);
            edge.getTarget().setAssociatedComponent(newComponent);
            components.add(newComponent);
        }
        edges.add(edgeToAdd);
    }

    private void initializeComponents(){
        components = new HashSet<Node>(nodes.size);
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
                HashSet<Node> joinedSet = new HashSet<Node>();
                joinedSet.addAll(edge.getSource().getAssociatedComponent());
                joinedSet.addAll(edge.getTarget().getAssociatedComponent());
                Component newComponent = new Component(joinedSet);
                components.remove(edge.getSource().getAssociatedComponent());
                components.remove(edge.getTarget().getAssociatedComponent());
                edge.getSource().setAssociatedComponent(newComponent);
                edge.getTarget().setAssociatedComponent(newComponent);
                components.add(newComponent);
            }
        }
    }

}