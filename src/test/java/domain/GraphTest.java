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
}