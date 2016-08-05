package graphEmbedder.graph;

import java.util.HashMap;
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
    HashMap<Node, HashSet<Node>> neighbours;

    public Graph(HashSet<Node> nodes, HashSet<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
        initializeComponents();
        this.neighbours = new HashMap<>(nodes.size());
        setNeighbours();
    }

    public Graph(Collection<Node> nodes, HashSet<Edge> edges) {
        HashSet<Node> nodeSet = new HashSet<>();
        for(Node currentNode : nodes){
            nodeSet.add(currentNode);
        }
        this.nodes = nodeSet;
        this.edges = edges;
        initializeComponents();
        this.neighbours = new HashMap<>(nodes.size());
        setNeighbours();
    }

    public Graph(Collection<Node> nodes, HashSet<Edge> edges, HashSet<Component> components) {
        HashSet<Node> nodeSet = new HashSet<>();
        for(Node currentNode : nodes){
            nodeSet.add(currentNode);
        }
        this.nodes = nodeSet;
        this.edges = edges;
        this.components = components;
        this.neighbours = new HashMap<>(nodes.size());
        setNeighbours();
    }

    private void setNeighbours() {
        //initialize neighbour list
        for(Node currentNode : nodes){
            HashSet<Node> emptyNeighbourSet = new HashSet<>();
            neighbours.put(currentNode, emptyNeighbourSet);
        }
        //set target of each edge as neighbour of each node
        for(Edge currentEdge : edges){
            neighbours.get(currentEdge.getSource()).add(currentEdge.getTarget());
        }
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
        if(GraphHelper.getIntersectionSize(edgeToAdd.getSource().getAssociatedComponents(), edgeToAdd.getTarget().getAssociatedComponents())==0){
            Component newComponent = GraphHelper.unifyComponents(edgeToAdd.getSource().getAssociatedComponents(), edgeToAdd.getTarget().getAssociatedComponents());
            for(Component currentComponent : edgeToAdd.getSource().getAssociatedComponents()) {
                components.remove(currentComponent);
            }
            for(Component currentComponent : edgeToAdd.getTarget().getAssociatedComponents()) {
                components.remove(currentComponent);
            }
            edgeToAdd.getSource().addAssociatedComponent(newComponent);
            edgeToAdd.getTarget().addAssociatedComponent(newComponent);
            components.add(newComponent);
        }
        edges.add(edgeToAdd);
        neighbours.get(edgeToAdd.getSource()).add(edgeToAdd.getTarget());
    }

    public void invertEdge(Edge edgeToInvert) throws IllegalArgumentException{
        if(!edges.contains(edgeToInvert)) throw new IllegalArgumentException("The edge to be inverted doesn't exist!!");
        edges.remove(edgeToInvert);
        edges.add(new Edge(edgeToInvert.getTarget(), edgeToInvert.getSource()));
        neighbours.get(edgeToInvert.getSource()).remove(edgeToInvert.getTarget());
        neighbours.get(edgeToInvert.getTarget()).add(edgeToInvert.getSource());
    }

    public void invertEdge(Node sourceNode, Node targetNode) throws IllegalArgumentException{
        boolean edgeFound = false;
        Edge foundEdge = null;
        for(Edge currentEdge : edges){
            if((currentEdge.getSource() == sourceNode) && (currentEdge.getTarget() == targetNode)){
                foundEdge = currentEdge;
                edgeFound = true;
                break;
            }
        }
        if(!edgeFound) throw new IllegalArgumentException("The edge to be inverted doesn't exist!!");
        edges.remove(foundEdge);
        edges.add(new Edge(targetNode, sourceNode));
        neighbours.get(sourceNode).remove(targetNode);
        neighbours.get(targetNode).add(sourceNode);
    }

    private void initializeComponents(){
        components = new HashSet<Component>(nodes.size());
        //initialize components for empty graph
        for(Node node : nodes){
            HashSet<Node> singleNodeComponentNodeSet = new HashSet<>(1);
            singleNodeComponentNodeSet.add(node);
            Component singleNodeComponent = new Component(singleNodeComponentNodeSet);
            components.add(singleNodeComponent);
            node.setAssociatedComponents(singleNodeComponent);
        }
        //connect components via edge
        for(Edge edge : edges){
            if(GraphHelper.getIntersectionSize(edge.getSource().getAssociatedComponents(), edge.getTarget().getAssociatedComponents())==0){
                Component newComponent = GraphHelper.unifyComponents(edge.getSource().getAssociatedComponents(), edge.getTarget().getAssociatedComponents());
                for(Component currentComponent : edge.getSource().getAssociatedComponents()) {
                    components.remove(currentComponent);
                }
                for(Component currentComponent : edge.getTarget().getAssociatedComponents()) {
                    components.remove(currentComponent);
                }
                edge.getSource().addAssociatedComponent(newComponent);
                edge.getTarget().addAssociatedComponent(newComponent);
                components.add(newComponent);
            }
        }
    }

    public HashMap<Node, HashSet<Node>> getNeighbours() {
        return neighbours;
    }
}