package util;

public class entity {
    public final double health;
    public double hurt;
    public int emitterSpeed;
    public Cartesian position;
    public entity (double health, int emitterSpeed) {
        this.health = health;
        this.emitterSpeed = emitterSpeed;
    }
    public entity (double health, int emitterSpeed, Cartesian position) {
        this.health = health;
        this.emitterSpeed = emitterSpeed;
        this.position = position;
    }
    public void addHurt () {
        hurt ++;
    }
    public Cartesian getPosition () {return position;}
    public double proportion () {
        return 1 - this.hurt / this.health;
    }
}
