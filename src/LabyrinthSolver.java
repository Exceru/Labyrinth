import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * This class provides the methods to solve a labyrinth with a given graph of it.
 */
public class LabyrinthSolver {

    /**
     * Graph which contains the lgic behind the labyrinth.
     */
    private final Graph graph;

    /**
     * Vertex, where the search starts.
     */
    private int startVertex;

    /**
     * Vertex, where the search ends.
     */
    private int endVertex;

    /**
     * List of all the paths leading from start to end.
     */
    private List<Stack<Integer>> paths;

    /**
     * Constructor for the class to solve a labyrinth.
     * @param graph Graph containing the logic behind the labyrinth.
     */
    public LabyrinthSolver(Graph graph) {
        paths = new ArrayList<>();
        this.graph = graph;
    }

    /**
     * Prints all possible paths from start to end in the labyrinth.
     * @param startVertex Vertex, where the search starts.
     * @param endVertex Vertex, where the search ends.
     */
    public void printAllPaths(int startVertex, int endVertex) {
        this.startVertex = startVertex;
        this.endVertex = endVertex;
        Stack<Integer> path = new Stack<>();
        searchForVertex(startVertex, path);

        for (Stack<Integer> visited : paths) {
            if (!visited.isEmpty()) {
                System.out.println(visited);
            }
        }
    }

    /**
     * This method checks the current vertex and his neighbours for his relevance to solve the labyrinth.
     * @param vertex Currently checked vertex.
     * @param visited Stack with all vertices, already visited.
     */
    private void searchForVertex(int vertex, Stack<Integer> visited) {

        if (!visited.contains(vertex)) {
            visited.push(vertex);
            if (vertex == endVertex) {
                System.out.println(visited);
                paths.add(visited);
            } else {
                for (Integer neighbour : graph.getNeighbours(vertex)) {
                    searchForVertex(neighbour, visited);
                }
            }
            visited.pop();
        }
    }
}
