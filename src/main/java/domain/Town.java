package domain;

import core.Edge;
import core.Node;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ERKIN on 20/09/2014.
 */
public class Town extends Node<Character, Integer> {

    private static final char GHOST = 'x';

    public static Town ghostTown(){
        return new Town(GHOST);
    }

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

    public List<Route> getRoutes() {
        return getEdges().stream().map((Edge<Integer> edge) ->
                new Route(edge.getWeight(), edge.getStart(), edge.getEnd())).collect(Collectors.toList());
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Town of: " + getValue() + ", has " + getRoutes().size() + " routes outside");
        return builder.toString();
    }
}
