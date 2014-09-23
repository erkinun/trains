package domain;

import core.TrainsException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
                System.out.println(distance + " could not find: " + stop );
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

    public int findShortestPathLength(char start, char end) {

        Optional<Town> startTown = findTown(start);
        Optional<Town> endTown = findTown(end);

        ShortestPathAlgorithm algorithm = new ShortestPathAlgorithm();

        return algorithm.findShortestPath(startTown.orElseThrow(IllegalArgumentException::new),
                endTown.orElseThrow(IllegalArgumentException::new));
    }

    //find cycles for town
    //get their weight
    public int findCyclicRoutesWithMax(char town, int max) throws TrainsException {

        Optional<Town> start = findTown(town);

        CyclesFindAlgorithm algorithm = new CyclesFindAlgorithm();
        List<List<Town>> lists = algorithm.findCyclesForTown(start.orElseThrow(IllegalArgumentException::new));

        List<Cycle> cycles = lists.stream().map(list -> new Cycle(list)).collect(Collectors.toList());

        int cycleCount = 0;
        int cycleWeight = 0;

        for (Cycle cycle : cycles) {
            algorithm.innerCycle(cycle, cycles, cycleWeight, max);
            int innerCount = algorithm.getCycleCount();
            System.out.println(cycle + " found count: " + innerCount);
            algorithm.resetCycleCount();
            cycleCount += innerCount;
        }

        return cycleCount;
    }


    public int findCycleSize(char town) throws TrainsException {

        Optional<Town> start = findTown(town);

        CyclesFindAlgorithm algorithm = new CyclesFindAlgorithm();
        List<List<Town>> lists = algorithm.findCyclesForTown(start.orElseThrow(IllegalArgumentException::new));

        lists.stream().forEach(list -> {
            System.out.print("Cycle of: ");
            list.stream().forEach(tw -> System.out.print(tw.getName()));
            System.out.println();
        });

        return lists.size();
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
            throw new IllegalArgumentException("Representation String must be length 3, instead: " + representation);
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

    private class CyclesFindAlgorithm {

        private int cycleCount = 0;

        public void innerCycle(Cycle cycle, List<Cycle> cycles, int cycleWeight, int max) {

            if (cycle.getWeight() + cycleWeight < max) {
                cycleCount++;
            }
            else {
                return;
            }

            int newWeight = cycleWeight + cycle.getWeight();

            for (Cycle nextCycle : cycles) {
                innerCycle(nextCycle, cycles, newWeight, max);
            }
        }

        public List<List<Town>> findCyclesForTown(Town parent) throws TrainsException {

            //create a list for the parent town
            List<List<Town>> cycles = new ArrayList<List<Town>>();

            if (parent.getRoutes().size() == 0) {
                throw new TrainsException(TrainsException.NO_CYCLE_ROUTE);
            }

            List<Town> parentCycle = new ArrayList<>();
            parentCycle.add(parent);

            parent.getRoutes().stream().forEach(route -> {
                List<List<Town>> depthCycles = findInnerCycles(route.goesTo(), parentCycle);
                if (depthCycles != null) {

                    for(List<Town> townList : depthCycles) {
                        cycles.add(townList);
                    }
                }
            });

            return cycles;
        }

        public int getCycleCount() {
            return cycleCount;
        }

        private List<List<Town>> findInnerCycles(Town town, List<Town> parentCycle) {

            List<Town> nodeCycles = new ArrayList<>(parentCycle);

            List<List<Town>> innerCycles = new ArrayList<>();

            if (nodeCycles.contains(town)) {
                //found a cycle
                nodeCycles.add(town);
                innerCycles.add(nodeCycles);
                return innerCycles;
            }

            if (town.getRoutes().size() == 0) {
                //end of the road
                return null;
            }

            nodeCycles.add(town);

            town.getRoutes().stream().forEach(route -> {
                List<List<Town>> childCycles = findInnerCycles(route.goesTo(), nodeCycles);

                if (childCycles != null) {
                    for (List<Town> townList : childCycles) {
                        innerCycles.add(townList);
                    }
                }
            });

            return innerCycles;
        }

        public void resetCycleCount() {
            cycleCount = 0;
        }
    }

    private class ShortestPathAlgorithm {

        //distance to each node from source
        private Map<Town, Integer> distances;
        //visited nodes
        private List<Town> visited;
        //unvisited nodes
        private List<Town> unvisited;

        private Comparator<Map.Entry<Town, Integer>> byDistance = (e1, e2) -> Integer.compare(e1.getValue(), e2.getValue());

        //Dijkstra's shortest path alg implementation
        public int findShortestPath(Town start, Town end) {

            visited = new ArrayList<>();
            unvisited = new ArrayList<>(towns);
            distances = new HashMap<>();

            //TODO check for start and end

            unvisited.stream().forEach(town -> distances.put(town, Integer.MAX_VALUE));
            distances.put(start, 0);
            //start visiting neighbors
            while (!unvisited.isEmpty()) {

                //get the nearest town to source
                //first get the unvisited ones in distances map
                Map.Entry<Town, Integer> nearest =
                        distances.entrySet().stream().sorted(byDistance).filter(entry ->
                                unvisited.contains(entry.getKey())).findFirst().orElseThrow(IllegalStateException::new);

                Town nearestTown = nearest.getKey();

                //remove the town from unvisited
                unvisited.remove(nearestTown);
                //add to visited
                visited.add(nearestTown);

                //foreach neighbor of town
                nearestTown.getRoutes().stream().forEach(route -> {
                    //calculate the distance edge distance + town distance

                    int distance = distances.get(nearestTown) == Integer.MAX_VALUE ? Integer.MAX_VALUE :
                            distances.get(nearestTown)+ route.getWeight();

                    //if lower than dist neighbor
                    //update the neighbor distance
                    if (distance < distances.get(route.goesTo()) || (isCyclicRoute(start, route.goesTo())
                            && distances.get(route.goesTo()) == 0)) {
                        //we found a new near route
                        distances.put(route.goesTo(), distance);
                    }
                });

            }

            return distances.get(end);
        }

        private boolean isCyclicRoute(Town start, Town end) {
            return start.equals(end);
        }
    }

    private class Cycle {
        private List<Town> towns;
        private String routeText;
        private int weight;

        private Cycle(List<Town> towns) {

            routeText = (String)towns.stream().map(town -> String.valueOf(town.getName())).reduce((name, str) ->
                    (String)name + (String)str ).get();

            this.towns = towns;
            try {
                this.weight = findDistance(routeText);
            } catch (TrainsException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }

        public int getWeight() {
            return weight;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder("Cycle of route: ");
            builder.append(routeText).append(", of weight: ").append(weight);

            return builder.toString();
        }
    }
}
