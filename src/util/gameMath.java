package util;

public class gameMath {
    private static double getPolarX;
    private static double getPolarY;
    public void CartesianGetter(double getPolarX, double getPolarY) {
        gameMath.getPolarX = getPolarX;
        gameMath.getPolarY = getPolarY;
    }
    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    }
    public static PolarCoordinate Cartesian2Polar (double x, double y) {
        double theta = Math.tan((y-getPolarY)/(x-getPolarX));
        double radius = distance(x,y,getPolarX,getPolarY);
        return new PolarCoordinate(theta*180/Math.PI,radius);
    }

    public static CartesianCoordinate Polar2Cartesian (double theta, double radius) {
        double polarX = radius * Math.cos(theta*Math.PI/180);
        double polarY = radius * Math.sin(theta);
        return new CartesianCoordinate(polarX+getPolarX,polarY+getPolarY);
    }
}
