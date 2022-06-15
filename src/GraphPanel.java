import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

public class GraphPanel extends JPanel {

    @Serial
    private static final long serialVersionUID = 6481890334304291711L;

    private final Point startEnd;
    private Graph graph;
    //private final List<Point> drawnPoints;
    private final Point windowSize;

    private Map<Integer, Point> drawnPoints;

    public GraphPanel(Graph graph, Point startEnd) {
        //this.drawnPoints = new ArrayList<Point>();
        this.startEnd = startEnd;
        this.graph = graph;
        //this.drawnPoints = new ArrayList<>(this.graph.getNumVertices());
        this.drawnPoints = new TreeMap<>();

        this.windowSize = new Point(1800, 1000);

        this.setPreferredSize(new Dimension(windowSize.x,windowSize.y));
    }

    // TODO: Maybe unnecessary?
    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    // TODO: Force directed graph solution is definitely needed! RANDOM LOCATIONS NOT READABLE.
    private Point calculateVertexPosition(int currentVertex) {
        // TODO Calculate according to window size
        int diagonal = (int) Math.sqrt(Math.pow(windowSize.x, 2) + Math.pow(windowSize.y, 2));
        int length = diagonal / graph.getNumVertices();

        length = 200;
        int gridWidth = 80;


        int x;
        int y;

        do {
            // TODO Seeded approach would be better
            double angle = Math.toRadians(ThreadLocalRandom.current().nextInt(-90, 180 + 1));
            //x = (drawnPoints.get(currentVertex - 1).x + (int)(length * Math.sin(angle)));
            //y = (drawnPoints.get(currentVertex - 1).y + (int)(length * Math.cos(angle)));

            x = ThreadLocalRandom.current().nextInt(40, 1800-40);
            y = ThreadLocalRandom.current().nextInt(40, 1000-40);



            x = Math.round(x / gridWidth) * gridWidth;
            y = Math.round(y / gridWidth) * gridWidth;





        } while(x > windowSize.x - 40 || x < 40 || y > windowSize.y - 40 || y < 40 || drawnPoints.containsValue(new Point(x, y)));


        //x = x + ThreadLocalRandom.current().nextInt(-20, 20 + 1);
        //y = y + ThreadLocalRandom.current().nextInt(-20, 20 + 1);


        return new Point(x,y);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawnPoints = new TreeMap<>();

        g.setColor(Color.BLACK);

        // To allow for changing the stroke size or using anti aliasing, we need to use a graphics 2d component.
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(1));

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setFont(new Font(g2.getFont().getFontName(), Font.PLAIN, 16));

        drawnPoints.put(0, new Point(50, 50));
        g2.setColor(Color.BLACK);
        g2.drawString(String.valueOf(1), 50, 50);


        // Calculate and place all vertex position.
        for(int i = 1; i < this.graph.getNumVertices(); i++) {
            List<Integer> neighbours = this.graph.getNeighbours(i);

            // Get last drawn point
            Point p = calculateVertexPosition(i);
            drawnPoints.put(i, p);
        }

        // First draw the edges of the vertices.
        for(int i = 0; i < this.graph.getNumVertices(); i++) {
            List<Integer> neighbours = this.graph.getNeighbours(i);

            for(int j = 0; j < neighbours.size(); j++) {

                // Do not draw edges to vertex itself...
                if(i == j) {
                    continue;
                }

                int neighbour = neighbours.get(j);

                Point vertexPosition = drawnPoints.get(i);
                Point neighbourPosition = drawnPoints.get(neighbour);

                g2.drawLine(vertexPosition.x, vertexPosition.y, neighbourPosition.x, neighbourPosition.y);
            }
        }

        int diagonal = (int) Math.sqrt(Math.pow(windowSize.x, 2) + Math.pow(windowSize.y, 2));
        int vertexSize = Math.min(diagonal / getGraph().getNumVertices() + 20, 100);

        // Then draw the vertices themselves.
        for (int i = 0; i < this.graph.getNumVertices(); i++) {
            Point vertexPosition = drawnPoints.get(i);

            g2.setColor(Color.WHITE);
            g2.fillOval(vertexPosition.x - (vertexSize / 2), vertexPosition.y - (vertexSize / 2), vertexSize, vertexSize);

            g2.setColor(Color.BLACK);
            g2.drawOval(vertexPosition.x - (vertexSize / 2), vertexPosition.y - (vertexSize / 2), vertexSize, vertexSize);

            g2.setColor(Color.BLACK);
            g2.drawString(String.valueOf(i+1), vertexPosition.x - 5, vertexPosition.y + 5);
        }

    }
}