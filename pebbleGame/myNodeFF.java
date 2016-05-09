import java.util.*;
import java.io.*;

class myTest
{
	public static void main (String[] args)
	{
		// Anzahl der Pebbles im Game
		int pebbles;
		// Hilfsvariable
		int x = 1;
		// Listen von Knoten und Kanten
		List<myNode> nodes = new ArrayList<myNode>();
		List<myEdge> edges = new ArrayList<myEdge>();
		List<myComponent> components = new ArrayList<myComponent>();
		// Hilfsknoten/-kante
		myNode node1 = new myNode(1,2);
		myNode node2 = new myNode(2,2);
		myEdge edge1 = new myEdge(node1,node2);
		// Datei mit Knoten- & Kanteninformationen
//		String fileName="doubleTdoubleE";
		String fileName="nodesInvalid";
//		String fileName="flexibleSquare";
//		String fileName="bigOne";
			
			
			
			try{
			// FileReader
			FileReader inputFile = new FileReader(fileName);

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
		// Gesamtzahl der Pebbles speichern
		pebbles = (nodes.size() * 2);	
		
		// So lange es noch edges gibt, die nicht verarbeitet wurden...	        
		while(edges.size()>0){
		// Endknoten der Kante lesen
		edge1 = edges.get(0);
		node1 = edge1.myNode1();
		node2 = edge1.myNode2();
		// Überprüfen, ob bereits eine gerichtete Kante vorhanden
		if (true){
			// Pebbles sammeln, falls nicht genug vorhanden
			while (node1.givePebbles()<2){
				if(pebblesearch(node1,node2)){
					System.out.println ("Success!");	
				}
				else{
					System.out.println ("Can't find enough pebbles!");
					break;
				}
			}
			while (node2.givePebbles()<2){
				if(pebblesearch(node2,node1)){
					System.out.println ("Success!");	
				}
				else{
					System.out.println ("Can't find enough pebbles!");
					break;
				}
			}
			// Falls genug Pebbles vorhanden, gerichtete Kante einfügen
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
		// Bearbeitete Kante entfernen
		edges.remove(0);
		}
	// Falls verbleibende Gesamtzahl der Pebbles = 3, ist der Graph rigid!
	if (pebbles==3){
		System.out.println ("Remaining pebbles: " + pebbles + "! It's rigid!");
	}
	else{
		System.out.println ("Remaining pebbles: " + pebbles + "! It's not rigid!");
	}
	}


	private static boolean pebblesearch (myNode n, myNode m)
	{
		// Hilfsknoten
		myNode tempNode1 = new myNode(0,0);
		myNode tempNode2 = new myNode(0,0);
		myNode tempNode3 = new myNode(0,0);
		tempNode1 = n;
		tempNode3 = m;
		// Hilfsvariable		
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

class myNode
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

class myEdge
{
	// Knoten, die durch die Kante verbunden sind
	myNode a;
	myNode b;

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

class myComponent
{
	// Knoten und Kanten der Komponenten
	List<myNode> cNodes = new ArrayList<myNode>();
	List<myEdge> cEdges = new ArrayList<myEdge>();

	public static void main (String[] args)
	{
	}

	// Constructor, dem die Knoten + Kante übergeben werden
	public myComponent (myNode a, myNode b, myEdge c) {
		cNodes.add(a); 
       		cNodes.add(b); 
       		cEdges.add(c);
	}

	public void merge (myComponent d) {
		cNodes.addAll(d.giveNodes()); 
       		cEdges.addAll(d.giveEdges());
	}

	public void addNode (myNode a) {
		cNodes.add(a); 
       	}

	public void addEdge (myEdge a) {
		cEdges.add(a); 
       	}

	public List<myNode> giveNodes () {
		return cNodes;
	}

	public boolean contNode (myNode a) {
		if (cNodes.contains(a)) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean contEdge (myEdge a) {
		if (cEdges.contains(a)) {
			return true;
		}
		else {
			return false;
		}
	}

	public List<myEdge> giveEdges () {
		return cEdges;
	}

}
