import java.awt.*;
import java.util.ArrayList;

public class Node {
    private double mass;
    private ArrayList<Node> adjacents;
    private ArrayList<Double> naturalSpringLengths;

    private double x;
    private double y;
    private double velocityX;
    private double velocityY;
    private double forceX;
    private double forceY;
    private int id;
    private double diameter;

    public Node(int id, double mass){
        this.id = id;
        this.mass = mass;
        this.adjacents = new ArrayList<Node>();
        this.naturalSpringLengths = new ArrayList<Double>();

        this.set(-1.0, -1.0, -1.0); //ad-hoc
        this.setVelocities(0.0, 0.0);
        this.setForceToApply(0.0, 0.0);
    }

    public void add(Node adjacent, double naturalSpringLength){
        this.adjacents.add(adjacent);                       //the order of elements in the two ArrayLists must be the same.
        this.naturalSpringLengths.add(naturalSpringLength); //better to capture these as like key-value pairs...
    }
    public void set(double x, double y){
        this.x = x;
        this.y = y;
    }
    public void set(double x, double y, double diameter){
        this.set(x, y);
        this.diameter = diameter;
    }
    public void setVelocities(double velocityX, double velocityY){
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }
    public void setForceToApply(double forceX, double forceY){
        this.forceX = forceX;
        this.forceY = forceY;
    }

    public int getID(){
        return this.id;
    }
    public double getMass(){
        return this.mass;
    }
    public double getX(){
        return this.x;
    }
    public double getY(){
        return this.y;
    }

    public double getVelocityX(){
        return this.velocityX;
    }
    public double getVelocityY(){
        return this.velocityY;
    }
    public double getForceX(){
        return this.forceX;
    }
    public double getForceY(){
        return this.forceY;
    }
    public int getSizeOfAdjacents(){
        return this.adjacents.size();
    }
    public Node getAdjacentAt(int index){
        return this.adjacents.get(index);
    }
    public double getNaturalSpringLengthAt(int index){
        return this.naturalSpringLengths.get(index);
    }

    public double getDiameter() {
        return diameter;
    }

    public void draw(Graphics2D g){
        g.setColor(Color.WHITE);
        g.fillOval((int) (this.x - (diameter / 2)), (int) (this.y - (diameter / 2)), (int) diameter, (int) diameter);

        g.setColor(Color.BLACK);
        g.drawOval((int) (this.x - (diameter / 2)), (int) (this.y - (diameter / 2)), (int) diameter, (int) diameter);

        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(id), (int) (this.x - 5), (int) (this.y + 5));
    }

    //@Override
    public String toString(){
        String adjacentIDsAndNaturalLengths = "[";
        for(int i = 0; i < this.adjacents.size(); i++)
            adjacentIDsAndNaturalLengths += this.adjacents.get(i).getID() + "(" + this.naturalSpringLengths.get(i) + "),";
        adjacentIDsAndNaturalLengths += "]";
        return "ID:" + this.id +
                ",MASS:" + this.mass +
                ",ADJACENTS(NATURAL_LEGTH):" + adjacentIDsAndNaturalLengths +
                ",X:" + this.x +
                ",Y:" + this.y +
                ",DIAMETER:" + this.diameter;

    }
}
