package test;

import java.awt.*;
import javax.swing.*;

public class StarTest extends JPanel {
    private static final int NUM_POINTS = 5;
    private static final int RADIUS = 50;
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
            xPoints[i] = (int) (centerX + RADIUS * Math.cos(angle));
            yPoints[i] = (int) (centerY - RADIUS * Math.sin(angle));
            angle += deltaAngle;
        }
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

