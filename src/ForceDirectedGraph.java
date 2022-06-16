import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class ForceDirectedGraph {
    private static final double  TOTAL_KINETIC_ENERGY_DEFAULT = 50000000;
    public static final double SPRING_CONSTANT_DEFAULT       = 0.1;
    public static final double COULOMB_CONSTANT_DEFAULT      = 500.0;
    public static final double DAMPING_COEFFICIENT_DEFAULT   = 0.2;
    public static final double TIME_STEP_DEFAULT             = 1.0;

    private final ArrayList<Node> nodes;
    private double totalKineticEnergy;
    private double springConstant;
    private double coulombConstant;
    private double dampingCoefficient;
    private double timeStep;

    private Dimension windowSize;


    private  double diameterSize;

    private Node dummyCenterNode; //for pulling the glaph to center

    public ForceDirectedGraph(){
        this.nodes = new ArrayList<Node>();
        this.totalKineticEnergy = TOTAL_KINETIC_ENERGY_DEFAULT;
        this.springConstant = SPRING_CONSTANT_DEFAULT;
        this.coulombConstant = COULOMB_CONSTANT_DEFAULT;
        this.dampingCoefficient = DAMPING_COEFFICIENT_DEFAULT;
        this.timeStep = TIME_STEP_DEFAULT;
        this.diameterSize = 10.0;

        this.windowSize = new Dimension(1800, 1000);

        this.dummyCenterNode = new Node(-1, 6.0);
    }

    public void add(Node node){
        this.nodes.add(node);
    }

    public void addEdge(int id1, int id2, double naturalSpringLength){
        Node node1 = this.getNodeWith(id1);
        Node node2 = this.getNodeWith(id2);
        node1.add(node2, naturalSpringLength);
        node2.add(node1, naturalSpringLength);
    }
    private Node getNodeWith(int id){
        Node node = null;
        for(int i = 0; i < this.nodes.size(); i++){
            Node target = this.nodes.get(i);
            if(target.getID() == id){
                node = target;
                break;
            }
        }
        return node;
    }

    public void initializeNodeLocations(){
        double maxMass = 0.0f;
        for(int i = 0; i < this.nodes.size(); i++){
            double mass = this.nodes.get(i).getMass();
            if(mass > maxMass)
                maxMass = mass;
        }

        for(int i = 0; i < this.nodes.size(); i++){
            Node node = this.nodes.get(i);
            node.setForceToApply(0.0,0.0);
            node.setVelocities(0.0,0.0);
            double x = ThreadLocalRandom.current().nextInt((int) diameterSize, windowSize.width);
            double y = ThreadLocalRandom.current().nextInt((int) diameterSize, windowSize.height);
            double d = node.getMass() * diameterSize;
            node.set(x, y, d);
        }
    }

    public void draw(Graphics2D g){
        this.totalKineticEnergy = this.calculateTotalKineticEnergy();
        
        this.drawEdges(g);
        for(int i = 0; i < this.nodes.size(); i++)
            this.nodes.get(i).draw(g);
    }

    private void drawEdges(Graphics2D g){
        for(int i = 0; i < this.nodes.size(); i++){
            Node node1 = this.nodes.get(i);
            for(int j = 0; j < node1.getSizeOfAdjacents(); j++){
                Node node2 = node1.getAdjacentAt(j);
                g.drawLine((int) node1.getX(), (int) node1.getY(), (int) node2.getX(), (int) node2.getY());
            }
        }
    }

    private double calculateTotalKineticEnergy(){ //ToDo:check the calculation in terms of Math...
        for(int i = 0; i < this.nodes.size(); i++){
            Node target = this.nodes.get(i);

            double forceX = 0.0;
            double forceY = 0.0;
            for(int j = 0; j < this.nodes.size(); j++){ //Coulomb's law
                Node node = this.nodes.get(j);
                if(node != target){
                    double dx = target.getX() - node.getX();
                    double dy = target.getY() - node.getY();
                    double distance = sqrt(dx * dx + dy * dy);
                    double xUnit = dx / distance;
                    double yUnit = dy / distance;

                    double coulombForceX = this.coulombConstant * (target.getMass() * node.getMass()) / pow(distance, 2.0) * xUnit;
                    double coulombForceY = this.coulombConstant * (target.getMass() * node.getMass()) / pow(distance, 2.0) * yUnit;

                    forceX += coulombForceX;
                    forceY += coulombForceY;
                }
            }

            for(int j = 0; j < target.getSizeOfAdjacents(); j++){ // Hooke's law
                Node node = target.getAdjacentAt(j);
                double springLength = target.getNaturalSpringLengthAt(j);
                double dx = target.getX() - node.getX();
                double dy = target.getY() - node.getY();
                double distance = sqrt(dx * dx + dy * dy);
                double xUnit = dx / distance;
                double yUnit = dy / distance;

                double d = distance - springLength;

                double springForceX = -1 * this.springConstant * d * xUnit;
                double springForceY = -1 * this.springConstant * d * yUnit;

                forceX += springForceX;
                forceY += springForceY;
            }

            target.setForceToApply(forceX, forceY);
        }

        double totalKineticEnergy = 0.0;
        for(int i = 0; i < this.nodes.size(); i++){
            Node target = this.nodes.get(i);

            double forceX = target.getForceX();
            double forceY = target.getForceY();

            double accelerationX = forceX / target.getMass();
            double accelerationY = forceY / target.getMass();

            double velocityX = (target.getVelocityX() + this.timeStep * accelerationX) * this.dampingCoefficient;
            double velocityY = (target.getVelocityY() + this.timeStep * accelerationY) * this.dampingCoefficient;

            double x = target.getX() + this.timeStep * target.getVelocityX() + accelerationX * pow(this.timeStep, 2.0) / 2.0;
            double y = target.getY() + this.timeStep * target.getVelocityY() + accelerationY * pow(this.timeStep, 2.0) / 2.0;



            // Window collision handling
            double radius = target.getDiameter();
            if(x < radius)
                x = radius;
            else if(x > this.windowSize.width - radius)
                x = this.windowSize.width - radius;
            if(y < radius)
                y = radius;
            else if(y > this.windowSize.height - radius)
                y = this.windowSize.height - radius;

            target.set(x, y);
            target.setVelocities(velocityX, velocityY);
            target.setForceToApply(0.0, 0.0);

            totalKineticEnergy += target.getMass() * sqrt(velocityX * velocityX + velocityY * velocityY) / 2.0;
        }
        return totalKineticEnergy;
    }



    public double getSpringConstant() {
        return springConstant;
    }

    public void setSpringConstant(double springConstant) {
        this.springConstant = springConstant;
    }

    public double getCoulombConstant() {
        return coulombConstant;
    }

    public void setCoulombConstant(double coulombConstant) {
        this.coulombConstant = coulombConstant;
    }

    public double getDampingCoefficient() {
        return dampingCoefficient;
    }

    public void setDampingCoefficient(double dampingCoefficient) {
        this.dampingCoefficient = dampingCoefficient;
    }

    public double getTimeStep() {
        return timeStep;
    }

    public void setTimeStep(double timeStep) {
        this.timeStep = timeStep;
    }

    public double getDiameterSize() {
        return diameterSize;
    }

    public void setDiameterSize(double diameterSize) {
        this.diameterSize = diameterSize;
    }


    public void setWindowSize(Dimension windowSize) {
        this.windowSize = windowSize;
    }
}
