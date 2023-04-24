package main;

import org.jetbrains.annotations.NotNull;
import util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static util.gameMath.*;
import static util.MP3Player.*;
import util.MP3Player;

@SuppressWarnings("ALL")
public class SimpleEasyEastProject extends JFrame implements Runnable, HyperlinkListener, ActionListener {

    private FrameRate frameRate;
    private BufferStrategy bs;
    private JButton button;
    private volatile boolean running;
    private Thread gameThread;
    private Thread move;
    private Thread backgroundmusic;
    private MP3Player.MusicThread musicThread = new MP3Player.MusicThread();
    private moveThread moveThread = new moveThread();
    private SimpleMouseInput mouse;
    private KeyboardInput keyboard;
    public static CartesianCoordinate cartesian = new CartesianCoordinate(300,100);
    private PolarCoordinate polar = new PolarCoordinate(30,50);
    final BufferedImage image = ImageIO.read(new File("src/image/crystal_small.png"));
    final BufferedImage bullet = ImageIO.read(new File("src/image/bullet.png"));
    final BufferedImage bullet2 = ImageIO.read(new File("src/image/bullet2.png"));
    final BufferedImage Hakurei_Reimu = ImageIO.read(new File("src/image/Hakurei_Reimu_big.png"));
    final BufferedImage background = ImageIO.read(new File("src/image/background.png"));
    final Image icon = ImageIO.read(new File("src/image/happy.png"));
    final Font font1 = new Font("微软雅黑", Font.PLAIN,12);
    private ArrayList<Point> lines = new ArrayList<>();
    private ArrayList<CartesianCoordinate> lines2 = new ArrayList<>();
    private ArrayList<CartesianCoordinate> OriginPoint = new ArrayList<>();
    private boolean drawingLine;
    private boolean doColor = true;
    private boolean doSize = false;
    private boolean doGameOver = false;
    private int colorIndex;
    private int size = 2;
    public static int emitter = 1;
    public static int RoundEmitterNum = 15;
    final private int width = 640;
    final private int height = 480;
    private int player_hur;
    private double hurt = 0;
    private final double health = 100;
    public static double RoundEmitterRotation = 1;
    public static double addRadius = 4;
    public static double addTheta = 0.5;
    private double rotation;
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
        JEditorPane editorPane1 = new JEditorPane();
        editorPane1.setLayout(new FlowLayout());
        editorPane1.setPreferredSize(new Dimension(width,40));
        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setEditable(false);
        editorPane.setText("<html><body style='font-family:微软雅黑;text-align:center;'><a href='https://github.com/Crystal1921' style='text-decoration:none;color:black;'>我很可爱，请给我star</a></body></html>");
        editorPane.addHyperlinkListener(this);
        JScrollPane scrollPane = new JScrollPane(editorPane);

        JButton button1 = new JButton("bgm on");
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!isPlaying) {
                    backgroundmusic.resume();
                    isPlaying = true;
                }
            }
        });
        JButton button2 = new JButton("bgm off");
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                backgroundmusic.suspend();
                isPlaying = false;
            }
        });
        editorPane1.add(button1);
        editorPane1.add(button2);

        Canvas canvas = new Canvas();
        canvas.setSize(width,height);
        canvas.setBackground(Color.BLACK);
        canvas.setIgnoreRepaint(true);
        getContentPane().add(canvas,BorderLayout.CENTER);
        getContentPane().add(scrollPane,BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(editorPane1), BorderLayout.SOUTH);
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
        setFilename("src/sound/Eternal_Night.mp3");
        backgroundmusic = new Thread(musicThread);
        backgroundmusic.start();
        move = new Thread(moveThread);
        move.start();
    }

    @Override
    public void run() {
        setOrigin(cartesian);
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
        sleep((long)(40-delta));
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
        } else if ( drawingLine ) {
            lines.add( null );
            drawingLine = false;
        }
        if ( keyboard.keyDownOnce( KeyEvent.VK_C ) ) {
            lines.clear();
            lines2.clear();
            OriginPoint.clear();
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

    public static void setOriginPoint (CartesianCoordinate coordinate) {
        cartesian = coordinate;
    }

    private void EmitterSpeed () {
        emitter++;
        if ( emitter == 5 ) {
            emitter = 0 ;
        }
    }

    private void RoundEmitter (int n, double theta) {
        if (n != 0) {
            int angle = 360 / n;
            rotation += theta;
            if (emitter == 0) {
                for (int i = 0; i < n; i++) {
                    PolarCoordinate polar1 = new PolarCoordinate(angle * i + rotation, 15);
                    CartesianCoordinate cartesian1 = Polar2Cartesian(polar1.theta, polar1.radius);
                    lines2.add(cartesian1);
                    OriginPoint.add(cartesian);
                }
            }
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

    private void render(@NotNull Graphics2D graphics) {
        graphics.drawImage(background,0,0,this);
        if (doColor) colorIndex += mouse.getNotches();
        if (doSize) size += mouse.getNotches();
        RoundEmitter(RoundEmitterNum,RoundEmitterRotation);
        setOrigin(cartesian);
        health_proportion = 1 - hurt / health;
        Color color = COLORS[ Math.abs( colorIndex % COLORS.length ) ];
        graphics.setColor(color);
        frameRate.calculate();
        graphics.setFont(font1);
        graphics.drawString(frameRate.getFrameRate(),30,30);
        for (int i = 0; i < lines.size() - 1; ++i) {
            Point p = lines.get(i);
            if ( !(p == null) ) {
                graphics.drawImage(bullet,(int)p.getX() - bullet.getHeight() / 2,(int)p.getY() - bullet.getWidth() / 2,this);
                p.setLocation(p.getX(),p.getY()-4);
                if (isOutImage(Hakurei_Reimu, p, cartesian) && !doGameOver) {
                    hurt++;
                    lines.remove(i);
                }
                if ( isOutFrame(width,height,new CartesianCoordinate(p.x,p.y)) ) {
                    lines.remove(i);
                }
            }
        }
        for (int i = 0; i < lines2.size() - 1 ; i++) {
            CartesianCoordinate p = lines2.get(i);
            if ( !(p == null) ) {
                graphics.drawImage(bullet2,(int)p.getX() - bullet2.getHeight() / 2,(int)p.getY() - bullet2.getWidth() / 2,this);
                PolarCoordinate polar = gameMath.Cartesian2Polar(p.getX(),p.getY());
                setOrigin(OriginPoint.get(i));
                polar.addTheta(addTheta);
                polar.addRadius(addRadius);
                CartesianCoordinate cartesian1 = gameMath.Polar2Cartesian(polar.getTheta(),polar.getRadius());
                p.setX(cartesian1.getX());
                p.setY(cartesian1.getY());
                if ( isOutFrame(width,height,cartesian1) ) {
                    lines2.remove(i);
                    OriginPoint.remove(i);
                }
            }
        }
        graphics.setColor(Color.CYAN);
        graphics.drawString(String.format("%.1f%%",(health_proportion * 100)),400,85);
        graphics.drawRoundRect(250,30,200,30,5,5);
        graphics.fillRoundRect(260,35,(int)Math.floor(180 * health_proportion),20,5,5);
        graphics.drawImage(image,(int)mouse.getPosition().getX() - image.getHeight() / 2,(int)mouse.getPosition().getY() - image.getWidth() / 2,this);
        graphics.drawImage(Hakurei_Reimu,(int)cartesian.x - Hakurei_Reimu.getHeight() / 2,(int)cartesian.y - Hakurei_Reimu.getHeight() / 2,this);
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

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
