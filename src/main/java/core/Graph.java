package core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ERKIN on 19/09/2014.
 */
public class Graph<V, W> {

    private List<Node<V, W>> nodes;

    public Graph() {
        nodes = new ArrayList<Node<V, W>>();
    }

    public int size() {
        return nodes.size();
    }
}
