package graphEmbedder.graph;
import java.util.HashSet;

public class Node{

    private String label;
    private int id;
//  private Component associatedComponent;
    private HashSet<Component> associatedComponents;

    public Node(String label, int id, Component associatedComponent) {
        this.label = label;
        this.id = id;
        setAssociatedComponents(associatedComponent);
    }

    public void setAssociatedComponents(Component associatedComponent) {
        HashSet<Component> tempCSet = new HashSet<>();
        tempCSet.add(associatedComponent);
        setAssociatedComponents(tempCSet);
    }

    public void setAssociatedComponents(HashSet<Component> associatedComponents) {
        this.associatedComponents = associatedComponents;
    }

    public void addAssociatedComponent(Component associatedComponent) {
        this.associatedComponents.add(associatedComponent);
    }

    public String getLabel() {

        return label;
    }

    public int getId() {
        return id;
    }

    public HashSet<Component> getAssociatedComponents() {
        return this.associatedComponents;
    }
}