import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class Screen implements Runnable {

    private GraphPanel graphPanel;
    private final Graph g;
    private final Point startEnd;

    public Screen(Graph graph, Point startEnd){
        this.g = graph;
        this.startEnd = startEnd;
    }

    @Override
    public void run() {
        JFrame frame = new JFrame("Circle Paint Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        graphPanel = new GraphPanel(g, startEnd);
        mainPanel.add(graphPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton lineButton = new JButton("Redraw Graph");
        lineButton.addActionListener(event -> graphPanel.repaint());
        buttonPanel.add(lineButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        /*
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
         */

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);

        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

}