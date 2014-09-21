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

    public long findExactStepTrips(char start, char end, int stops) throws TrainsException {

        List<Route> availableRoutes = findStartingRoutes(start);

        if (availableRoutes.size() == 0) {
            throw new TrainsException(TrainsException.NO_SUCH_ROUTE);
        }

        List<Trip> trips = new ArrayList<>();
        availableRoutes.stream().map(Route::goesFrom).distinct().forEach(town -> {
            Trip trip = new Trip();
            trip.add(town.getName());
            trips.add(trip);
        });

        List<Trip> updatedTrips = new ArrayList<Trip>();
        for (int step = 0; step < stops; step++) {

            availableRoutes.stream().forEach(route -> {
                trips.stream().filter(trip -> trip.getLastStop() == route.goesFrom().getName()).forEach(trip -> {
                            Trip updateTrip = new Trip(trip);
                            updateTrip.add(route.goesTo().getName());
                            updatedTrips.add(updateTrip);
                        }
                );
            });

            trips.clear();
            updatedTrips.forEach(trips::add);
            updatedTrips.clear();

            List<Route> tempRoutes = new ArrayList<>(availableRoutes);
            availableRoutes.clear();

            //update the new routes to be followed in next iteration
            tempRoutes.stream().map(Route::goesTo).distinct().forEach(town ->
                    town.getRoutes().stream().forEach(availableRoutes::add));
        }

        return trips.stream().filter(trip -> {
            return trip.getLastStop() == end;
        }).count();
    }

    public int findTrips(char start, char end, int maxStop) throws TrainsException {

        List<Route> availableRoutes = findStartingRoutes(start);

        if (availableRoutes.size() == 0) {
            throw new TrainsException(TrainsException.NO_SUCH_ROUTE);
        }

        //TODO may need more work to print out the exact routes
        List<Route> allFinishedRoutes = new ArrayList<>();

        for (int step = 0; step < maxStop; step++) {
            //find finished trips
            availableRoutes.stream().filter(route -> route.isGoingTo(end)).forEach(allFinishedRoutes::add);

            //make room for new routes
            List<Route> tempRoutes = new ArrayList<>(availableRoutes);
            availableRoutes.clear();

            //update the new routes to be followed in next iteration
            tempRoutes.stream().map(Route::goesTo).distinct().forEach(town ->
                    town.getRoutes().stream().forEach(availableRoutes::add));
        }

        return allFinishedRoutes.size();
    }

    public void addRoute(String representation) {
        //it must be in format "AB5"
        //if not illegalargument

        validateRepresentation(representation);

        //search for town
        final char firstTownName = representation.charAt(0);
        Optional<Town> firstTownOpt = findTown(firstTownName);

        char secondTownName = representation.charAt(1);
        Optional<Town> secondTownOpt = findTown(secondTownName);
        Town secondTown = secondTownOpt.orElseGet(() -> getNewTown(secondTownName));

        Town firstTown = firstTownOpt.orElseGet(() -> getNewTown(firstTownName));

        int distance = Character.getNumericValue(representation.charAt(2));
        Route route = new Route(distance, firstTown, secondTown);
        firstTown.addRoute(route);

    }

    //will find from ABC A->B->C
    public int findDistance(String route) throws TrainsException {
        char[] stops = route.toCharArray();

        List<Route> routes = ghostTown.getRoutes();

        int distance = 0;
        for (char stop : stops) {

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

        }

        return distance;
    }

    private List<Route> findStartingRoutes(char start) {
        List<Route> routes = ghostTown.getRoutes();

        //starting routes
        List<Route> availableRoutes = new ArrayList<>();

        routes.stream().filter(route ->
                route.endsAt(start)).map(Route::goesTo).distinct().forEach(town ->
                town.getRoutes().stream().forEach(availableRoutes::add));
        return availableRoutes;
    }

    private Town getNewTown(char name) {
        Town newTown = new Town(name);
        towns.add(newTown);

        addGhostRoute(newTown);

        return newTown;
    }

    private Optional<Town> findTown(char firstTownName) {
        Stream townStream = towns.stream().filter(town -> town.getName() == firstTownName);
        return townStream.findFirst();
    }


    private void addGhostRoute(Town town) {
        Route ghostRoute = new Route(0, ghostTown, town);
        ghostTown.addRoute(ghostRoute);
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
