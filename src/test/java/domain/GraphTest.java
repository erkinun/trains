package domain;

import core.TrainsException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class GraphTest {

    private Graph graph;

    @Before
    public void setUp() {
        graph = new Graph();
    }

    @Test
    public void testAddTownOk() throws Exception {
        String rep = "AB5";

        graph.addRoute(rep);

        Assert.assertTrue(graph.size() == 2);
    }

    @Test
    public void testAddTownThree() throws Exception {
        String rep = "AB5";
        String rep2 = "BC4";

        graph.addRoute(rep);
        graph.addRoute(rep2);

        int sizeOfGraph = graph.size();

        System.out.println("size: " + sizeOfGraph);

        Assert.assertTrue(sizeOfGraph == 3);
    }

    @Test
    public void testAddTownTwoThreeRoutes() throws Exception {
        String rep = "AB5";
        String rep2 = "BC4";
        String rep3 = "AC9";

        graph.addRoute(rep);
        graph.addRoute(rep2);
        graph.addRoute(rep3);

        Assert.assertTrue(graph.size() == 3);
    }

    @Test
    public void testAddThreeTowns() throws Exception {
        graph.addRoute("AB5");
        graph.addRoute("BC4");

        Town town = new Town('B');
        Town end = new Town('C');
        Route route = new Route(4, town, end);
        town.addRoute(route);

        Assert.assertTrue(graph.hasTown(town));

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowException() throws Exception {
        String rep = "";

        graph.addRoute(rep);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowLongerRep() {
        String rep = "1234";

        graph.addRoute(rep);

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowFormatWrong() {
        String rep = "3AB";

        graph.addRoute(rep);

    }

    @Test
    public void shouldFind9Distance() throws TrainsException {
        graph.addRoute("AB5");
        graph.addRoute("BC4");

        int distance = graph.findDistance("ABC");

        Assert.assertEquals(9, distance);
    }

    @Test
    public void shouldFind5() throws TrainsException {

        createGraph();

        int distance = graph.findDistance("AD");

        Assert.assertEquals(5, distance);
    }

    @Test
    public void shouldFind13() throws TrainsException {

        createGraph();

        int distance = graph.findDistance("ADC");

        Assert.assertEquals(13, distance);
    }

    @Test
    public void shouldFind22() throws TrainsException {

        createGraph();

        int distance = graph.findDistance("AEBCD");

        Assert.assertEquals(22, distance);
    }

    @Test(expected = TrainsException.class)
    public void shouldThrowNoSuchRoute() throws TrainsException {

        createGraph();

        graph.findDistance("AED");
    }

    @Test
    public void findTripsFromCtoCIn3Stops() throws TrainsException {

        createGraph();

        int findRouteNumber = graph.findTrips('C', 'C', 3);

        Assert.assertEquals(2, findRouteNumber);
    }

    @Test
    public void findExactStepsForTrips() throws TrainsException {

        createGraph();

        long routeNumber = graph.findExactStepTrips('A', 'C', 4);

        Assert.assertEquals(3, routeNumber);
    }

    @Test
    public void testShortestPath() throws Exception {

        createGraph();

        int distance = graph.findShortestPathLength('A','C');

        Assert.assertEquals(9, distance);
    }

    @Test
    public void testShortestPathAB() throws Exception {

        createGraph();

        int distance = graph.findShortestPathLength('A','B');

        Assert.assertEquals(5, distance);
    }

    @Test
    public void testShortestPathBE() throws Exception {

        createGraph();

        int distance = graph.findShortestPathLength('B','E');

        Assert.assertEquals(6, distance);
    }

    @Test
    public void testShortestPathCE() throws Exception {

        createGraph();

        int distance = graph.findShortestPathLength('C','E');

        Assert.assertEquals(2, distance);
    }

    @Test
    public void testShortestPathBB() throws Exception {

        createGraph();

        int distance = graph.findShortestPathLength('B','B');

        Assert.assertEquals(9, distance);
    }

    private void createGraph() {
        graph.addRoute("AB5");
        graph.addRoute("BC4");
        graph.addRoute("CD8");
        graph.addRoute("DC8");
        graph.addRoute("DE6");
        graph.addRoute("AD5");
        graph.addRoute("CE2");
        graph.addRoute("EB3");
        graph.addRoute("AE7");
    }
}