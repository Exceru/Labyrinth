import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class LabyrinthSolver {

    private final Graph graph;
    private int startVertex;
    private int endVertex;
    private List<Stack<Integer>> paths;

    public LabyrinthSolver(Graph graph) {
        paths = new ArrayList<>();
        this.graph = graph;
    }

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
