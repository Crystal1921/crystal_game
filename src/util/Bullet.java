package util;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

import static util.gameMath.distance;

public class Bullet {
    private final double OriginX;
    private final double OriginY;
    private double x;
    private double y;
    private double theta;
    private double radius;
    public int lifetime = 0;
    private int pauseTime = 0;
    public int stage = 1;
    public double speed = 4;
    private double BoxRotation;
    private boolean isPause = false;

    public Bullet(double x, double y, double OriginX, double OriginY) {
        this.x = x;
        this.y = y;
        this.OriginX = OriginX;
        this.OriginY = OriginY;
        double deltaX = x - OriginX;
        double deltaY = y - OriginY;
        this.radius = distance(x, y, OriginX, OriginY);
        this.theta = Math.toDegrees(Math.atan2(deltaY, deltaX));
    }
    public Bullet(@NotNull PolarCoordinate polar, double OriginX, double OriginY, double BoxRotation) {
        this.theta = polar.theta;
        this.radius = polar.radius;
        this.OriginX = OriginX;
        this.OriginY = OriginY;
        this.BoxRotation = BoxRotation;
        this.x = polar.radius * Math.cos(Math.toRadians(polar.theta)) + OriginX;
        this.y = polar.radius * Math.sin(Math.toRadians(polar.theta)) + OriginY;
    }
    public Bullet(@NotNull PolarCoordinate polar, double OriginX, double OriginY) {
        this.theta = polar.theta;
        this.radius = polar.radius;
        this.OriginX = OriginX;
        this.OriginY = OriginY;
        this.x = polar.radius * Math.cos(Math.toRadians(polar.theta)) + OriginX;
        this.y = polar.radius * Math.sin(Math.toRadians(polar.theta)) + OriginY;
    }

    public void addTheta(double delta) {
        this.theta = this.theta + delta;
    }

    public void addRadius(double delta) {
        this.radius = this.radius + delta;
    }

    public void PolarPosition() {
        this.x = radius * Math.cos(Math.toRadians(theta)) + OriginX;
        this.y = radius * Math.sin(Math.toRadians(theta)) + OriginY;
    }

    public void CartesianPosition() {
        double deltaX = x - OriginX;
        double deltaY = y - OriginY;
        this.radius = distance(x, y, OriginX, OriginY);
        this.theta = Math.toDegrees(Math.atan2(deltaY, deltaX));
    }
    public Point Point () {
        return new Point((int)x,(int)y);
    }
    public void setLocation(double x,double y) {
        this.x = x;
        this.y = y;
    }
    public void setTheta (double theta) {
        this.theta = theta;
    }
    public double getTheta () {
        return theta;
    }
    public double getX() {
        return this.x;
    }
    public double getY() {
        return this.y;
    }
    public void addLife() {
        this.lifetime++;
    }
    public void addSpeed() {this.speed++;}
    public void addStage() {
        this.stage++;
    }
    public void addPauseTime() {pauseTime++;}
    public void resetLife() {
        this.lifetime = 0;
    }
    public void resetPauseTime() {this.pauseTime = 0;}
    public int getLifeTime() {
        return lifetime;
    }
    public int getPauseTime() {return pauseTime;}
    public String toPosition () {
        return "x:" + this.x + ",y:" + this.y;
    }
    public boolean isContinue() {return isPause;}
    public void pause () {isPause = !isPause;}
    public void setSpeed (double speed) {
        this.speed = speed;
    }
}
