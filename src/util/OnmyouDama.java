package util;

public class OnmyouDama  extends entity{
    public double x;
    public double y;
    public double OriginX;
    public double OriginY;
    public double radius = 60;

    public OnmyouDama(double health, int emitterSpeed) {
        super(health, emitterSpeed);
    }

    public void setOrigin(Cartesian cartesian) {
        this.OriginX = cartesian.x;
        this.OriginY = cartesian.y;
    }

    public void RoundPosition(double theta) {
        this.x = radius * Math.cos(Math.toRadians(theta)) + OriginX;
        this.y = radius * Math.sin(Math.toRadians(theta)) + OriginY;
    }
    public Cartesian getPosition () {
        return new Cartesian(x,y);
    }
    public String toPosition () {
        return "x:" + this.x + ",y:" + this.y;
    }
}
