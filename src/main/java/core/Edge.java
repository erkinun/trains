package core;

/**
 * Created by ERKIN on 19/09/2014.
 */
public class Edge<W> {

    private W weight;
    private Vertice start;
    private Vertice end;

    public W getWeight() {
        return weight;
    }

    public void setWeight(W weight) {
        this.weight = weight;
    }

    public Vertice getStart() {
        return start;
    }

    public void setStart(Vertice start) {
        this.start = start;
    }

    public Vertice getEnd() {
        return end;
    }

    public void setEnd(Vertice end) {
        this.end = end;
    }
}
