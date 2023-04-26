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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;

import static util.gameMath.*;
import static Thread.MP3PlayerThread.*;
import Thread.MP3PlayerThread;
import Thread.moveThread;

@SuppressWarnings("ALL")
public class SimpleEasyEastProject extends JFrame implements Runnable, HyperlinkListener {

    private FrameRate frameRate;
    private BufferStrategy bs;
    private JButton button;
    private volatile boolean running;
    private Thread gameThread;
    private Thread move;
    private Thread backgroundmusic;
    private MP3PlayerThread.MusicThread musicThread = new MP3PlayerThread.MusicThread();
    private moveThread moveThread = new moveThread();
    private Random random = new Random();
    private SimpleMouseInput mouse;
    private KeyboardInput keyboard;
    public static CartesianCoordinate cartesian = new CartesianCoordinate(300,100);
    private PolarCoordinate polar = new PolarCoordinate(30,50);
    final BufferedImage player_image = ImageIO.read(SimpleEasyEastProject.class.getClassLoader().getResourceAsStream("image/crystal_small.png"));
    final BufferedImage enemy1 = ImageIO.read(SimpleEasyEastProject.class.getClassLoader().getResourceAsStream("image/enemy1.png"));
    final BufferedImage bullet = ImageIO.read(SimpleEasyEastProject.class.getClassLoader().getResourceAsStream("image/bullet.png"));
    final BufferedImage bullet2 = ImageIO.read(SimpleEasyEastProject.class.getClassLoader().getResourceAsStream("image/bullet2.png"));
    final BufferedImage bullet3 = ImageIO.read(SimpleEasyEastProject.class.getClassLoader().getResourceAsStream("image/bullet3.png"));
    final BufferedImage Hakurei_Reimu = ImageIO.read(SimpleEasyEastProject.class.getClassLoader().getResourceAsStream("image/Hakurei_Reimu_big.png"));
    final BufferedImage background = ImageIO.read(SimpleEasyEastProject.class.getClassLoader().getResourceAsStream("image/background.png"));
    final Image icon = ImageIO.read(SimpleEasyEastProject.class.getClassLoader().getResourceAsStream("image/happy.png"));
    final Font font1 = new Font("微软雅黑", Font.PLAIN,12);
    final Font font2 = new Font("微软雅黑", Font.BOLD,24);
    private ArrayList<Point> lines = new ArrayList<>();
    private ArrayList<CartesianCoordinate> lines2 = new ArrayList<>();
    private ArrayList<CartesianCoordinate> lines3 = new ArrayList<>();
    private ArrayList<CartesianCoordinate> OriginPoint = new ArrayList<>();
    private ArrayList<CartesianCoordinate> enemy = new ArrayList<>();
    private ArrayList<BufferedImage> rotatedImage = new ArrayList<>();
    private boolean drawingLine;
    private boolean doColor = true;
    private boolean doGameOver = false;
    private int colorIndex;
    private int size = 2;
    private int enemyNum = 0;
    public static int emitter = 1;
    public static int RoundEmitterNum = 15;
    final private int width = 640;
    final private int height = 480;
    private int player_hurt = 0;
    private double hurt = 0;
    private final double Hakurei_ReimuHealth = 100;
    private final double playerHealth = 50;
    public static double RoundEmitterRotation = 1;
    public static double addRadius = 4;
    public static double addTheta = 0.5;
    private double rotation;
    private double healthProportion;
    private double playerHealthProportion;
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

        //新建bgm控制按钮
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
        //面版属性控制
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
        //新建线程，开始游戏
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
    //我很可爱，请给我star
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
    //游戏循环，每tick执行一次
    private void gameLoop(double delta) {
        processInput();
        renderFrame();
        sleep((long)(40-delta));
        EmitterSpeed();
        //游戏结束条件--有一方血量为零
        if (healthProportion <= 0 || playerHealthProportion <= 0) {
            doGameOver = true;
            move.stop();
        }
        //每tick有0.1%的概率随机生成一个浮空敌人
        if (random.nextInt(100) <= 1 && enemyNum <= 4) {
            enemy.add(new CartesianCoordinate(random.nextInt(250)+70, random.nextInt(100)+50) );
            enemyNum++;
        }
    }
    //处理键盘输入和鼠标输入
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
        //作弊码，按下c后全屏清零
        if ( keyboard.keyDownOnce( KeyEvent.VK_C ) ) {
            lines.clear();
            lines2.clear();
            rotatedImage.clear();
            OriginPoint.clear();
            rotatedImage.clear();
        }
        if ( keyboard.keyDownOnce( KeyEvent.VK_Y ) ) {
            doColor = !doColor;
        }
        if ( hurt >= Hakurei_ReimuHealth) {
            doGameOver = true;
        }
    }

    public static void setOriginPoint (CartesianCoordinate coordinate) {
        cartesian = coordinate;
    }

    private CartesianCoordinate PositionTransform (CartesianCoordinate p, CartesianCoordinate origin) {
        PolarCoordinate polar = gameMath.Cartesian2Polar(p.getX(),p.getY());
        setOrigin(origin);
        polar.addTheta(addTheta);
        polar.addRadius(addRadius);
        CartesianCoordinate cartesian1 = gameMath.Polar2Cartesian(polar.getTheta(),polar.getRadius());
        return cartesian1;
    }
    private void EmitterSpeed () {
        emitter++;
        if ( emitter == 5 ) {
            emitter = 0 ;
        }
    }
    //圆形发射器，以输入的坐标为原点，以360/n为差值，等角度生成n个目标
    private void RoundEmitter (int n, double theta, CartesianCoordinate cartesian,ArrayList lines) {
        if (n != 0) {
            int angle = 360 / n;
            rotation += theta;
            if (emitter == 0) {
                for (int i = 0; i < n; i++) {
                    PolarCoordinate polar1 = new PolarCoordinate(angle * i + rotation + 90, 15);
                    CartesianCoordinate cartesian1 = Polar2Cartesian(polar1.theta, polar1.radius);
                    lines.add(cartesian1);
                    OriginPoint.add(this.cartesian);
                    rotatedImage.add(ImageRotatorExample.rotateImage(bullet3,(int)cartesian1.x,(int)cartesian1.y,angle * i + rotation));
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
        Color color = COLORS[ Math.abs( colorIndex % COLORS.length ) ];
        graphics.drawImage(background,0,0,this);
        graphics.setFont(font1);
        graphics.setColor(color);
        //计算帧数
        frameRate.calculate();
        if (doColor) colorIndex += mouse.getNotches();
        if (!doGameOver) RoundEmitter(RoundEmitterNum,RoundEmitterRotation,cartesian,lines2);
        setOrigin(cartesian);
        healthProportion = 1 - hurt / Hakurei_ReimuHealth;
        playerHealthProportion = 1 - player_hurt / playerHealth;
        graphics.drawString(frameRate.getFrameRate(),30,30);
        if (enemy.size() >= 1){
            for (int i = 0; i < enemy.size() - 1; i++) {
                graphics.drawImage(enemy1, (int) enemy.get(i).x, (int) enemy.get(i).y,this);
                if (!doGameOver) RoundEmitter(RoundEmitterNum,RoundEmitterRotation,enemy.get(i),lines3);
            }
        }
        for (int i = 0; i < lines.size() - 1; ++i) {
            Point p = lines.get(i);
            if ( !(p == null) ) {
                graphics.drawImage(bullet,(int)p.getX() - bullet.getHeight() / 2,(int)p.getY() - bullet.getWidth() / 2,this);
                p.setLocation(p.getX(),p.getY()-4);
                if (BoxTest(Hakurei_Reimu, p, cartesian) && !doGameOver) {
                    hurt++;
                    lines.remove(i);
                }
                if ( isOutWindow(width,height,new CartesianCoordinate(p.x,p.y)) ) {
                    lines.remove(i);
                }
            }
        }
        for (int i = 0; i < lines2.size() - 1 ; i++) {
            CartesianCoordinate p = lines2.get(i);
            if ( p != null ) {
                graphics.drawImage(rotatedImage.get(i),(int)p.getX() - bullet2.getHeight() / 2,(int)p.getY() - bullet2.getWidth() / 2,this);
                CartesianCoordinate cartesian1 = PositionTransform(p,OriginPoint.get(i));
                p.setX(cartesian1.getX());
                p.setY(cartesian1.getY());
                if (BoxTest(player_image, p, mouse.getPosition()) && !doGameOver) {
                    player_hurt++;
                    lines2.remove(i);
                    rotatedImage.remove(i);
                    OriginPoint.remove(i);
                }
                if ( isOutWindow(width,height,cartesian1) ) {
                    lines2.remove(i);
                    rotatedImage.remove(i);
                    OriginPoint.remove(i);
                }
            }
        }
        //绘制博丽灵梦的血量条
        graphics.setColor(Color.CYAN);
        graphics.drawString(String.format("%.1f%%",(healthProportion * 100)),400,85);
        graphics.drawRoundRect(250,30,200,30,5,5);
        graphics.fillRoundRect(260,35,(int)Math.floor(180 * healthProportion),20,5,5);
        //绘制玩家的血量条
        graphics.setColor(Color.ORANGE);
        graphics.drawString(String.format("%.1f%%",(playerHealthProportion * 100)),400,455);
        graphics.drawRoundRect(250,400,200,30,5,5);
        graphics.fillRoundRect(260,405,(int)Math.floor(180 * playerHealthProportion),20,5,5);
        graphics.setFont(font2);
        //结束后
        if (healthProportion <= 0) {
            graphics.drawString("You Win",400,105);
            graphics.drawString("欺负灵梦awa",width / 2 - 100, height / 2);
        }
        if (playerHealthProportion <= 0) {
            graphics.drawString("You Lose",400,475);
            graphics.drawString("作者都打不过，你打不过很正常的",width / 2 - 100, height / 2);
        }
        graphics.drawImage(player_image,(int)mouse.getPosition().getX() - player_image.getHeight() / 2,(int)mouse.getPosition().getY() - player_image.getWidth() / 2,this);
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
}
