package graphEmbedder.graph;

public class Node{

    private String label;
    private int id;
    private Component associatedComponent;

    public Node(String label, int id, Component associatedComponent) {
        this.label = label;
        this.id = id;
        this.associatedComponent = associatedComponent;
    }

    public void setAssociatedComponent(Component associatedComponent) {
        this.associatedComponent = associatedComponent;
    }

    public String getLabel() {

        return label;
    }

    public int getId() {
        return id;
    }

    public Component getAssociatedComponent() {
        return associatedComponent;
    }
}