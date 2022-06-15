import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GraphLoader {
    private String name;

    public GraphLoader(String name){
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Graph getGraph(){
        List<Point> points = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(this.name))) {
            for(String line; (line = br.readLine()) != null; ) {

                String coords[] = line.split(" ");
                points.add(new Point(Integer.parseInt(coords[0]), Integer.parseInt(coords[1])));
                System.out.println(points.get(points.size() - 1));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int numVertices = 0;
        for(Point p : points) {
            if(p.x > numVertices) {
                numVertices = p.x;
            } else if (p.y > numVertices) {
                numVertices = p.y;
            }
        }

        Graph g = new Graph(numVertices);

        for(Point p : points) {
            g.addEdge(p.x - 1, p.y - 1);
        }

        return g;
    }

}
