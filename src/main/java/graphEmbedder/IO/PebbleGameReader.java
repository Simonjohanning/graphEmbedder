package graphEmbedder.IO;

import java.util.*;
import java.io.*;

import graphEmbedder.configuration.GraphConfiguration;
import graphEmbedder.graph.*;

public class PebbleGameReader{

    public static Graph readPebbleGameInformation(GraphConfiguration graphConfiguration) throws Exception{
        switch (graphConfiguration.getType()){
            case "mariusGraph":
                return readMariusGraph(graphConfiguration.getFilePath());
            default:
                throw new Exception("This case has not been implemented!!");
        }
    }

    private static Graph readMariusGraph(String filePath){

        System.out.println("filePath is "+filePath);

        // Listen von Knoten und Kanten
        List<myNode> nodes = new ArrayList<myNode>();
        List<myEdge> edges = new ArrayList<myEdge>();
        List<myComponent> components = new ArrayList<myComponent>();

        try{
            // FileReader
            FileReader inputFile = new FileReader(filePath);

            // BufferedReader
            BufferedReader bufferReader = new BufferedReader(inputFile);

            // Variable, die jeweils Zeile enthält
            String line;

            // Datei Zeile für Zeile lesen und Knoten/Kanten speichern
            while ((line = bufferReader.readLine()) != null)   {
                // Überprüfen, ob Knoteninformation folgt
                if (line.equals("n")){
                    // ggf.	verarbeiten / speichern
                    try{
                        line = bufferReader.readLine();
                        int xc = Integer.parseInt(line);
                        line = bufferReader.readLine();
                        int yc = Integer.parseInt(line);
                        myNode node1 = new myNode(xc,yc);
                        nodes.add(node1);
                    }
                    catch(Exception e){
                        System.out.println("File not valid! " + e.getMessage());
                        System.exit(0);
                    }
                }
                // Überprüfen, ob Kanteninformation folgt
                if (line.equals("e")){
                    // ggf.	verarbeiten / speichern
                    try{
                        line = bufferReader.readLine();
                        int xc = Integer.parseInt(line);
                        line = bufferReader.readLine();
                        int yc = Integer.parseInt(line);
                        myEdge edge1 = new myEdge(nodes.get(xc),nodes.get(yc));
                        edges.add(edge1);
                    }
                    catch(Exception e){
                        System.out.println("File not valid! " + e.getMessage());
                        System.exit(0);
                    }
                }
            }
            // bufferReader schließen
            bufferReader.close();
        }catch(Exception e){
            System.out.println("Error while reading file line by line:" + e.getMessage());
        }

        return mariusGraphToSimonGraph(nodes, edges, components);
    }

    private static Graph mariusGraphToSimonGraph(List<myNode> nodes, List<myEdge> edges, List<myComponent> components){
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

