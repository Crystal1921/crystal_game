package util;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;

public class gameMath {

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
    public static double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2)+Math.pow(p1.y - p2.y, 2));
    }

    //judge whether the point is out of the window
    public static boolean isOutWindow (int width, int height, @NotNull Bullet bullet) {
        return bullet.getX() <= -100 || bullet.getY() <= -100 || bullet.getX() >= width || bullet.getY() >= height;
    }

    public static boolean isOutWindow (int width, int height, @NotNull Cartesian coordinate) {
        return coordinate.x <= -100 || coordinate.y <= -100 || coordinate.x >= width || coordinate.y >= height;
    }

    public static boolean BoxTest(@NotNull BufferedImage image, @NotNull Point p, @NotNull Cartesian cartesian) {
        return (p.getX() >= (cartesian.x - image.getWidth() / 2.0) && p.getX() <= (cartesian.x + image.getWidth() / 2.0) && p.getY() >= (cartesian.y - image.getHeight() / 2.0) && p.getY() <= (cartesian.y + image.getHeight() / 2.0));
    }
    public static boolean BoxTest(@NotNull BufferedImage image, @NotNull Bullet bullet, @NotNull Point player) {
        return bullet.getX() >= (player.x - image.getWidth() / 2.0) && bullet.getX() <= (player.x + image.getWidth() / 2.0) && bullet.getY() >= (player.y - image.getHeight() / 2.0) && bullet.getY() <= (player.y + image.getHeight() / 2.0);
    }
}
