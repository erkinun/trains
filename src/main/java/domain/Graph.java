package domain;

import core.TrainsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by ERKIN on 19/09/2014.
 */
public class Graph {

    private static final int REP_LEN = 3; //representation string length

    private List<Town> towns;
    private Town ghostTown; //used for linking to all towns

    public Graph() {
        towns = new ArrayList<>();
        ghostTown = Town.ghostTown();
    }

    public int size() {
        return towns.size();
    }

    public boolean hasTown(Town town) {

        List<Town> townList = towns.stream().filter(innerTown ->
                innerTown.getName() == town.getName()).collect(Collectors.toList());


        if (townList.size() == 0) {
            return false;
        }

        Town realTown = townList.get(0);

        for (Route rt : town.getRoutes()) {
            List<Route> ways = realTown.getRoutes().stream().filter(route ->
                    route.isGoingTo(rt.goesTo().getValue()) &&
                            route.getWeight().equals(rt.getWeight())).collect(Collectors.toList());

            if (ways.size() == 0) {
                return false;
            }
        }

        return true;
    }

    public void addRoute(String representation) {
        //it must be in format "AB5"
        //if not illegalargument

        validateRepresentation(representation);

        //search for town
        final char firstTownName = representation.charAt(0);
        Optional<Town> firstTownOpt = findTown(firstTownName);


        if (firstTownOpt.isPresent()){
            //found
            char secondTownName = representation.charAt(1);
            Town secondTown = new Town(secondTownName);

            Town firstTown = (Town) firstTownOpt.get();
            int distance = Character.getNumericValue(representation.charAt(2));
            Route route = new Route(distance, firstTown, secondTown);
            firstTown.addRoute(route);
        }
        else {

            char secondTownName = representation.charAt(1);
            Town secondTown = new Town(secondTownName);
            Town firstTown = new Town(firstTownName);

            int distance = Integer.valueOf(representation.charAt(2));
            Route route = new Route(distance, firstTown, secondTown);
            firstTown.addRoute(route);
            towns.add(firstTown);

            addGhostRoute(firstTown);
        }
    }

    private void addGhostRoute(Town town) {
        Route ghostRoute = new Route(0, ghostTown, town);
        ghostTown.addRoute(ghostRoute);
    }

    //will find from ABC A->B->C
    public int findDistance(String route) throws TrainsException {
        char[] stops = route.toCharArray();

        List<Route> routes = ghostTown.getRoutes();

        int distance = 0;
        int i = 0;
        for (char stop : stops) {

            System.out.println("search for stop: " + stop);

            for (Route rt : routes) {
                System.out.println(rt);
            }

            //find the town
            List<Route> onWay = routes.stream().filter(inner -> inner.isGoingTo(stop)).collect(Collectors.toList());

            if (onWay.size() == 0) {
                throw new TrainsException(TrainsException.NO_SUCH_ROUTE);
            }

            //check predicate for list size 1

            Route thisWay = onWay.get(0);

            //ghost has zero distance
            distance = distance + thisWay.getWeight();

            routes = thisWay.goesTo().getRoutes();
            i++;

        }

        return distance;
    }

    private Optional<Town> findTown(char firstTownName) {
        Stream townStream = towns.stream().filter(town -> town.getName() == firstTownName);
        return townStream.findFirst();
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
