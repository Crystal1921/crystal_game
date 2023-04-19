package input;

import org.jetbrains.annotations.NotNull;
import util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

@SuppressWarnings("ALL")
public class SimpleEasyEastProject extends JFrame implements Runnable, HyperlinkListener {

    private FrameRate frameRate;
    private BufferStrategy bs;
    private volatile boolean running;
    private Thread gameThread;
    private SimpleMouseInput mouse;
    private KeyboardInput keyboard;
    private CartesianCoordinate cartesian = new CartesianCoordinate(300,100);
    private PolarCoordinate polar = new PolarCoordinate(30,50);
    BufferedImage image = ImageIO.read(new File("src/image/crystal.png"));
    BufferedImage bullet = ImageIO.read(new File("src/image/bullet.png"));
    BufferedImage bullet2 = ImageIO.read(new File("src/image/bullet2.png"));
    Image icon = ImageIO.read(new File("src/image/happy.png"));
    Font font1 = new Font("微软雅黑", Font.PLAIN,12);
    private ArrayList<Point> lines = new ArrayList<>();
    private ArrayList<Point> lines2 = new ArrayList<>();
    private boolean drawingLine;
    private boolean doColor = true;
    private boolean doSize = false;
    private boolean doGameOver = false;
    private int colorIndex;
    private int size = 2;
    private int emitter = 0;
    private final double health = 500;
    private double hurt = 0;
    private double health_proportion;
    private final Color[] COLORS = {
            Color.RED,
            Color.ORANGE,
            Color.YELLOW,
            Color.GREEN,
            Color.CYAN,
            Color.BLUE,
            Color.MAGENTA
    };

    public SimpleEasyEastProject() throws IOException {
        frameRate = new FrameRate();
    }

    gameMath math = new gameMath();
    protected void createAndShowGUI() {

        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setEditable(false);
        editorPane.setText("<html><body style='font-family:微软雅黑;text-align:center;'><a href='https://github.com/Crystal1921' style='text-decoration:none;color:black;'>我很可爱，请给我star</a></body></html>");
        editorPane.addHyperlinkListener(this);
        JScrollPane scrollPane = new JScrollPane(editorPane);

        Canvas canvas = new Canvas();
        canvas.setSize(640,480);
        canvas.setBackground(Color.BLACK);
        canvas.setIgnoreRepaint(true);
        getContentPane().add(canvas,BorderLayout.CENTER);
        getContentPane().add(scrollPane,BorderLayout.NORTH);
        setTitle("简易丐版东方小游戏");
        setIgnoreRepaint(true);
        pack();
        keyboard = new KeyboardInput();
        canvas.addKeyListener( keyboard );
        mouse = new SimpleMouseInput();
        canvas.addMouseListener( mouse );
        canvas.addMouseMotionListener( mouse );
        canvas.addMouseWheelListener( mouse );
        setVisible(true);
        canvas.createBufferStrategy(2);
        bs = canvas.getBufferStrategy();
        canvas.requestFocus();
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        gameMath math1 = new gameMath();
        math1.setPolar(cartesian.x,cartesian.y);
        running = true;
        long curTime = System.nanoTime();
        long lastTime = curTime;
        double msPerFrame;
        frameRate.initialize();
        while (running) {
            curTime = System.nanoTime();
            msPerFrame = (curTime - lastTime) / 1000000;
            gameLoop( msPerFrame );//input ms
            lastTime = curTime;
        }
    }

    @Override
    public void hyperlinkUpdate(@NotNull HyperlinkEvent event) {
        if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            try {
                Desktop.getDesktop().browse(new URI(event.getURL().toString()));
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void gameLoop(double delta) {
        processInput();
        renderFrame();
        sleep((long)(50-delta));
        EmitterSpeed();
    }

    private void processInput() {
        keyboard.poll();
        mouse.poll();
        if ( keyboard.keyDownOnce(KeyEvent.VK_SPACE) ) {
            System.out.println("VK_SPACE");
        }
        if ( mouse.buttonDownOnce(MouseEvent.BUTTON1) ) {
            drawingLine = true;
        }
        if ( mouse.buttonDown(MouseEvent.BUTTON1) && (emitter == 0) ) {
            lines.add( mouse.getPosition() );
            lines2.add( mouse.getPosition() );
        } else if ( drawingLine ) {
            lines.add( null );
            lines2.add( null );
            drawingLine = false;
        }
        if ( keyboard.keyDownOnce( KeyEvent.VK_C ) ) {
            lines.clear();
        }
        if ( keyboard.keyDownOnce( KeyEvent.VK_S ) ) {
            doSize = !doSize;
        }
        if ( keyboard.keyDownOnce( KeyEvent.VK_Y ) ) {
            doColor = !doColor;
        }
        if ( hurt >= health ) {
            doGameOver = true;
        }
    }


    private void EmitterSpeed () {
        emitter++;
        if ( emitter == 5 ) {
            emitter = 0 ;
        }
    }
    private void sleep(long sleep) {
        try {
            if ( sleep <= 0 ) sleep = 0;
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void renderFrame() {
        do {
            do {
                Graphics2D graphics = null;
                try {
                    graphics =(Graphics2D) bs.getDrawGraphics();
                    graphics.clearRect(0,0,getWidth(),getHeight());
                    render(graphics);
                } finally {
                    if (graphics != null) {
                        graphics.dispose();
                    }
                }
            } while ( bs.contentsRestored() );
            bs.show();
        } while (bs.contentsLost());
    }

    private void render(Graphics2D graphics) {
        if (doColor) colorIndex += mouse.getNotches();
        if (doSize) size += mouse.getNotches();
        health_proportion = 1 - hurt / health;
        Color color = COLORS[ Math.abs( colorIndex % COLORS.length ) ];
        graphics.setColor(color);
        frameRate.calculate();
        graphics.setFont(font1);
        graphics.drawString(frameRate.getFrameRate(),30,30);
        graphics.drawString("Use Mouse to draw lines",30,45);
        graphics.drawString("Press C to clear lines",30,60);
        graphics.drawString("Mouse Wheel cycle colors",30,75);
        graphics.drawString(mouse.toString(),30,90);
        for (int i = 0; i < lines.size() - 1; ++i) {
            Point p = lines.get(i);
            if ( !(p == null) ) {
                graphics.drawImage(bullet,(int)p.getX() - bullet.getHeight() / 2,(int)p.getY() - bullet.getWidth() / 2,this);
                p.setLocation(p.getX(),p.getY()-4);
                if (p.getX() <= (260 + 180 * health_proportion) && p.getX() >= 260 && p.getY() <= 85 && p.getY() >= 65 ) {
                    if ( !doGameOver ) hurt++;
                }
                if ( p.getY() <= 0 ) {
                    lines.remove(i);
                }
            }
        }
        for (int i = 0; i < lines.size() - 1 ; i++) {
            Point p = lines2.get(i);
            if ( !(p == null) ) {
                graphics.drawImage(bullet2,(int)p.getX() - bullet2.getHeight() / 2,(int)p.getY() - bullet2.getWidth() / 2,this);
                PolarCoordinate polar = gameMath.Cartesian2Polar(p.getX(),p.getY());
                polar.addTheta(2);
                CartesianCoordinate cartesian1 = gameMath.Polar2Cartesian(polar.getTheta(),polar.getRadius());
                p.setLocation(cartesian1.getX(),cartesian1.getY());
                if ( p.getY() <= 0 ) {
                    lines2.remove(i);
                }
            }
        }
        graphics.setColor(Color.CYAN);
        graphics.drawString(String.format("%.1f%%",(health_proportion * 100)),300,105);
        graphics.drawRoundRect(250,60,200,30,5,5);
        graphics.fillRect(260,65,(int)Math.floor(180 * health_proportion),20);
        graphics.drawLine(250,90,400,90);
        graphics.drawImage(image,(int)mouse.getPosition().getX() - image.getHeight() / 2,(int)mouse.getPosition().getY() - image.getWidth() / 2,this);
    }

    protected void onWindowClosing() {
        try {
            running = false;
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public static void main(String[] args) throws IOException {
        final SimpleEasyEastProject app = new SimpleEasyEastProject();

        app.setIconImage(app.icon);
        app.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                app.onWindowClosing();
            }
        });
        SwingUtilities.invokeLater(app::createAndShowGUI);
    }
}
