package graphEmbedder.helper;

import graphEmbedder.graph.*;
import graphEmbedder.graph.GraphHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by sim on 25.07.16.
 */
public class GraphConverter {

    public static Graph pubchemGraphToSimonGraph(List<SdfNode> pubchemNodes, List<SdfEdge> pubchemEdges){
        HashSet<Node> nodes = new HashSet<>(pubchemNodes.size());
        HashMap<Integer, Node> nodesById = new HashMap<>(pubchemNodes.size());
        HashSet<Edge> edges = new HashSet<>(pubchemEdges.size());
        for(SdfNode currentNode : pubchemNodes){
            Node nodeToAdd = new Node(currentNode.getAtom(),currentNode.getNodeID(), null);
            nodes.add(nodeToAdd);
            nodesById.put(nodeToAdd.getId(), nodeToAdd);
        }
        for(SdfEdge currentEdge : pubchemEdges){
            String label = null;
            switch(currentEdge.getNumBonds()){
                case 1:
                    label = "-";
                    break;
                case 2:
                    label = "=";
                    break;
                case 3:
                    label = "#";
                    break;
                case 4:
                    label = "$";
                    break;
                default:
                    System.out.println("WARNING!! No label defined for numBonds "+currentEdge.getNumBonds()+"!!! Will be set to null!!!");
            }
            edges.add(new Edge(label, nodesById.get(currentEdge.getSourceID()), nodesById.get(currentEdge.getTargetID())));
        }
        return new Graph(nodes, edges);
    }

    public static Graph mariusGraphToSimonGraph(List<myNode> nodes, List<myEdge> edges, List<myComponent> components){
        HashMap<Integer, Node> nodeMap = new HashMap<>(nodes.size());
        HashSet<Edge> edgeSet = new HashSet<>(edges.size());
        HashMap<myNode, Node> associatedMyNodes = new HashMap<>(nodes.size());
        HashSet<Component> newComponents = new HashSet<>(nodes.size());
        for(myNode node : nodes){
            Node currentNode = new Node(node.toString(), node.getX()*nodes.size()+node.getY(), null);
            HashSet singleNodeList = new HashSet<>(1);
            Component nodeComponent = new Component(singleNodeList);
            newComponents.add(nodeComponent);
            currentNode.setAssociatedComponent(nodeComponent);
            nodeMap.put(currentNode.getId(), currentNode);
            associatedMyNodes.put(node, currentNode);
        }
        for(myEdge edge : edges){
            Edge currentEdge = new Edge(edge.toString(), associatedMyNodes.get(edge.getA()), associatedMyNodes.get(edge.getB()));
            edgeSet.add(currentEdge);
            if(currentEdge.getSource().getAssociatedComponent() != currentEdge.getTarget().getAssociatedComponent()){
                Component unifiedComponent = GraphHelper.unifyComponents(currentEdge.getSource().getAssociatedComponent(), currentEdge.getTarget().getAssociatedComponent());
                newComponents.remove(currentEdge.getSource().getAssociatedComponent());
                newComponents.remove(currentEdge.getTarget().getAssociatedComponent());
                newComponents.add(unifiedComponent);
                nodeMap.get(currentEdge.getSource().getId()).setAssociatedComponent(unifiedComponent);
                nodeMap.get(currentEdge.getTarget().getId()).setAssociatedComponent(unifiedComponent);
            }
        }
        return new Graph(nodeMap.values(), edgeSet, newComponents);
    }
}
