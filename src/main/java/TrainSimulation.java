import domain.Graph;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ERKIN on 19/09/2014.
 */
public class TrainSimulation {

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

        //validate

        //process the problems

        //save each result

        //output somehow
    }
}
