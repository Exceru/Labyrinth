import java.awt.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        GraphLoader loader = new GraphLoader("labyrinth-1.graph");
        Graph g = loader.getGraph();

        Screen s = new Screen(g, new Point(0, 131));




        s.run();
    }
}