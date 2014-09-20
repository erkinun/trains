package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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

        validateRepresentation(representation);

        //search for town
        final char firstTownName = representation.charAt(0);
        Stream townStream = towns.stream().filter(town -> town.getName() == firstTownName);
        long townCount = townStream.count();


        if (townCount == 1){
            //fount
            char secondTownName = representation.charAt(1);
            Town secondTown = new Town(secondTownName);

            Town firstTown = (Town) townStream.findFirst().get();
            int distance = Integer.valueOf(representation.charAt(2));
            Route route = new Route(distance, firstTown, secondTown);
            firstTown.addRoute(route);
        }
        else if (townCount == 0){

            char secondTownName = representation.charAt(1);
            Town secondTown = new Town(secondTownName);
            Town firstTown = new Town(firstTownName);

            int distance = Integer.valueOf(representation.charAt(2));
            Route route = new Route(distance, firstTown, secondTown);
            firstTown.addRoute(route);
            towns.add(firstTown);
        }
        else {
            //something wrong
            throw new IllegalStateException("There is a town with more than one instance");
        }
    }

    private void validateRepresentation(String representation) {
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
