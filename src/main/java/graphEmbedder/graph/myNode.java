package graphEmbedder.graph;

import java.util.List;

public class myNode
{
    // Koordinaten
    int x;
    int y;
    // Pebbleanzahl
    int pebbles;
    // Liste der Nachfolger (über gerichtete Kanten direkt erreichbar)
    List<myNode> followers = new ArrayList<myNode>();

    // Main-Methode diente nur einem simplen Test...
    public static void main (String[] args)	{
        myNode node = new myNode (1, 2);
        System.out.println (node);
    }

    // Constructor, bei dem die Koordinaten angegeben und die Pebblezahl auf 2 gesetzt wird
    public myNode (int x, int y) {
        this.x = x;
        this.y = y;
        this.pebbles = 2;
    }

    // Funktion, die Koordinaten und Pebbleanzahl als String zurückgibt
    public String toString () {
        return "(" + x + "," + y + ") : " + pebbles;
    }

    // Funktion, die Pebbleanzahl als Integer zurückgibt
    public int givePebbles () {
        return pebbles;
    }

    // Funktion, die Liste der Nachfolger zurückgibt
    public List<myNode> giveFollowers () {
        return followers;
    }

    // Funktion, die durch i bestimmten Nachfolger zurückgibt
    public myNode giveFollower (int i) {
        return followers.get(i);
    }

    // Funktion, die Anzahl der Nachfolger zurückgibt
    public int giveNumFol () {
        return followers.size();
    }

    // Methode, um Nachfolger b anzuhängen
    public void addFollower (myNode b) {
        followers.add(b);
    }

    // Methode, um Nachfolger an Position i zu entfernen
    public void removeFollower (int i) {
        followers.remove(i);
    }

    // Methode, um Pebble-Zahl zu dekrementieren
    public void removePebble () {
        pebbles--;
    }

    // Methode, um Pebble-Zahl zu inkrementieren
    public void addPebble () {
        pebbles++;
    }
}