package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ERKIN on 19/09/2014.
 */
public class Graph {

    private static final int REP_LEN = 3; //representation string length

    private List<Town> towns;

    public Graph() {
        towns = new ArrayList<Town>();
    }

    public int size() {
        return towns.size();
    }

    public void addTown(String representation) {
        //it must be in format "AB5"
        //if not illegalargument

        if (representation.length() != REP_LEN) {
            throw new IllegalArgumentException("Representation String must be length 3");
        }

        char firstChar = representation.charAt(0);

        if (!Character.isLetter(firstChar)) {
             throw new IllegalArgumentException("First character must be a letter");
        }

        char secondChar = representation.charAt(1);

        if (!Character.isLetter(secondChar)) {
            throw new IllegalArgumentException("second character must be a letter");
        }

        char thirdChar = representation.charAt(2);

        if (!Character.isDigit(thirdChar)) {
            throw new IllegalArgumentException("third character must be a digit");
        }
    }
}
