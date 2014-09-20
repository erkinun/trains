package core;

/**
 * Created by ERKIN on 19/09/2014.
 */
public class Edge<W> {

    private W weight;
    private Node start;
    private Node end;

    public W getWeight() {
        return weight;
    }

    public void setWeight(W weight) {
        this.weight = weight;
    }

    public Node getStart() {
        return start;
    }

    public void setStart(Node start) {
        this.start = start;
    }

    public Node getEnd() {
        return end;
    }

    public void setEnd(Node end) {
        this.end = end;
    }
}
