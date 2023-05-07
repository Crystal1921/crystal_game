package util;

public class entity {
    public final double health;
    public double hurt;
    public int emitterSpeed;
    public entity (double health,int emitterSpeed) {
        this.health = health;
        this.emitterSpeed = emitterSpeed;
    }
    public void addHurt () {
        hurt ++;
    }
    public double proportion () {
        return 1 - this.hurt / this.health;
    }
}
