import java.util.List;
import java.util.Stack;

public class LabyrinthSolver {

    private Graph graph;
    private int startVertex;
    private int endVertex;
    private List<Stack<Integer>> paths;

    public LabyrinthSolver(Graph graph) {
        this.graph = graph;
    }

    public void printAllPaths(int startVertex, int endVertex) {
        this.startVertex = startVertex;
        this.endVertex = endVertex;
        Stack<Integer> path = new Stack<>();
        searchForVertex(startVertex, path);
    }

    private void searchForVertex(int vertex, Stack<Integer> visited) {

        // funktioniert noch nicht... später noch weiter bearbeiten

        // look if current vertex is searched vertex
        if (vertex == endVertex) {
            visited.push(vertex);
            paths.add(visited);
            return;
        }

        // look if circlefree and if not move on
        if (!visited.contains(vertex)) {
            visited.push(vertex);
            for (Integer neighbour : graph.getNeighbours(vertex)) {
                searchForVertex(neighbour, visited);
            }
        }
    }
}
