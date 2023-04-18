package image;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MyPanel extends JPanel {
    BufferedImage image = ImageIO.read(new File("E:/code/javagame/src/image/crystal.png"));

    public MyPanel() throws IOException {
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 10, 10, null);
    }

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("My Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MyPanel panel = new MyPanel();
        frame.add(panel);
        frame.setSize(300, 300);
        frame.setVisible(true);
    }
}
