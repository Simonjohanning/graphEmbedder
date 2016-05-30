package graphEmbedder.graph;

import java.util.List;

public class myEdge
{
    // Knoten, die durch die Kante verbunden sind
    myNode a;
    myNode b;

    public myNode getA() {
        return a;
    }

    public myNode getB() {
        return b;
    }

    // Main-Methode diente nur einem simplen Test...
    public static void main (String[] args)
    {
        myNode c = new myNode (1, 2);
        myNode d = new myNode (2, 2);
        myEdge edge = new myEdge (c, d);
        System.out.println (edge);
    }

    // Constructor, dem die Knoten übergeben werden
    public myEdge (myNode a, myNode b) {
        this.a = a;
        this.b = b;
    }

    // Funktion, die die Koordinaten und Pebblezahl der verbundenen Knoten zurückgibt
    public String toString () {
        return a + " ; " + b;
    }

    // Funktion, die den "ersten" Knoten zurückgibt
    public myNode myNode1 () {
        return a;
    }

    // Funktion, die den "zweiten" Knoten zurückgibt
    public myNode myNode2 () {
        return b;
    }
}
