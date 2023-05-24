package util;

public class PolarCoordinate {
    public double theta;
    public double radius;
    public PolarCoordinate (double theta, double radius){
        this.theta = theta;
        this.radius = radius;
    }
    public PolarCoordinate addRadius(double radius) {
        this.radius += radius;
        return this;
    }
    @Override
    public String toString () {
        return "[theta=" + theta + ",radius=" + radius + "]";
    }
}
