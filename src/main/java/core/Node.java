package core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ERKIN on 19/09/2014.
 */
public class Node<V, W> {

    private V value;
    private List<Edge<W>> edges;

    public Node(V value, Edge edge) {
        this.value = value;
        edges = new ArrayList<Edge<W>>();
        edges.add(edge);
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public List<Edge<W>> getEdges() {
        return edges;
    }

    public V getValue() {
        return value;
    }
}
