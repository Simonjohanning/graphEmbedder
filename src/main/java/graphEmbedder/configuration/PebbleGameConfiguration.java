package graphEmbedder.configuration;

import java.util.HashMap;

/**
 * Created by sim on 31.05.16.
 */
public class PebbleGameConfiguration {

    private int k;
    private int l;

    public int getK() {
        return k;
    }

    public int getL() {
        return l;
    }

    public PebbleGameConfiguration(int k, int l) {

        this.k = k;
        this.l = l;
    }

    public PebbleGameConfiguration(HashMap<String, Object> pebbleGameConfiguration){
        this.k = (int) pebbleGameConfiguration.get("k");
        this.l = (int) pebbleGameConfiguration.get("l");
    }
}
