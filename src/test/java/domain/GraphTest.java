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

        graph.addTown(rep);

        Assert.assertTrue(graph.size() == 1);
    }

    @Test
    public void testAddTownTwo() throws Exception {
        String rep = "AB5";
        String rep2 = "BC4";

        graph.addTown(rep);
        graph.addTown(rep2);

        Assert.assertTrue(graph.size() == 2);
    }

    @Test
    public void testAddTownTwoThreeRoutes() throws Exception {
        String rep = "AB5";
        String rep2 = "BC4";
        String rep3 = "AC9";

        graph.addTown(rep);
        graph.addTown(rep2);
        graph.addTown(rep3);

        Assert.assertTrue(graph.size() == 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowException() throws Exception {
        String rep = "";

        graph.addTown(rep);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowLongerRep() {
        String rep = "1234";

        graph.addTown(rep);

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowFormatWrong() {
        String rep = "3AB";

        graph.addTown(rep);

    }
}