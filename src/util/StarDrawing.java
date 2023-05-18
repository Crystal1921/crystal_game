package util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class StarDrawing extends JPanel {
    private int method;
    private int a; // 内接圆半径
    private int b; // 外接圆半径
    private int num;
    private int numRows; // 网格行数
    private int numCols; // 网格列数

    public StarDrawing(int a, int b, int num) {
        this.a = a;
        this.b = b;
        this.num = num;
        method = 1;
    }

    public StarDrawing(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        method = 2;
    }

    private BufferedImage createImage() {
        int width = getWidth();
        int height = getHeight();
        System.out.println(width + "+" + height);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.BLACK); // 设置绘图上下文的颜色为黑色
        paintComponent(g2d);
        g2d.dispose();

        return image;
    }


    public static BufferedImage drawBlackIntersection(BufferedImage image1, BufferedImage image2) {
        int width = image1.getWidth();
        int height = image1.getHeight();

        BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb1 = image1.getRGB(x, y);
                int rgb2 = image2.getRGB(x, y);

                if (isBlack(rgb1) && isBlack(rgb2)) {
                    resultImage.setRGB(x, y, Color.WHITE.getRGB());
                }
            }
        }

        return resultImage;
    }

    private static boolean isBlack(int rgb) {
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;

        return red == 0 && green == 0 && blue == 0;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        switch (method) {
            case 1 -> drawStar(g, getWidth() / 2, getHeight() / 2, a, b, num);
            case 2 -> drawGrid(g);
        }
    }

    private void drawStar(Graphics g, int x, int y, int a, int b, int numPoints) {
        int[] xPoints = new int[2 * numPoints];
        int[] yPoints = new int[2 * numPoints];
        double angleIncrement = 2 * Math.PI / numPoints; // 角度增量
        double angle = Math.PI / 2 - angleIncrement; // 调整初始角度

        for (int i = 0; i < 2 * numPoints; i++) {
            if (i % 2 == 0) {
                xPoints[i] = x + (int) (a * Math.cos(angle));
                yPoints[i] = y - (int) (a * Math.sin(angle));
            } else {
                xPoints[i] = x + (int) (b * Math.cos(angle));
                yPoints[i] = y - (int) (b * Math.sin(angle));
            }
            angle += angleIncrement;
        }

        g.drawPolygon(xPoints, yPoints, 2 * numPoints);
    }

    private void drawGrid(Graphics g) {
        int cellWidth = getWidth() / numCols;
        int cellHeight = getHeight() / numRows;

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                int x = col * cellWidth;
                int y = row * cellHeight;

                g.drawRect(x, y, cellWidth, cellHeight);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Drawing Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        StarDrawing drawing1 = new StarDrawing(46, 150, 5); // 创建一个Drawing对象
        frame.add(drawing1); // 将Drawing对象添加到容器中
        frame.setSize(400, 400); // 设置窗口大小
        frame.setVisible(true); // 显示窗口

        BufferedImage image1 = drawing1.createImage(); // 将Drawing对象转换为BufferedImage对象

        StarDrawing drawing2 = new StarDrawing(10,10);
        frame.add(drawing2);
        frame.setSize(400,400);
        frame.setVisible(true);

        BufferedImage image2 = drawing2.createImage();

        File outputFile = new File("output.png");
        ImageIO.write(drawBlackIntersection(image1,image2), "png", outputFile);

    }
}
