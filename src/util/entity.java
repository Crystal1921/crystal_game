package util;

public class entity {
    public double health;
    public double hurt;
    public entity (double health) {
        this.health = health;
    }
    public void addHurt (int add) {
        hurt += add;
    }
    public double proportion () {
        return 1 - this.hurt / this.health;
    }
}
