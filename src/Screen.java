
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Screen implements Runnable {

    private JFrame frame;

    private PaintPanel paintPanel;

    @Override
    public void run() {
        frame = new JFrame("Circle Paint Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        paintPanel = new PaintPanel();
        mainPanel.add(paintPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton lineButton = new JButton("Draw Lines");
        lineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (paintPanel.isComplete()) {
                    paintPanel.setDrawLines(true);
                    paintPanel.repaint();
                }
            }
        });
        buttonPanel.add(lineButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);

        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    /*public static void main(String[] args) {
        SwingUtilities.invokeLater(new Screen());
    }*/

    public class PaintPanel extends JPanel {

        private static final long serialVersionUID = 6481890334304291711L;

        private final Color[] colors = { Color.RED, Color.GREEN, Color.BLUE,
                Color.ORANGE, Color.CYAN, Color.YELLOW };

        private boolean drawLines;

        private final int pointLimit;

        private final List<Point> points;

        public PaintPanel() {
            this.points = new ArrayList<Point>();
            this.pointLimit = 3;
            this.drawLines = false;

            this.addMouseListener(new CircleMouseListener());
            this.setPreferredSize(new Dimension(400, 400));
        }

        public void setDrawLines(boolean drawLines) {
            this.drawLines = drawLines;
        }

        void drawArrow(Graphics g1, double x1, double y1, double x2, double y2 ) {
            Graphics2D ga = (Graphics2D) g1.create();
            ga.drawLine((int)x1, (int)y1, (int)x2, (int)y2);

            double l = Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));//  line length
            double d = l / 10; // arrowhead distance from end of line. you can use your own value.

            double newX = ((x2 + (((x1 - x2) / (l) * d)))); // new x of arrowhead position on the line with d distance from end of the line.
            double newY = ((y2 + (((y1 - y2) / (l) * d)))); // new y of arrowhead position on the line with d distance from end of the line.

            double dx = x2 - x1, dy = y2 - y1;
            double angle = (Math.atan2(dy, dx)); //get angle (Radians) between ours line and x vectors line. (counter clockwise)
            angle = (-1) * Math.toDegrees(angle);// cconvert to degree and reverse it to round clock for better understand what we need to do.
            if (angle < 0) {
                angle = 360 + angle; // convert negative degrees to posative degree
            }
            angle = (-1) * angle; // convert to counter clockwise mode
            angle = Math.toRadians(angle);//  convert Degree to Radians
            AffineTransform at = new AffineTransform();
            at.translate(newX, newY);// transport cursor to draw arrowhead position.
            at.rotate(angle);
            ga.transform(at);

            Polygon arrowHead = new Polygon();
            arrowHead.addPoint(5, 0);
            arrowHead.addPoint(-5, 5);
            arrowHead.addPoint(-2, -0);
            arrowHead.addPoint(-5, -5);
            ga.fill(arrowHead);
            ga.drawPolygon(arrowHead);
        }

        public boolean isComplete() {
            return points.size() >= pointLimit;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Point pp = null;
            Point p0 = null;

            for (int i = 0; i < points.size(); i++) {
                g.setColor(colors[i]);

                Point p = points.get(i);
                g.fillOval(p.x - 20, p.y - 20, 40, 40);

                pp = p;
            }

            if (drawLines && (points.size() > 1)) {
                p0 = points.get(0);
                pp = p0;
                g.setColor(Color.BLACK);
                for (int i = 1; i < points.size(); i++) {
                    Point p = points.get(i);
                    drawArrow(g, pp.x, pp.y, p.x, p.y);
                    //g.drawLine(pp.x, pp.y, p.x, p.y);
                    pp = p;
                }
                drawArrow(g, pp.x, pp.y, p0.x, p0.y);
                //g.drawLine(pp.x, pp.y, p0.x, p0.y);
            }
        }

        public class CircleMouseListener extends MouseAdapter {

            @Override
            public void mousePressed(MouseEvent event) {
                if (points.size() < pointLimit) {
                    points.add(event.getPoint());
                    PaintPanel.this.repaint();
                }
            }
        }
    }

}