package graphEmbedder.graph;

public class Edge{

    private String label;
    private Node source;
    private Node target;

    public Edge(Node source, Node target) {
        this.source = source;
        this.target = target;
        this.label = "";
    }

    public Edge(String label, Node source, Node target) {

        this.label = label;
        this.source = source;
        this.target = target;
    }

    public String getLabel() {

        return label;
    }

    public Node getSource() {
        return source;
    }

    public Node getTarget() {
        return target;
    }
}