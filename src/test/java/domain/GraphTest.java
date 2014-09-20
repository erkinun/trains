package domain;

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