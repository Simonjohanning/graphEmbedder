package graphEmbedder.configuration;

import java.util.HashMap;

/**
 * Created by sim on 31.05.16.
 */
public class GraphConfiguration {

    String type;
    String fileName;
    String pathPrefix;

    public String getType() {
        return type;
    }

    public String getFileName() {
        return fileName;
    }

    public GraphConfiguration(String type, String fileName) {
        this.type = type;
        this.fileName = fileName;
        this.pathPrefix = "";
    }

    public GraphConfiguration(HashMap<String, Object> graphConfiguration){
        this.type = (String) graphConfiguration.get("type");
        this.fileName = (String) graphConfiguration.get("fileName");
        this.pathPrefix = "";
    }

    public void setPathPrefix(String pathPrefix){
        this.pathPrefix = pathPrefix;
    }

    public String getFilePath(){
        return pathPrefix+fileName;
    }
}
