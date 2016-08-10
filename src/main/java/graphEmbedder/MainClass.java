package graphEmbedder;

import graphEmbedder.IO.PebbleGameReader;
import graphEmbedder.algorithms.PebbleGame;
import graphEmbedder.configuration.GraphConfiguration;
import graphEmbedder.configuration.PebbleGameConfiguration;
import graphEmbedder.graph.Edge;
import graphEmbedder.graph.Graph;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;

import graphEmbedder.IO.ConfigLoader;

/**
 * Initializes and starts the program. Serves as entry point for the program
 * 
 * @author Simon Johanning
 *
 */
public class MainClass{

    private static final String PATHPREFIX = "src/main/resources/inputGraphs/";
    private static final String DEFAULTCONFIGURATION = "configurations/configurations.json";

    private static Logger initLogger = LogManager.getLogger("initLogger");
    private static Logger initConsoleLogger = LogManager.getLogger("initConsoleLogger");
    private static Logger debugLogger = LogManager.getLogger("debugLogger");

    public static void main(final String[] args) throws Exception {
        HashMap<String, Object> configurations = loadConfiguration(args);

        HashMap<String, Object> pgConfigurationMap = (HashMap<String, Object>) configurations.get("Pebble game configuration");
        PebbleGameConfiguration pgconf = new PebbleGameConfiguration(pgConfigurationMap);
        initConsoleLogger.info("Loaded pebble game information with k={}, l={}", pgconf.getK(), pgconf.getL());
        PebbleGame pebbleGame = new PebbleGame(pgconf);

        HashMap<String, Object> graphConfigurationMap = (HashMap<String, Object>) configurations.get("Graph configuration");
        GraphConfiguration graphConfig = new GraphConfiguration(graphConfigurationMap);
        graphConfig.setPathPrefix(PATHPREFIX);
        Graph graph = PebbleGameReader.readPebbleGameInformation(graphConfig);
        Graph minimalRigidGraph = pebbleGame.constructMinimallyRigidSubgraph(graph);
        initConsoleLogger.info("original graph has {} edges, where minimalRigidGraph has {} edges ",graph.getEdges().size(), minimalRigidGraph.getEdges().size());
        for (Edge edge : minimalRigidGraph.getEdges()) {
            initConsoleLogger.info("{},{}",edge.getSource().getId(), edge.getTarget().getId());
        }
        initLogger.info("Initialization complete");
	}

    private static HashMap<String, Object> loadConfiguration(String[] args) throws IOException, Exception{
        //try to set up the configuration Hashmaps
        try{
            String configPath = "";
            for(int i=0;i < args.length; i++){
                if(args[i].equals("-config") && (i+1) < args.length){
                    configPath = args[i+1].replaceAll("\\s+","");
                    if(configPath.length()> 0) {
                        /*if (!configPath.substring(configPath.length() - 1).equals("\\")) {
                            configPath = configPath+"\\";
                        }*/
                    }
                    break;
                }
            }
            //if config path is still "", load default one
            configPath = DEFAULTCONFIGURATION;
            if(configPath!=null){
                return ConfigLoader.loadConfiguration(configPath);
            }else{
                throw new Exception("No -config argument was provided!!");
            }
        } catch(IOException ioe){
            ioe.printStackTrace();
            throw ioe;
        } catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }


}
