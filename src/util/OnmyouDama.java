package util;

public class OnmyouDama {
    public double x;
    public double y;
    public double OriginX;
    public double OriginY;
    public double radius = 50;
    public void setOrigin(Cartesian cartesian) {
        this.OriginX = cartesian.x;
        this.OriginY = cartesian.y;
    }
    public void RoundPosition(double theta) {
        this.x = radius * Math.cos(Math.toRadians(theta)) + OriginX;
        this.y = radius * Math.sin(Math.toRadians(theta)) + OriginY;
    }
}
