import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Screen implements Runnable {

    private GraphPanel graphPanel;
    private final Graph g;
    private final Point startEnd;
    private JPanel test;
    private JSlider slider1;
    private JSlider slider2;
    private JSlider slider3;
    private JButton reInitLocationsButton;
    private ArrayList<Double> info;

    public Screen(Graph graph, Point startEnd){

        this.g = graph;
        this.startEnd = startEnd;
        info = new ArrayList<>();

        this.info.add(0.25);
        this.info.add(1000.0);
        this.info.add(0.15);

        slider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                info.set(0, (double)(slider1.getValue()) * (.005));
            }
        });
        slider2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                info.set(1, (double)(slider2.getValue()) * (20.0));
            }
        });
        slider3.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                info.set(2, (double)(slider3.getValue() * (0.003)));
            }
        });
        reInitLocationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graphPanel.reInitLocations();
            }
        });
    }

    @Override
    public void run() {
        JFrame frame = new JFrame("Circle Paint Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /*JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());


*/

        graphPanel = new GraphPanel(g, startEnd);

        graphPanel.setInfo(info);

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