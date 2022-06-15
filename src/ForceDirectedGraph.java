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

    private ArrayList<Node> nodes;
    private double totalKineticEnergy;
    private double springConstant;
    private double coulombConstant;
    private double dampingCoefficient;
    private double timeStep;

    private Node lockedNode;
    private Node dummyCenterNode; //for pulling the glaph to center

    public ForceDirectedGraph(){
        this.nodes = new ArrayList<Node>();
        this.totalKineticEnergy = TOTAL_KINETIC_ENERGY_DEFAULT;
        this.springConstant = SPRING_CONSTANT_DEFAULT;
        this.coulombConstant = COULOMB_CONSTANT_DEFAULT;
        this.dampingCoefficient = DAMPING_COEFFICIENT_DEFAULT;
        this.timeStep = TIME_STEP_DEFAULT;

        this.lockedNode = null;
        this.dummyCenterNode = new Node(-1, 1.0f);
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
        /*double nodeSizeRatio;
        if(this.getWidth() < this.getHeight())
            nodeSizeRatio = this.getWidth() / (maxMass * 5.0f); //ad-hoc
        else
            nodeSizeRatio = this.getHeight() / (maxMass * 5.0f); //ad-hoc
        double offset = nodeSizeRatio * maxMass;
        double minXBound = this.getX() + offset;
        double maxXBound = this.getX() + this.getWidth() - offset;
        double minYBound = this.getY() + offset;
        double maxYBound = this.getY() + this.getHeight() - offset;*/
        for(int i = 0; i < this.nodes.size(); i++){
            Node node = this.nodes.get(i);
            double x = ThreadLocalRandom.current().nextInt(40, 1800-40);
            double y = ThreadLocalRandom.current().nextInt(40, 1000-40);
            double d = node.getMass() * 10.0;
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
            if(target == this.lockedNode)
                continue;

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

                    double coulombForceX = this.coulombConstant * (target.getMass() * node.getMass()) / pow(distance, 2.0f) * xUnit;
                    double coulombForceY = this.coulombConstant * (target.getMass() * node.getMass()) / pow(distance, 2.0f) * yUnit;

                    forceX += coulombForceX;
                    forceY += coulombForceY;
                }
            }

            for(int j = 0; j < target.getSizeOfAdjacents(); j++){ //Hooke's law
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

        double totalKineticEnergy = 0.0f;
        for(int i = 0; i < this.nodes.size(); i++){
            Node target = this.nodes.get(i);
            if(target == this.lockedNode)
                continue;

            double forceX = target.getForceX();
            double forceY = target.getForceY();

            double accelerationX = forceX / target.getMass();
            double accelerationY = forceY / target.getMass();

            double velocityX = (target.getVelocityX() + this.timeStep * accelerationX) * this.dampingCoefficient;
            double velocityY = (target.getVelocityY() + this.timeStep * accelerationY) * this.dampingCoefficient;

            double x = target.getX() + this.timeStep * target.getVelocityX() + accelerationX * pow(this.timeStep, 2.0f) / 2.0f;
            double y = target.getY() + this.timeStep * target.getVelocityY() + accelerationY * pow(this.timeStep, 2.0f) / 2.0f;

            double radius = target.getDiameter() / 2.0f; //for boundary check
            /*if(x < this.getX() + radius)
                x = this.getX() + radius;
            else if(x > this.getX() + this.getWidth() - radius)
                x =  this.getX() + this.getWidth() - radius;
            if(y < this.getY() + radius)
                y = this.getY() + radius;
            else if(y > this.getY() + this.getHeight() - radius)
                y =  this.getX() + this.getHeight() - radius;*/

            target.set(x, y);
            target.setVelocities(velocityX, velocityY);
            target.setForceToApply(0.0f, 0.0f);

            totalKineticEnergy += target.getMass() * sqrt(velocityX * velocityX + velocityY * velocityY) / 2.0f;
        }
        return totalKineticEnergy;
    }

}
