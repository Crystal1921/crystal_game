package util;

import main.SimpleEasyTouhouFangame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class ResourceLoad {
    final public static BufferedImage player_image;
    final public static BufferedImage yin_yang_yu;
    final public static BufferedImage bullet;
    final public static BufferedImage bullet2;
    final public static BufferedImage Hakurei_bullet1;
    final public static BufferedImage Hakurei_bullet2;
    final public static BufferedImage Hakurei_bullet3;
    final public static BufferedImage Hakurei_bullet_1;
    final public static BufferedImage Hakurei_bullet_2;
    final public static BufferedImage Hakurei_bullet_3;
    final public static BufferedImage Hakurei_bullet_4;
    final public static BufferedImage Hakurei_Reimu;
    final public static Image background;
    final public static BufferedImage Minecraft_1;
    final public static BufferedImage Minecraft_2;
    final public static BufferedImage Minecraft_3;
    final public static BufferedImage Minecraft_3_2;
    final public static BufferedImage Minecraft_4;
    final public static BufferedImage Minecraft_4_2;
    final public static BufferedImage Minecraft_5;
    final public static BufferedImage Minecraft_6;

    static {
        try {
            player_image = ImageIO.read(Objects.requireNonNull(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/crystal_small.png")));
            yin_yang_yu = ImageIO.read(Objects.requireNonNull(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/yin_yang_yu.png")));
            bullet = ImageIO.read(Objects.requireNonNull(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/bullet.png")));
            bullet2 = ImageIO.read(Objects.requireNonNull(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/bullet2.png")));
            Hakurei_bullet1 = ImageIO.read(Objects.requireNonNull(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/Hakurei_bullet1.png")));
            Hakurei_bullet2 = ImageIO.read(Objects.requireNonNull(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/Hakurei_bullet2.png")));
            Hakurei_bullet3 = ImageIO.read(Objects.requireNonNull(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/Hakurei_bullet3.png")));
            Hakurei_bullet_1 = ImageIO.read(Objects.requireNonNull(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/Hakurei_bullet_1.png")));
            Hakurei_bullet_2 = ImageIO.read(Objects.requireNonNull(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/Hakurei_bullet_2.png")));
            Hakurei_bullet_3 = ImageIO.read(Objects.requireNonNull(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/Hakurei_bullet_3.png")));
            Hakurei_bullet_4 = ImageIO.read(Objects.requireNonNull(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/Hakurei_bullet_4.png")));
            Hakurei_Reimu = ImageIO.read(Objects.requireNonNull(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/Hakurei_Reimu_big.png")));
            background = ImageIO.read(Objects.requireNonNull(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/background.png")));
            Minecraft_1 = ImageIO.read(Objects.requireNonNull(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/Minecraft_image_1.png")));
            Minecraft_2 = ImageIO.read(Objects.requireNonNull(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/Minecraft_image_2.png")));
            Minecraft_3 = ImageIO.read(Objects.requireNonNull(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/Minecraft_image_3.png")));
            Minecraft_3_2 = ImageIO.read(Objects.requireNonNull(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/Minecraft_image_3_2.png")));
            Minecraft_4 = ImageIO.read(Objects.requireNonNull(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/Minecraft_image_4.png")));
            Minecraft_4_2 = ImageIO.read(Objects.requireNonNull(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/Minecraft_image_4_2.png")));
            Minecraft_5 = ImageIO.read(Objects.requireNonNull(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/Minecraft_image_5.png")));
            Minecraft_6 = ImageIO.read(Objects.requireNonNull(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/Minecraft_image_6.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
