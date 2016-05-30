package graphEmbedder.IO;

import java.util.*;
import java.io.*;

import graph.Graph;
import graph.Node;
import graph.Edge;
import graph.myNode;
import graph.myEdge;
import graph.myComponent;

public class PebbleGameReader{

    public static Graph readPebbleGameInformation(String filePath){

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
                        node1 = new myNode(xc,yc);
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
                        edge1 = new myEdge(nodes.get(xc),nodes.get(yc));
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

    private Graph mariusGraphToSimonGraph(List<myNode> nodes, List<myEdge> edges, List<myComponent> components){
        HashSet<Node> nodeSet = new HashSet<>(nodes.size());
        HashSet<Node> edgeSet = new HashSet<>(edges.size());
        nodeSet.addAll(nodes);
        edgeSet.addAll(edges);
        return new Graph(nodeSet, edgeSet);
    }
}

