import core.TrainsException;
import domain.Graph;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ERKIN on 19/09/2014.
 */
public class TrainSimulation {

    private Graph graph;

    public TrainSimulation(Graph graph) {
        this.graph = graph;
    }

    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            System.out.println("You have to supply the graph file as the first argument");
            return;
        }

        //get the input file
        String fileName = args[0];

        String data;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),
                "UTF-8"))) {
            data = br.readLine();
        }

        if (data == null || data.equals("")) {
            System.out.println("Graph data is empty");
            return;
        }

        Graph graph = new Graph();

        String routesInfo = data.replace("Graph: ", "");
        List<String> routes = Arrays.asList(routesInfo.split(","));

        for (String route : routes) {
            graph.addRoute(route);
        }


        TrainSimulation simulation = new TrainSimulation(graph);

        //process the problems
        List<String> answers = new ArrayList<>();
        final String output = "OUTPUT #";
        answers.add(output + "1" + simulation.findDistance("ABC"));

        answers.add(output + "2" + simulation.findDistance("AD"));

        answers.add(output + "3" + simulation.findDistance("ADC"));

        answers.add(output + "4" + simulation.findDistance("AEBCD"));

        answers.add(output + "5" + simulation.findDistance("AED"));

        answers.add(output + "6" + simulation.findTrips());

        answers.add(output + "7" + simulation.findExactStepTrips());

        answers.add(output + "8" + simulation.shortestPath('A', 'C'));

        answers.add(output + "9" + simulation.shortestPath('B', 'B'));

        answers.add(output + "10" + simulation.getCyclicRouteCountWithMaxDist());

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output.txt"),
                "UTF-8"))) {
            //save each result
            for (String answer : answers) {
                bw.write(answer);
                bw.newLine();
            }
        }

    }

    private String findDistance(String route) {
        try {
            int distance = graph.findDistance(route);
            return String.valueOf(distance);
        } catch (TrainsException e) {
            return e.getMessage();
        }
    }

    private String findTrips() {
        try {
            int findRouteNumber = graph.findTrips('C', 'C', 3);
            return String.valueOf(findRouteNumber);
        } catch (TrainsException e) {
            return e.getMessage();
        }
    }

    private String findExactStepTrips() {
        try {
            long routeNumber = graph.findExactStepTrips('A', 'C', 4);
            return String.valueOf(routeNumber);
        } catch (TrainsException e) {
            return e.getMessage();
        }
    }

    private String shortestPath(char start, char end) {
        int distance = graph.findShortestPathLength(start, end);
        return String.valueOf(distance);
    }

    private String getCyclicRouteCountWithMaxDist() {
        try {
            int count = graph.findCyclicRoutesWithMax('C', 30);
            return String.valueOf(count);
        } catch (TrainsException e) {
            return e.getMessage();
        }
    }
}
