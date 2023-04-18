package util;

public class PolarCoordinate {
    public double theta;
    public double radius;
    public PolarCoordinate (double theta, double radius){
        this.theta = theta;
        this.radius = radius;
    }
    public void addTheta(double delta) {
        this.theta = this.theta + delta;
    }
    public void addRadius(double delta) {
        this.radius = this.radius + delta;
    }
    public double getTheta() {
        return this.theta;
    }
    public double getRadius() {
        return this.radius;
    }
}
