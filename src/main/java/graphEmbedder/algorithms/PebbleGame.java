package graphEmbedder.algorithms;

import graphEmbedder.IO.PebbleGameReader;
import graphEmbedder.configuration.PebbleGameConfiguration;
import graphEmbedder.graph.*;
import graphEmbedder.helper.*;
import graphEmbedder.helper.GraphHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;


public class PebbleGame{

    private static Logger initConsoleLogger = LogManager.getLogger("initConsoleLogger");
    private PebbleGameConfiguration configuration;

    public PebbleGame(int k, int l) {
        configuration = new PebbleGameConfiguration(k,l);
    }

    public PebbleGame(PebbleGameConfiguration configuration){
        this.configuration = configuration;
    }


    public Graph constructMinimallyRigidSubgraph(Graph baseGraph){
        Graph independentGraph = new Graph(baseGraph.getNodes(), new HashSet<Edge>());
        HashMap<Node, Integer> pebblesPerNode = new HashMap<>(baseGraph.getNodes().size());
        for(Node currentNode : baseGraph.getNodes()){
            pebblesPerNode.put(currentNode, configuration.getK());
        }
        for(Edge edgeToAdd : baseGraph.getEdges()){
            //only consider edges not in the same component
            if(graphEmbedder.graph.GraphHelper.getIntersectionSize(edgeToAdd.getSource().getAssociatedComponents(), edgeToAdd.getTarget().getAssociatedComponents())==0){
                //if enough pebbles present
                if(pebblesPerNode.get(edgeToAdd.getSource()) + pebblesPerNode.get(edgeToAdd.getTarget()) > configuration.getL()){
                    //if a pebble is present at the source, add the original orientation of the edge
                    if(pebblesPerNode.get(edgeToAdd.getSource()) > 0){
                        pebblesPerNode.replace(edgeToAdd.getSource(), pebblesPerNode.get(edgeToAdd.getSource()) - 1);
                        independentGraph.addEdge(edgeToAdd,pebblesPerNode);
                        initConsoleLogger.info("{},{} added",edgeToAdd.getSource().getId(), edgeToAdd.getTarget().getId());
                    //else take the pebble from target and orient edge from target to source
                    }else{
                        pebblesPerNode.replace(edgeToAdd.getTarget(), pebblesPerNode.get(edgeToAdd.getTarget()) - 1);
                        independentGraph.addEdge(new Edge(edgeToAdd.getTarget(), edgeToAdd.getSource()),pebblesPerNode);
                    }
                }
                //else pebbles need to be collected
                else{
                    int numPebblesNeeded = configuration.getL()+1-(pebblesPerNode.get(edgeToAdd.getSource()) + pebblesPerNode.get(edgeToAdd.getTarget()));
                    initConsoleLogger.info("Node {} has {} pebbles, Node {} has {} pebbles. {} pebbles needed.",edgeToAdd.getSource().getId(),pebblesPerNode.get(edgeToAdd.getSource()),edgeToAdd.getTarget().getId(),pebblesPerNode.get(edgeToAdd.getTarget()),numPebblesNeeded);
                    ArrayList<Path> pathsToAcquirePebblesFrom = findPebblePaths(independentGraph, edgeToAdd.getSource(), edgeToAdd.getTarget(), numPebblesNeeded, pebblesPerNode);
                    //if enough pebble paths could be found (otherwise null)
                    if(pathsToAcquirePebblesFrom != null) {
                        for (Path currentPath : pathsToAcquirePebblesFrom) {
                            //adjust number of pebbles
                            pebblesPerNode.replace(currentPath.getPathOrigin(), pebblesPerNode.get(currentPath.getPathOrigin()) + 1);
                            pebblesPerNode.replace(currentPath.getPathTarget(), pebblesPerNode.get(currentPath.getPathTarget()) - 1);
                            //invert edges on path
                            for (Edge currentEdgeOnPath : currentPath.getEdgesOnPath()) {
                                independentGraph.invertEdge(currentEdgeOnPath);
                            }
                            //if a pebble is present at the source, add the original orientation of the edge
                            if(pebblesPerNode.get(edgeToAdd.getSource()) > 0){
                                pebblesPerNode.replace(edgeToAdd.getSource(), pebblesPerNode.get(edgeToAdd.getSource()) - 1);
                                independentGraph.addEdge(edgeToAdd,pebblesPerNode);
                                initConsoleLogger.info("{},{} added",edgeToAdd.getSource().getId(), edgeToAdd.getTarget().getId());
                                //else take the pebble from target and orient edge from target to source
                            }else{
                                pebblesPerNode.replace(edgeToAdd.getTarget(), pebblesPerNode.get(edgeToAdd.getTarget()) - 1);
                                independentGraph.addEdge(new Edge(edgeToAdd.getTarget(), edgeToAdd.getSource()),pebblesPerNode);
                                initConsoleLogger.info("{},{} added",edgeToAdd.getSource().getId(), edgeToAdd.getTarget().getId());
                            }
                        }
                    }else{
                        //if not enough pebbles could be acquired do nothing (reject edge)
                        initConsoleLogger.info("{},{} rejected",edgeToAdd.getSource().getId(), edgeToAdd.getTarget().getId());
                    }
                }
            }//if nodes are already in the same component, do nothing (reject edge)
            //check for completeness condition
            if(independentGraph.getEdges().size() >= configuration.getK()*independentGraph.getNodes().size() - configuration.getL()) break;

        }
        return independentGraph;
    }

    private ArrayList<Path> findPebblePaths(Graph independentGraph, Node source, Node target, int numPebblesNeeded, HashMap<Node, Integer> pebblesPerNode) {
        ArrayList<Path> pathList = new ArrayList<>(numPebblesNeeded);
        //a temporary graph with edges oriented the way they would be if pebble search is successful
        Graph tmpGraph = new Graph(independentGraph.getNodes(), independentGraph.getEdges());
        HashMap<Node, Integer> tmpPebblesPerNode = new HashMap<>(pebblesPerNode.size());
        tmpPebblesPerNode.putAll(pebblesPerNode);
        //try to acquire numPebblesNeeded for source or target
        for(int pebbleIndex = 0; pebbleIndex<numPebblesNeeded; pebbleIndex++){
            //try to find a pebble for source node
            Path pathFromSource = null;
            if(pebblesPerNode.get(source)<configuration.getK()) {
                pathFromSource = findFreePebble(tmpGraph, source, tmpPebblesPerNode);
            }
            //if path exist, use this one
            if(pathFromSource != null){
                tmpPebblesPerNode.replace(pathFromSource.getPathOrigin(), tmpPebblesPerNode.get(pathFromSource.getPathOrigin()) + 1);
                tmpPebblesPerNode.replace(pathFromSource.getPathTarget(), tmpPebblesPerNode.get(pathFromSource.getPathTarget()) - 1);
                pathList.add(pathFromSource);
                for(Edge currentEdgeOnPath : pathFromSource.getEdgesOnPath()){
                    tmpGraph.invertEdge(currentEdgeOnPath);
                }
            //if this doesn't work (anymore) try to find a path for the target of the edge to be checked
            }else{
                Path pathFromTarget = null;
                if(pebblesPerNode.get(target)<configuration.getK()) {
                    pathFromTarget = findFreePebble(tmpGraph, target, tmpPebblesPerNode);
                }
                //if no path can be found for target nor source, not enough pebbles can be acquired
                if(pathFromTarget == null) return null;
                else{
                    tmpPebblesPerNode.replace(pathFromTarget.getPathOrigin(), tmpPebblesPerNode.get(pathFromTarget.getPathOrigin()) + 1);
                    tmpPebblesPerNode.replace(pathFromTarget.getPathTarget(), tmpPebblesPerNode.get(pathFromTarget.getPathTarget()) - 1);
                    pathList.add(pathFromTarget);
                    for(Edge currentEdgeOnPath : pathFromTarget.getEdgesOnPath()){
                        tmpGraph.invertEdge(currentEdgeOnPath);
                    }
                }
            }
        }
        return pathList;
    }

    //tries to find a path on the component of source in tmpgraph from source to a node with a free pebble
    private Path findFreePebble(Graph tmpGraph, Node source, HashMap<Node, Integer> tmpPebblesPerNode) {
        if (!tmpGraph.getNeighbours().get(source).isEmpty()) {
        HashSet<Node> potentialNodes = new HashSet<>();
        for (Component currentComponent : source.getAssociatedComponents()) {
            potentialNodes.addAll(currentComponent.getNodesInComponent());
        }
        HashMap<Node, Boolean> nodeMarked = new HashMap<>(potentialNodes.size());
        nodeMarked.put(source, true);
        for (Node currentNode : potentialNodes) {
            if (currentNode != source) nodeMarked.put(currentNode, false);
        }
        HashSet<Path> potentialPaths = new HashSet<>();
        for (Node initialNeighbour : tmpGraph.getNeighbours().get(source)) {
            Path initialPath = new Path(source, initialNeighbour, new ArrayList<>());
            nodeMarked.replace(initialNeighbour, true);
            potentialPaths.add(initialPath);
        }
        while (!potentialPaths.isEmpty()) {
            for (Path currentPath : potentialPaths) {
                //if ends on a node with a pebble, a path is found
                if (tmpPebblesPerNode.get(currentPath.getPathTarget()) > 0) {
                    return currentPath;
                } else {
                    //else construct path to all non-marked neighbours of current node and remove this path
                    for (Node potentialNewNode : tmpGraph.getNeighbours().get(currentPath.getPathTarget())) {
                        //if node has not been visited yet
                        if (!nodeMarked.get(potentialNewNode)) {
                            //extend the path to this node, remove former path and mark node
                            potentialPaths.add(extendPath(currentPath, potentialNewNode));
                            potentialPaths.remove(currentPath);
                            nodeMarked.replace(potentialNewNode, true);
                        }
                    }
                    potentialPaths.remove(currentPath);
                }
            }
        }
    }
        return null;
    }

    private Path extendPath(Path currentPath, Node potentialNewNode) {
        ArrayList<Edge> extendedPathEdgeList = currentPath.getEdgesOnPath();
        extendedPathEdgeList.add(new Edge(currentPath.getPathTarget(), potentialNewNode));
        return new Path(currentPath.getPathOrigin(), potentialNewNode, extendedPathEdgeList);
    }




    /*
    *
    * Marius stub
    *
    * */





    /**
     * Tests the given graph for rigidity playing the pebble game
     *
     * @return boolean to indicate whether graph is rigid (true)
     */
    /*public boolean testGraphForRigidity(Graph graph){

        Graph minimallyRigidGraph = new Graph(graph.getNodes(), new HashSet<Edge>());
        // Datei mit Knoten- & Kanteninformatione
        // Gesamtzahl der Pebbles speichern (brauchen wir das noch)?
        int pebbles = (graph.getNodes().size() * configuration.getK());
        HashMap<Node, Integer> pebblesPerNode = new HashMap<>(graph.getNodes().size());
        for(Node currentNode : graph.getNodes()){
            pebblesPerNode.put(currentNode, configuration.getK());
        }
        //Check independence for every edge by acquiring pebbles
        for(Edge currentEdge : graph.getEdges()){
            while (pebblesPerNode.get(currentEdge.getSource()) < configuration.getL()){
                if(pebblesearch(currentEdge.getSource(),currentEdge.getTarget(), minimallyRigidGraph, pebblesPerNode)){
                   System.out.println ("Success!");
                }
                else{
                    System.out.println ("Can't find enough pebbles!");
                    break;
                }
            }
            while(pebblesPerNode.get(currentEdge.getTarget()) < configuration.getL()){
                if(pebblesearch(currentEdge.getSource(),currentEdge.getTarget(), minimallyRigidGraph, pebblesPerNode)){
                   System.out.println ("Success!");
                }
                else{
                   System.out.println ("Can't find enough pebbles!");
                   break;
                }
            }
            // TODO check how exactly to remove pebbles
                if (pebblesPerNode.get(currentEdge.getSource())==configuration.getL() && pebblesPerNode.get(currentEdge.getSource())==configuration.getL()){
                    minimallyRigidGraph.addEdge(currentEdge);
                    System.out.println ("Linked from " + currentEdge.getSource() + " to " + currentEdge.getTarget() + "!");
                    pebblesPerNode.replace(currentEdge.getSource(), pebblesPerNode.get(currentEdge.getSource()) - 1);
                    pebbles--;
                }
                else {
                    System.out.println ("Edge rejected!");
                }
        }
        // Falls verbleibende Gesamtzahl der Pebbles = l, ist der Graph rigid!
        if (pebbles==configuration.getL()){
            System.out.println ("Remaining pebbles: " + pebbles + "! It's rigid!");
        }
        else{
            System.out.println ("Remaining pebbles: " + pebbles + "! It's not rigid!");
        }
        return false;
    }*/

   /* private static boolean pebblesearch(Node n, Node m, Graph currentGraph, HashMap<Node, Integer> pebblesPerNode)
    {

        // Hilfsvariable für Anzahl der Nachfolger
        ArrayList<Node> neighboursN = GraphHelper.getNeighboursAsArrayList(currentGraph, n);
        int x = neighboursN.size();
        //tempNode1: n, tempNode3: m, tempNode2: neighboursN.get(x-1);
        // So lange Nachfolger vorhanden...
        while (x > 0) {
            // ...wird überprüft, ob Nachfolger keiner der zu verbindenden Knoten ist und Pebbles hat!
            if (neighboursN.get(x-1) != m && pebblesPerNode.get(neighboursN.get(x-1))>0){
                System.out.println ("Follower " + neighboursN.get(x-1) + " of " + n + " has a pebble!");
                // Falls ja: Pebbles werden verschoben, Kanten invertiert
                pebblesPerNode.replace(neighboursN.get(x-1), pebblesPerNode.get(neighboursN.get(x-1)) - 1);
                pebblesPerNode.replace(n, pebblesPerNode.get(n) + 1);
                currentGraph.invertEdge(n, neighboursN.get(x-1));
                //true
                break;
            }
            // Ansonsten wird überprüft, ob weitere Nachfolger existieren...
            else if( > 0) {
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
        // Falls kein Nachfolger mehr vorhanden und nicht genug Pebbles gefunden wurden...*//*
        return false;
    }*/

    /*private static List<myComponent> findV (myNode v, List<myComponent> cl)
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
    }*/

}