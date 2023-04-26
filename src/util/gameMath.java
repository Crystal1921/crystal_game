package util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;

public class gameMath {
    private static double polarX;
    private static double polarY;

    public static void setOrigin(double polarX, double polarY) {
        gameMath.polarX = polarX;
        gameMath.polarY = polarY;
    }
    public static void setOrigin(CartesianCoordinate coordinate) {
        gameMath.polarX = coordinate.x;
        gameMath.polarY = coordinate.y;
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    @Contract("_, _ -> new")
    public static @NotNull PolarCoordinate Cartesian2Polar(double x, double y) {
        double deltaX = x - polarX;
        double deltaY = y - polarY;
        double radius = distance(x, y, polarX, polarY);
        double theta = Math.toDegrees(Math.atan2(deltaY, deltaX));
        return new PolarCoordinate(theta, radius);
    }

    @Contract("_, _ -> new")
    public static @NotNull CartesianCoordinate Polar2Cartesian(double theta, double radius) {
        double x = radius * Math.cos(Math.toRadians(theta)) + polarX;
        double y = radius * Math.sin(Math.toRadians(theta)) + polarY;
        return new CartesianCoordinate(x, y);
    }
    //judge whether the point is out of the window
    public static boolean isOutWindow (int width, int height, @NotNull CartesianCoordinate coordinate) {
        return coordinate.x <= -100 || coordinate.y <= -100 || coordinate.x >= width || coordinate.y >= height;
    }

    public static boolean BoxTest(@NotNull BufferedImage image, @NotNull Point p, @NotNull CartesianCoordinate cartesian) {
        return (p.getX() >= (cartesian.x - image.getWidth() / 2.0) && p.getX() <= (cartesian.x + image.getWidth() / 2.0) && p.getY() >= (cartesian.y - image.getHeight() / 2.0) && p.getY() <= (cartesian.y + image.getHeight() / 2.0));
    }
    public static boolean BoxTest(BufferedImage image, CartesianCoordinate coordinate_bullet, Point player) {
        return coordinate_bullet.x >= (player.x - image.getWidth() / 2.0) && coordinate_bullet.x <= (player.x + image.getWidth() / 2.0) && coordinate_bullet.y >= (player.y - image.getHeight() / 2.0) && coordinate_bullet.y <= (player.y + image.getHeight() / 2.0);
    }
}
