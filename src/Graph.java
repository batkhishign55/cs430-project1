package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

class Graph {
    private int nodeCount;
    public String result = "";

    // list of linked lists for saving adjacency list
    private ArrayList<LinkedList<Integer>> adjacencyList = new ArrayList<>();

    public Graph(int nodeCount) {
        this.nodeCount = nodeCount;

        for (int i = 0; i < this.nodeCount; i++) {
            this.addVertex(i);
        }
    }

    void clearResult() {
        result = "";
    }

    // adds vertex to the graph
    void addVertex(int idx) {
        adjacencyList.add(new LinkedList<>());
    }

    // adds edge to the graph
    void addEdge(int idx, int w) {
        adjacencyList.get(idx).add(w);
    }

    // recursive DFS tool
    void Recurse(int vertex, ArrayList<Boolean> visited) {
        // mark the current vertex as visited
        // visited.get(vertex) = true;
        visited.set(vertex, true);
        // for saving the result
        result = result + vertex + "\n";

        // recurse over adjecant vertices
        Iterator<Integer> itr = adjacencyList.get(vertex).listIterator();
        while (itr.hasNext()) {
            int nextVertex = itr.next();
            System.out.println(String.format("reaching first %d", nextVertex));
            if (!visited.get(nextVertex)) {
                System.out.println(String.format("reaching unvisited %d", nextVertex));
                Recurse(nextVertex, visited);
            }
        }
        // for saving the result
        // result = result + vertex + "\n";
    }

    // recursive DFS
    void recursiveDFS() {
        // mark all vertices unvisited
        // boolean visited[] = new boolean[nodeCount];
        ArrayList<Boolean> visited = new ArrayList<Boolean>(nodeCount);
        for (int i = 0; i < nodeCount; i++) {
            visited.add(false);
        }

        // find all trees if graph is disconnected
        while (visited.contains(false)) {
            int vertex = visited.indexOf(false);
            Recurse(vertex, visited);
            result += '\n';
        }

        // print the result to the console
        System.out.println(result);
    }

    // iterative DFS
    void iterativeDFS(boolean preorder) {
        // mark all vertices unvisited
        ArrayList<Boolean> visited = new ArrayList<Boolean>(nodeCount);
        for (int i = 0; i < nodeCount; i++) {
            visited.add(false);
        }

        // empty stack
        Stack<Integer> stack = new Stack<>();

        // find all trees if graph is disconnected
        while (visited.contains(false)) {

            int vertex = visited.indexOf(false);
            // push the start vertex
            stack.push(vertex);
            while (!stack.empty()) {
                // pop the topmost vertex from stack
                vertex = stack.peek();
                if (preorder) {
                    stack.pop();
                }

                // for postorder
                boolean tail = true;

                // adding vertex to the result if unvisited
                if (!visited.get(vertex)) {
                    // to save the result
                    if (preorder) {
                        result = result + vertex + "\n";
                    }
                    // mark the current vertex as visited
                    visited.set(vertex, true);
                }

                // get all the adjacent vertex. Here we convert the linked list in descending
                // order to put the smallest indexed vertex at the top. This way we can achieve
                // "faithful" DFS.(same order as the recursive one)
                Iterator<Integer> itr = adjacencyList.get(vertex).descendingIterator();

                while (itr.hasNext()) {
                    int nextVertex = itr.next();
                    if (!visited.get(nextVertex)) {
                        tail = false;
                        stack.push(nextVertex);
                    }
                }

                if (!preorder) {
                    if (tail) {
                        result = result + vertex + "\n";
                        stack.pop();
                    }
                }
            }
        }
        // print the result to the console
        System.out.println(result);
    }

    public static void writeToFile(String fileName, String str) {

        Path path = Paths.get(fileName);
        byte[] strToBytes = str.getBytes();

        try {
            Files.write(path, strToBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Graph loadFromFile(String readFile) {

        // create new graph
        Graph graph = new Graph(0);

        // load data from file
        try {
            File myObj = new File(readFile);
            Scanner myReader = new Scanner(myObj);

            int idx = 0;
            int vertexCount = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] adjs = new String[0];

                if (idx == 0) {
                    graph = new Graph(Integer.parseInt(data));
                    idx += 1;
                    continue;
                } else if (idx == 1) {
                    vertexCount = Integer.parseInt(data);
                    idx += 1;
                    continue;
                }

                if (idx == vertexCount + 2) {
                    idx += 1;
                    continue;
                }

                if (!data.equals("")) {
                    adjs = data.split("\\s+");
                }
                // add edge to the graph
                graph.addEdge(Integer.parseInt(adjs[0]), Integer.parseInt(adjs[1]));

                System.out.println(data);
                idx += 1;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return graph;
    }

    // Driver Code
    public static void main(String args[]) {

        Graph graph = loadFromFile(args[0]);

        graph.iterativeDFS(true);
        // graph.clearResult();
        graph.iterativeDFS(false);
        writeToFile(args[0]+"output.txt", graph.result);
    }
}