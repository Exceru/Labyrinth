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

    ForceDirectedGraph fdg;

    private final Point startEnd;
    private Graph graph;
    //private final List<Point> drawnPoints;
    private final Point windowSize;
    private ArrayList<Double> info;

    private Map<Integer, Point> drawnPoints;

    public GraphPanel(Graph graph, Point startEnd) {
        //this.drawnPoints = new ArrayList<Point>();
        this.startEnd = startEnd;
        this.graph = graph;
        //this.drawnPoints = new ArrayList<>(this.graph.getNumVertices());
        this.drawnPoints = new TreeMap<>();

        this.windowSize = new Point(1800, 1000);

        this.setPreferredSize(new Dimension(windowSize.x,windowSize.y));



        fdg = new ForceDirectedGraph();

        for(int i = 0; i < graph.getNumVertices(); i++) {
            double mass = graph.getNeighbours(i).size();

            fdg.add(new Node(i + 1, mass));
        }

        for(int i = 0; i < graph.getNumVertices(); i++) {

            for(int j = 0; j < graph.getNeighbours(i).size(); j++) {
                fdg.addEdge(i + 1, graph.getNeighbours(i).get(j) + 1, 10.0);
            }
        }

        fdg.initializeNodeLocations();
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
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

        fdg.setSpringConstant(info.get(0));
        fdg.setCoulombConstant(info.get(1));
        fdg.setDampingCoefficient(info.get(2));
        fdg.setWindowSize(this.getSize());
        int diagonal = (int) Math.sqrt(Math.pow(this.getSize().width, 2) + Math.pow(this.getSize().height, 2));


        //fdg.setDiameterSize((diagonal / 2060) * 20);

        fdg.draw(g2);
    }

    public void reInitLocations() {
        fdg.initializeNodeLocations();
    }

    public ArrayList<Double> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<Double> info) {
        this.info = info;
    }
}