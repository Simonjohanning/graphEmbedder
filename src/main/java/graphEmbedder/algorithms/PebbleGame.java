package graphEmbedder.algorithms;

import graph.myNode;
import graph.myEdge;
import graph.myComponent;
import graph.Graph;
import graph.Node;
import graph.Edge;
import graph.Component;
import java.util.*;


public class PebbleGame{

    private int k;
    private int l;

    public PebbleGame(int k, int l) {
        this.k = k;
        this.l = l;
    }

    /**
     * Tests the given graph for rigidity playing the pebble game
     *
     * @return boolean to indicate whether graph is rigid (true)
     */
    public boolean testGraphForRigidity(Graph graph){

        // Datei mit Knoten- & Kanteninformationen
//		String fileName="doubleTdoubleE";
        String fileName="nodesInvalid";
//		String fileName="flexibleSquare";
//		String fileName="bigOne";

        Graph graph = PebbleGameReader.readPebbleGameInformation(fileName);
        Graph pebbleGraph = new Graph(graph.getNodes(), new HashSet<Edge>(graph.getEdges.size()));
        // Gesamtzahl der Pebbles speichern (brauchen wir das noch)?
        int pebbles = (nodes.size() * k);
        HashMap<Node, Integer> pebblesPerNode = new HashMap<>(nodes.size());
        for(Node currentNode : graph.getNodes()){
            pebblesPerNode.put(currentNode, k);
        }
        //Check independence for every edge by acquiring pebbles
        //TODO check how to be done
        for(Edge currentEdge : graph.getEdges()){
            while (pebblesPerNode.get(currentEdge.getSource()) < l){
                if(pebblesearch(node1,node2)){
                   System.out.println ("Success!");
                }
                else{
                    System.out.println ("Can't find enough pebbles!");
                    break;
                }
            }
            while(pebblesPerNode.get(currentEdge.getTarget()) < l){
                if(pebblesearch(node2,node1)){
                   System.out.println ("Success!");
                }
                else{
                   System.out.println ("Can't find enough pebbles!");
                   break;
                }
            }
            // TODO check how exactly to remove pebbles
            // TODO MODIFY THIS CODE!!
                if (node1.givePebbles()==2 && node2.givePebbles()==2){
                    node1.addFollower(node2);
                    System.out.println ("Linked from " + node1 + " to " + node2 + "!");
                    node1.removePebble();
                    pebbles--;
                }
                else {
                    System.out.println ("Edge rejected!");
                }
        }
        // Falls verbleibende Gesamtzahl der Pebbles = l, ist der Graph rigid!
        if (pebbles==l){
            System.out.println ("Remaining pebbles: " + pebbles + "! It's rigid!");
        }
        else{
            System.out.println ("Remaining pebbles: " + pebbles + "! It's not rigid!");
        }
    }

    //TODO change to some BFS / DFS
    private static boolean pebblesearch (myNode n, myNode m)
    {
        // Hilfsvariable für Anzahl der Nachfolger
        int x = n.giveNumFol();
        // So lange Nachfolger vorhanden...
        while (x > 0) {
            tempNode2 = tempNode1.giveFollower(x-1);
            // ...wird überprüft, ob Nachfolger keiner der zu verbindenden Knoten ist und Pebbles hat!
            if (tempNode2 != tempNode3 && tempNode2.givePebbles()>0){
                System.out.println ("Follower " + tempNode2 + " of " + tempNode1 + " has a pebble!");
                // Falls ja: Pebbles werden verschoben, Kanten invertiert
                tempNode2.removePebble();
                tempNode1.addPebble();
                tempNode2.addFollower(tempNode1);
                tempNode1.removeFollower(x-1);
                return true;
            }
            // Ansonsten wird überprüft, ob weitere Nachfolger existieren...
            else if (tempNode2.giveNumFol() > 0) {
                // ...und mit diesen die Pebblesearch durchgeführt.
                if(pebblesearch (tempNode2, tempNode3)){
                    tempNode2.removePebble();
                    tempNode1.addPebble();
                    tempNode2.addFollower(tempNode1);
                    tempNode1.removeFollower(x-1);
                    return true;
                }
            }
            // Reduziere x, um mit nächstem Nachfolger fortzufahren
            x--;
        }
        // Falls kein Nachfolger mehr vorhanden und nicht genug Pebbles gefunden wurden...
        return false;
    }

    private static List<myComponent> findV (myNode v, List<myComponent> cl)
    {
        List<myComponent> inputComponents = new ArrayList<myComponent>();
        List<myComponent> returnComponents = new ArrayList<myComponent>();
        inputComponents = cl;
        int x = inputComponents.size();
        while (x > 0){
            if (inputComponents.get(x-1).contNode(v)){
                returnComponents.add(inputComponents.get(x-1));
            }
            x--;
        }
        return returnComponents;
    }

}