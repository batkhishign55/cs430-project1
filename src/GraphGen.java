package src;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraphGen {
    public static List<List<Integer>> randomGraph(int n, double p, boolean directed) {
        List<Integer> nodes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            nodes.add(i);
        }
        List<List<Integer>> adjList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adjList.add(new ArrayList<>());
        }
        Random random = new Random();
        for (int u : nodes) {
            for (int v : nodes) {
                if (directed || u < v) {
                    if (random.nextDouble() < p) {
                        adjList.get(u).add(v);
                        if (!directed) {
                            adjList.get(v).add(u);
                        }
                    }
                }
            }
        }
        return adjList;
    }

    public static void main(String[] args) {

        int n = Integer.parseInt(args[0]);
        double p = Double.parseDouble(args[1]);
        System.out.println(String.format("%d, %f", n, p));
        boolean directed = false;
        List<List<Integer>> adjList = randomGraph(n, p, directed);
        OutputStream os = null;

        try {
            String row = "";
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < adjList.get(i).size(); j++) {
                    System.out.print(adjList.get(i).get(j) + " ");
                    row += adjList.get(i).get(j);
                    if (j < adjList.get(i).size() - 1) {
                        row += " ";
                    }
                }
                System.out.println();
                if (i < n - 1) {
                    row += "\n";
                }
            }
            os = new FileOutputStream(new File(args[2]));
            os.write(row.getBytes(), 0, row.length());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}