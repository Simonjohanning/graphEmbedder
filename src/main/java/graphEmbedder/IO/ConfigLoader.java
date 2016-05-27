package graphEmbedder.IO;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Class to load configurations from files into Hashmaps
 *
 * @author Simon Johanning
 */
public class ConfigLoader {

    private static final Logger LOG = LogManager.getLogger(ConfigLoader.class);

   /* public static final String BASE_PATH = "";*/
    public static final String SOURCE_PATH = "src/main/java/";

    /**
     * Loads the configuration of a file (as a hashmap-ready structure such as a JSON-file) into the hashmap using the jackson datamapper
     *
     * @param filepath The path where the configuration file is located at
     * @return A hashmap that associates the configuration key to the read objects
     * @throws IOException exception that is thrown if errors with the file to be read occur
     */
    public static HashMap<String, Object> loadConfiguration(String filepath) throws IOException{
        try {
            //read the values in the file using the jackson objectmapper
            ObjectMapper mapper = new ObjectMapper();
            HashMap<String,Object> configMap = mapper.readValue(new File(filepath), HashMap.class);
            return configMap;
        }catch(IOException ioe){
            LOG.warn("ERROR: Configuration for "+filepath+" could not be loaded!"+ ioe.toString());
            throw ioe;
        }
    }

    /**
     * Loads the configuration of a file (as a hashmap-ready structure such as a JSON-file) into the hashmap using the jackson datamapper
     *
     * @param filepath The path where the configuration file is located at
     * @return A hashmap that associates the configuration key to the read objects as strings
     * @throws IOException exception that is thrown if errors with the file to be read occur
     */
    public static HashMap<String, String> loadConfigurationStringmap(String filepath) throws IOException{
        try {
            //read the values in the file using the jackson objectmapper
            ObjectMapper mapper = new ObjectMapper();
            HashMap<String,String> configMap = mapper.readValue(new File(filepath), HashMap.class);
            return configMap;
        }catch(IOException ioe){
            LOG.warn("ERROR: Configuration for "+filepath+" could not be loaded!"+ ioe.toString());
            throw ioe;
        }
    }
}
