package test;

import java.awt.*;
import javax.swing.*;

public class StarTest extends JPanel {
    private static final int NUM_POINTS = 10;
    private static final int RADIUS = 50;
    private static final int INNER_RADIUS = 25;
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int[] xPoints = new int[NUM_POINTS * 2];
        int[] yPoints = new int[NUM_POINTS * 2];
        double angle = Math.PI / 2.0;
        double deltaAngle = 4.0 * Math.PI / NUM_POINTS;
        for (int i = 0; i < NUM_POINTS * 2; i++) {
            int radius = (i % 2 == 0) ? RADIUS : INNER_RADIUS;
            xPoints[i] = (int) (centerX + radius * Math.cos(angle));
            yPoints[i] = (int) (centerY - radius * Math.sin(angle));
            angle += deltaAngle;
        }
        g.setColor(Color.RED);
        g.fillPolygon(xPoints, yPoints, NUM_POINTS * 2);
        g.setColor(Color.BLACK);
        g.drawPolygon(xPoints, yPoints, NUM_POINTS * 2);
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Star");
        frame.add(new StarTest());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

