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
    private JPanel test;
    private JCheckBox checkBox1;
    private JCheckBox checkBox2;

    public Screen(Graph graph, Point startEnd){

        this.g = graph;
        this.startEnd = startEnd;
    }

    @Override
    public void run() {
        JFrame frame = new JFrame("Circle Paint Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /*JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());


*/

        graphPanel = new GraphPanel(g, startEnd);



        frame.add(test, BorderLayout.SOUTH);
        graphPanel.setLayout(new GridLayout(0, 1));
        frame.add(graphPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JSlider slider = new JSlider(10,100,20);
        JButton lineButton = new JButton("Redraw Graph");
        lineButton.addActionListener(event -> graphPanel.repaint());
        buttonPanel.add(lineButton);
        buttonPanel.add(slider);

        //mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        //frame.add(mainPanel);

        //frame.setContentPane(test);

        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);


        while(true) {
            graphPanel.repaint();

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

}