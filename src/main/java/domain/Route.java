package domain;

import core.Edge;
import core.Node;

/**
 * Created by ERKIN on 20/09/2014.
 */
public class Route extends Edge<Integer> {
    public Route(Integer weight, Node start, Node end) {
        super(weight, start, end);
    }
}
