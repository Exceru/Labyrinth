import java.awt.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Graph g = new Graph(5);

        g.addEdge(2,4);
        g.addEdge(0, 1);
        g.addEdge(1, 3);
        g.addEdge(0, 2);
        g.addEdge(3, 2);
        g.addEdge(0, 2);
        g.addEdge(2, 3);
        g.addEdge(0, 4);

        Screen s = new Screen(g, new Point(0, 4));



        s.run();
    }
}