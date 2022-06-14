import java.util.ArrayList;
import java.util.List;

/**
 * Class for a graph data structure using an adjacency list.
 * Edges of vertices will be stored undirected.
 */
public class Graph {
    private final int numVertices;
    private final List<List<Integer>> adjList;

    /**
     *
     * @param numberOfVertices Number of vertices in the graph.
     */
    public Graph(int numberOfVertices){
        adjList = new ArrayList<>();
        this.numVertices = numberOfVertices;

        for(int i = 0; i < numberOfVertices; i++) {
            ArrayList<Integer> neighbours = new ArrayList<>();
            adjList.add(neighbours);
        }
    }

    /**
     * Will insert an undirected edge into the adjacency list of this class.
     * @param u First vertex of the edge
     * @param v Second vertex of the edge
     */
    public void addEdge(int u, int v){
        if(adjList.get(u) != null || adjList.get(v) != null){
            // Only add vertex as neighbour if it isn't already a neighbour.
            if(!adjList.get(u).contains(v)) {
                adjList.get(u).add(v);
            }

            // Graph is not directional, so add the vertex to the other vertex as a neighbour as well.
            if(!adjList.get(v).contains(u)) {
                adjList.get(v).add(u);
            }

        } else {
            throw new ArrayIndexOutOfBoundsException("Vertex does not exist in adjacency list.");
        }
    }

    /**
     * Returns a list of neighbours from a specific vertex.
     * Will include the vertex itself as well, as the graph is not directional.
     * @param vertex Vertex to get the neighbours from.
     * @return The neighbours of the vertex as a list.
     */
    public List<Integer> getNeighbours(int vertex) {
        return adjList.get(vertex);
    }

    /**
     * Returns the number of vertices in the graph.
     * @return Number of vertices in the graph.
     */
    public int getNumVertices(){
        return numVertices;
    }


}
