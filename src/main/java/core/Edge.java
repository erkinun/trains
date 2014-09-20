package core;

/**
 * Created by ERKIN on 19/09/2014.
 */
public class Edge<W> {

    private W weight;
    private Node start;
    private Node end;

    public Edge(W weight, Node start, Node end) {
        this.weight = weight;
        this.start = start;
        this.end = end;
    }

    public W getWeight() {
        return weight;
    }

    public Node getStart() {
        return start;
    }

    public Node getEnd() {
        return end;
    }
}
