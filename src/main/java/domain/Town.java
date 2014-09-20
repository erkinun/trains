package domain;

import core.Edge;
import core.Node;

/**
 * Created by ERKIN on 20/09/2014.
 */
public class Town extends Node<Character, Integer> {

    public Town(Character value, Edge edge) {
        super(value, edge);
    }

    public Town(Character value) {
        super(value);
    }

    public char getName() {
        return getValue();
    }

    public void addRoute(Route route) {
        addEdge(route);
    }
}
