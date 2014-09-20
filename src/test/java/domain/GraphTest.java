package domain;

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

        Assert.assertTrue(graph.size() == 1);
    }

    @Test
    public void testAddTownTwo() throws Exception {
        String rep = "AB5";
        String rep2 = "BC4";

        graph.addRoute(rep);
        graph.addRoute(rep2);

        Assert.assertTrue(graph.size() == 2);
    }

    @Test
    public void testAddTownTwoThreeRoutes() throws Exception {
        String rep = "AB5";
        String rep2 = "BC4";
        String rep3 = "AC9";

        graph.addRoute(rep);
        graph.addRoute(rep2);
        graph.addRoute(rep3);

        Assert.assertTrue(graph.size() == 2);
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
}