package graphEmbedder.graph;

import java.util.HashSet;

/**
 * Created by sim on 30.05.16.
 */
public class GraphHelper {

    public static Component unifyComponents(HashSet<Component> firstComponents, HashSet<Component> secondComponents){
        HashSet<Node> componentNodeList = new HashSet<>();
        for(Component currentComponent : firstComponents) {
            componentNodeList.addAll(currentComponent.getNodesInComponent());
        }
        for(Component currentComponent : secondComponents) {
            componentNodeList.addAll(currentComponent.getNodesInComponent());
        }
            return new Component(componentNodeList);
    }

    public static int getIntersectionSize(HashSet<Component> firstComponents, HashSet<Component> secondComponents){
        boolean set1IsLarger = firstComponents.size()>secondComponents.size();
        HashSet<Component> cloneSet = new HashSet<>(set1IsLarger ? secondComponents : firstComponents);
        cloneSet.retainAll(set1IsLarger ? firstComponents : secondComponents);
        return cloneSet.size();
    }
}
