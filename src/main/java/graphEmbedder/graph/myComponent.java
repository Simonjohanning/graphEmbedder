package graphEmbedder.graph;

import myNode;
import myEdge;
import java.util.List;

public class myComponent {
    // Knoten und Kanten der Komponenten
    List<myNode> cNodes = new ArrayList<myNode>();
    List<myEdge> cEdges = new ArrayList<myEdge>();

    // Constructor, dem die Knoten + Kante übergeben werden
    public myComponent(myNode a, myNode b, myEdge c) {
        cNodes.add(a);
        cNodes.add(b);
        cEdges.add(c);
        Collection
    }

    public void merge(myComponent d) {
        cNodes.addAll(d.giveNodes());
        cEdges.addAll(d.giveEdges());
    }

    public void addNode(myNode a) {
        cNodes.add(a);
    }

    public void addEdge(myEdge a) {
        cEdges.add(a);
    }

    public List<myNode> giveNodes() {
        return cNodes;
    }

    public boolean contNode(myNode a) {
        if (cNodes.contains(a)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean contEdge(myEdge a) {
        if (cEdges.contains(a)) {
            return true;
        } else {
            return false;
        }
    }

    public List<myEdge> giveEdges() {
        return cEdges;
    }
}