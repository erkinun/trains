package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by unlue on 21/09/14.
 */
public class Trip {

    private List<Character> stops = new ArrayList<>();

    public Trip() { }

    public Trip(Trip that) {
        stops = new ArrayList<>(that.stops);
    }

    public void add(char stop) {
        stops.add(stop);
    }

    public char getLastStop() {
        return stops.get(stops.size()-1);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("Trip is: ");

        for (char stop : stops) {
            builder.append(stop + ", ");
        }

        return builder.toString();
    }
}
