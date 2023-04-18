package render;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.jetbrains.annotations.NotNull;
import util.*;

public class HelloWorld extends JFrame implements HyperlinkListener {
    private FrameRate frameRate;

    public HelloWorld() {
        frameRate = new FrameRate();
    }

    protected void createAndShowGUI() {
        GamePanel gamePanel = new GamePanel();
        JFrame frame = new JFrame("Hyperlink Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setEditable(false);
        editorPane.setText("<html><body style='font-family:微软雅黑;text-align:center;'><a href='https://github.com/Crystal1921' style='text-decoration:none;color:black;'>我很可爱，请给我star</a></body></html>");
        editorPane.addHyperlinkListener(this);
        JScrollPane scrollPane = new JScrollPane(editorPane);

        gamePanel.setBackground(Color.BLACK);
        gamePanel.setPreferredSize(new Dimension(320,420));
        getContentPane().add(gamePanel,BorderLayout.CENTER);
        getContentPane().add(scrollPane,BorderLayout.NORTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Hello World");
        pack();
        frameRate.initialize();
        setVisible( true );
    }

    private class GamePanel extends JPanel {
        @Override
        public void paint (Graphics graphics) {
            super.paint(graphics);
            onPaint(graphics);
        }
    }

    protected void onPaint(@NotNull Graphics graphics) {
        frameRate.calculate();
        graphics.setColor(Color.WHITE);
        graphics.drawString(frameRate.getFrameRate(),30,30);
        repaint();
    }

    public void hyperlinkUpdate (@NotNull HyperlinkEvent event) {
        if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            try {
                Desktop.getDesktop().browse(new URI(event.getURL().toString()));
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        final HelloWorld app = new HelloWorld();
        EventQueue.invokeLater(app::createAndShowGUI);
    }
}

