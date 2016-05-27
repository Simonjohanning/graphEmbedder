package  projectName

/**
 * Initializes and starts the program. Serves as entry point for the program
 * 
 * @author Simon Johanning
 *
 */
public class MainClass{

    private static Logger initLogger = LogManager.getLogger("initLogger");
    private static Logger initConsoleLogger = LogManager.getLogger("initConsoleLogger");
    private static Logger debugLogger = LogManager.getLogger("debugLogger");

    public static void main(final String[] args) throws Exception {
        HashMap<String, String> configurations;
        //try to set up the configuration Hashmaps
        try{
            String configPath = "";
            for(int i=0;i < args.length; i++){
                if(args[i].equals("-config") && (i+1) < args.length){
                    configPath = args[i+1].replaceAll("\\s+","");
                    if(configPath.length()> 0) {
                        if (!configPath.substring(configPath.length() - 1).equals("\\")) {
                            configPath = configPath+"\\";
                        }
                    }
                    break;
                }
            }
            //TODO just load default if no valid path provided
            if(configPath!=null){
                configurations = loadConfigurations(configPath);
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
        initLogger.info("Initialization complete");
	}

    /**
     * Load the configurations in the target/configuration/configuration.json file into the returned map
     *
     * @param configPath the path where the configuration folder is located
     * @return A hashmap following the structure of the configuration JSON-file, in order for the configurationMaps to access the corresponding configuration files
     * @throws IOException Thrown when an IO-Error occurs during reading the configuration file (and default configurations will be loaded)
     */
    private static HashMap<String, String> loadConfigurations(String configPath) throws IOException{
        HashMap<String, String> configurations;
        try {
            //load configurations using the ConfigLoader
            configurations = ConfigLoader.loadConfigurationStringmap(configPath+"configurations/configurations.json");
            for(String key : configurations.keySet()){
                LOG.info("Reading configuration: "+key+": "+configurations.get(key));
            }
            LOG.info("configurations: "+configurations.toString());
            return configurations;
        } catch (IOException e) {
            LOG.error("Could not load file configurations/configurations.json. IOException: "+e);
            e.printStackTrace();
            throw e;
        }
    }
}
