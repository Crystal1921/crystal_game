package util;

public class gameMath {
    private static double polarX;
    private static double polarY;

    public static void setPolar(double polarX, double polarY) {
        gameMath.polarX = polarX;
        gameMath.polarY = polarY;
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public static PolarCoordinate Cartesian2Polar(double x, double y) {
        double deltaX = x - polarX;
        double deltaY = y - polarY;
        double radius = distance(x, y, polarX, polarY);
        double theta = Math.toDegrees(Math.atan2(deltaY, deltaX));
        return new PolarCoordinate(theta, radius);
    }

    public static CartesianCoordinate Polar2Cartesian(double theta, double radius) {
        double x = radius * Math.cos(Math.toRadians(theta)) + polarX;
        double y = radius * Math.sin(Math.toRadians(theta)) + polarY;
        return new CartesianCoordinate(x, y);
    }
}
