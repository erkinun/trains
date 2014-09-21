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

    public boolean isGoingTo(char dest) {
        return getEnd().getValue().equals(dest);
    }

    public Town goesTo() {
        return (Town) getEnd();
    }

    public Town goesFrom() {
        return (Town) getStart();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("route from: " + getStart().getValue() + " to: " + getEnd().getValue()
                + ", of distance: " + getWeight());

        return builder.toString();
    }

    public boolean startsFrom(char start) {
        return getStart().getValue().equals(start);
    }

    public boolean endsAt(char end) {
        return getEnd().getValue().equals(end);
    }
}
