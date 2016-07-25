package graphEmbedder.IO;

import java.util.*;
import java.io.*;

import graphEmbedder.configuration.GraphConfiguration;
import graphEmbedder.graph.*;
import graphEmbedder.helper.GraphConverter;
import graphEmbedder.helper.SdfParseHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PebbleGameReader{

    private static Logger importLogger = LogManager.getLogger("importLogger");

    public static Graph readPebbleGameInformation(GraphConfiguration graphConfiguration) throws Exception{
        switch (graphConfiguration.getType()){
            case "mariusGraph":
                return readMariusGraph(graphConfiguration.getFilePath());
            case "pubchemGraph":
                return readPubchemGraph(graphConfiguration.getFilePath());
            default:
                throw new Exception("This case has not been implemented!!");
        }
    }

    private static Graph readPubchemGraph(String filePath) {
        System.out.println("filePath is "+filePath);

        // Listen von Knoten und Kanten
        List<SdfNode> nodes = new ArrayList<SdfNode>();
        List<SdfEdge> edges = new ArrayList<SdfEdge>();

        try {
            // FileReader
            FileReader inputFile = new FileReader(filePath);

            // BufferedReader
            BufferedReader bufferReader = new BufferedReader(inputFile);

            // Variable, die jeweils Zeile enthält
            String line;
            //line count in order to handle sdf-file depending on line one is in
            int lineCount = 0;

            //ints to hold num of atoms and bonds in order to know where one is in the file
            int numAtoms = 0, numBonds = 0;

            // Datei Zeile für Zeile lesen und Knoten/Kanten speichern
            while ((line = bufferReader.readLine()) != null) {
                importLogger.debug("Line count is {}, numAtoms={}, numBonds={}", lineCount,numAtoms,numBonds);
                //4th line in sdf specifies the number of atoms and bonds
                if(lineCount == 3) {
                    String[] lineContent = line.split(" ");
                    //specifies the position within the line of the non-empty entries (non-whitespace)
                    int inLinePosition = 0;
                    for (String content : lineContent) {
                        if (content.length() > 0) {
                            importLogger.debug("In 3rd row: {} with {} entries", line, lineContent.length);
                            switch (inLinePosition) {
                                //first entry: number of atoms
                                case 0:
                                    importLogger.debug("Content is {}");
                                    numAtoms = Integer.valueOf(content);
                                    importLogger.debug("numAtoms is {}");
                                    break;
                                //second entry: number of bonds
                                case 1:
                                    importLogger.debug("Content is {}");
                                    numBonds = Integer.valueOf(content);
                                    importLogger.debug("numBonds is {}");
                                    break;
                                //default case irrelevant for now
                                default:
                            }
                        //proceed to next position
                        inLinePosition++;
                        }
                    }
                }
                //next numAtoms lines hold the atom information (nodeID is lineCount-3, since the nodes start at the 4th line)
                else if((lineCount > 3 ) && (lineCount < 4+numAtoms)) nodes.add(SdfParseHelper.parseNode(line, lineCount-3));
                //next numBonds lines hold the bond information
                else if((lineCount > 3+numAtoms ) && (lineCount < 4+numAtoms+numBonds)) edges.add(SdfParseHelper.parseEdge(line));
                else importLogger.debug("Line is not relevant for the configuration. Line is: \n {} ", line);
                lineCount++;
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
        return GraphConverter.pubchemGraphToSimonGraph(nodes,edges);
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

        return GraphConverter.mariusGraphToSimonGraph(nodes, edges, components);
    }


}

