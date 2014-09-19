package core;

/**
 * Created by ERKIN on 19/09/2014.
 */
public class Vertice<V> {

    private V value;
    private Edge[] edges;

    public Vertice(Edge[] edges, V value) {
        this.value = value;
        this.edges = edges.clone();
    }

    public Edge[] getEdges() {
        return edges.clone();
    }

    public V getValue() {
        return value;
    }
}
