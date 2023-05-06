package util;

import main.SimpleEasyEastProject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

public class ImageRotatorExample {

    public static void main(String[] args) {
        try {
            // 加载图像
            BufferedImage image = ImageIO.read(Objects.requireNonNull(SimpleEasyEastProject.class.getClassLoader().getResourceAsStream("image/bullet3.png")));

            // 定义旋转角度和旋转中心坐标
            double angle = 45;
            int centerX = image.getWidth() / 2;
            int centerY = image.getHeight() / 2;

            // 对图像进行旋转
            BufferedImage rotatedImage = rotateImage(image, angle);

            // 保存旋转后的图像
            File outputFile = new File("output.png");
            ImageIO.write(rotatedImage, "png", outputFile);

            System.out.println("旋转完成");
        } catch (IOException ex) {
            System.err.println("发生错误：" + ex.getMessage());
        }
    }

    public static BufferedImage rotateImage(BufferedImage image, double angle) {
        // 计算旋转后的画布大小
        double radians = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(radians));
        double cos = Math.abs(Math.cos(radians));
        int width = (int) Math.round(image.getWidth() * cos + image.getHeight() * sin);
        int height = (int) Math.round(image.getHeight() * cos + image.getWidth() * sin);

        // 创建旋转后的画布
        BufferedImage rotatedImage = new BufferedImage(width, height, image.getType());
        Graphics2D g2d = rotatedImage.createGraphics();

        // 对画布进行旋转和平移操作
        AffineTransform transform = new AffineTransform();
        transform.translate((width - image.getWidth()) / 2.0, (height - image.getHeight()) / 2.0);
        transform.rotate(radians, image.getWidth() / 2.0, image.getHeight() / 2.0);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(image, transform, null);

        g2d.dispose();
        return rotatedImage;
    }
}
