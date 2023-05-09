package util;

import java.awt.*;

public class Cartesian {
    public double x;
    public double y;
    public Cartesian(double x, double y){
        this.x = x;
        this.y = y;
    }
    public void addX(double x) {
        this.x += x;
    }
    public void addY(double y) {
        this.y += y;
    }
    public void setLocation (double x, double y) {
        this.x = x;
        this.y = y;
    }
    public Point Point () {
        return new Point((int)x,(int)y);
    }
    @Override
    public String toString() {
        return "[x=" + x + ",y=" + y + "]";
    }
}
