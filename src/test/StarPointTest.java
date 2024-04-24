package test;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
public class StarPointTest extends JPanel {
    private final ArrayList<Point> five_star = new ArrayList<>();
    private static final int NUM_POINTS = 5;
    private static final int RADIUS = 50;
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        double angle = Math.PI / 2.0;
        double deltaAngle = 4.0 * Math.PI / NUM_POINTS;
        double x1 = RADIUS * Math.cos(angle);
        double y1 = - RADIUS * Math.sin(angle);
        double x2 = RADIUS * Math.cos(angle + deltaAngle);
        double y2 = - RADIUS * Math.sin(angle + deltaAngle);
        ArrayList<DoublePoint> points = getEquallySpacedPoints(new DoublePoint(x1,y1), new DoublePoint(x2,y2), 15);
        for (int i = 0; i < NUM_POINTS; i++) {
            for (DoublePoint point : points) {
                double x = point.x;
                double y = point.y;
                double theta = Math.toRadians(216 * i);
                five_star.add(new Point((int) (x * Math.cos(theta) - y * Math.sin(theta)), (int) (x * Math.sin(theta) + y * Math.cos(theta))));
            }
        }
        five_star.forEach((p) -> g.drawLine(p.x + getWidth() / 2,p.y + getWidth() / 2,p.x + getWidth() / 2,p.y + getWidth() / 2));
        g.setColor(Color.BLACK);
    }
    public static ArrayList<DoublePoint> getEquallySpacedPoints(DoublePoint point1, DoublePoint point2, int numPoints) {
        ArrayList<DoublePoint> points = new ArrayList<>();
        double deltaX = (point2.x - point1.x) / (numPoints + 1.0);
        double deltaY = (point2.y - point1.y) / (numPoints + 1.0);
        for (int i = 1; i <= numPoints; i++) {
            int x = (int) (point1.x + i * deltaX);
            int y = (int) (point1.y + i * deltaY);
            points.add(new DoublePoint(x, y));
        }
        return points;
    }
    public static class DoublePoint {
        protected double x;
        protected double y;
        protected DoublePoint (double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Star");
        frame.add(new StarPointTest());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

