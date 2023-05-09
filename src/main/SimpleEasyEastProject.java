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
    private volatile boolean running;
    private Thread gameThread;
    private Thread move;
    private Thread backgroundmusic;
    private MP3PlayerThread.MusicThread musicThread = new MP3PlayerThread.MusicThread();
    private moveThread moveThread = new moveThread();
    private Random random = new Random();
    private SimpleMouseInput mouse;
    private KeyboardInput keyboard;
    public static Cartesian cartesian = new Cartesian(300,100);
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
    private ArrayList<bullet> lines2 = new ArrayList<>();
    private ArrayList<bullet> lines3 = new ArrayList<>();
    private ArrayList<Cartesian> enemy = new ArrayList<>();
    private ArrayList<BufferedImage> rotatedImage = new ArrayList<>();
    private boolean doColor = true;
    private boolean doGameOver = false;
    private boolean MotionControl = false;
    private int colorIndex;
    private int enemyNum = 0;
    private int emitter = 1;
    private int MoveSpeed = 3;
    public static int RoundEmitterNum = 15;
    final private int width = 640;
    final private int height = 480;
    private entity Reimu = new entity(100,7);
    private entity player = new entity(50,4,new Cartesian(320,300));
    private Cartesian position = player.getPosition();
    private boolean doImmutable = false;
    public static double RoundEmitterRotation = 1;
    public static double addRadius = 4;
    public static double addTheta = 0.5;
    private double rotation;
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
                    isPlaying = !isPlaying;
                }
            }
        });
        JButton button2 = new JButton("bgm off");
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isPlaying) {
                    backgroundmusic.suspend();
                    isPlaying = !isPlaying;
                }
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
        setFilename();
        backgroundmusic = new Thread(musicThread);
        backgroundmusic.start();
        backgroundmusic.suspend();
        move = new Thread(moveThread);
        move.start();
    }

    @Override
    public void run() {
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
        if ( Reimu.hurt >= Reimu.health) {
            doGameOver = true;
        }
        processInput();
        renderFrame();
        emitter++;
        sleep((long)(40-delta));
        //游戏结束条件--有一方血量为零
        if (Reimu.proportion() <= 0 || player.proportion() <= 0) {
            doGameOver = true;
            move.stop();
        }
        //每tick有0.1%的概率随机生成一个浮空敌人
        if (random.nextInt(100) <= 1 && enemyNum <= 4) {
            enemy.add(new Cartesian(random.nextInt(250)+70, random.nextInt(100)+50) );
            enemyNum++;
        }
    }
    //处理键盘输入和鼠标输入
    private void processInput() {
        keyboard.poll();
        mouse.poll();
        MoveSpeed += mouse.getNotches();
        if (MoveSpeed <= 0) {
            MoveSpeed = 1;
        }
        if ( MotionControl ) {
            MouseControl();
        }
        else {
            KeyboardControl();
        }
        if ( keyboard.keyDownOnce( KeyEvent.VK_F ) ) {
            doImmutable = true;
        }
        //作弊码，按下c后全屏清零
        if ( keyboard.keyDownOnce( KeyEvent.VK_C ) ) {
            lines.clear();
            lines2.clear();
            rotatedImage.clear();
            rotatedImage.clear();
        }
    }

    private void MouseControl () {
        position.setLocation(mouse.getPosition().x,mouse.getPosition().y);
        if ( mouse.buttonDown(MouseEvent.BUTTON1) && (emitter % player.emitterSpeed == 0) ) {
            lines.add( position.Point() );
        }
    }

    private void KeyboardControl () {
        if ( emitter % player.emitterSpeed == 0 ) {
            lines.add( position.Point() );
        }
        if ( keyboard.keyDown( KeyEvent.VK_DOWN ) || keyboard.keyDown( KeyEvent.VK_S )) {
            position.addY(MoveSpeed);
        }
        if ( keyboard.keyDown( KeyEvent.VK_UP ) || keyboard.keyDown( KeyEvent.VK_W )) {
            position.addY(-MoveSpeed);
        }
        if ( keyboard.keyDown( KeyEvent.VK_RIGHT ) || keyboard.keyDown( KeyEvent.VK_D )) {
            position.addX(MoveSpeed);
        }
        if ( keyboard.keyDown( KeyEvent.VK_LEFT ) || keyboard.keyDown( KeyEvent.VK_A )) {
            position.addX(-MoveSpeed);
        }
    }

    //圆形发射器，以输入的坐标为原点，等角度生成n个目标
    private void RoundEmitter (int n, double theta, ArrayList lines) {
        if (n != 0) {
            int angle = 360 / n;
            rotation += theta;
            if (emitter % Reimu.emitterSpeed == 0) {
                for (int i = 0; i < n; i++) {
                    PolarCoordinate polar1 = new PolarCoordinate(angle * i + rotation + 90, 15);
                    lines.add(new bullet(polar1,cartesian.x,cartesian.y));
                    rotatedImage.add(ImageRotatorExample.rotateImage(bullet3,angle * i + rotation));
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
        if (!doGameOver) RoundEmitter(RoundEmitterNum,RoundEmitterRotation,lines2);
        graphics.drawString(frameRate.getFrameRate(),30,30);
        //处理玩家发射的子弹事件
        for (int i = 0; i < lines.size() - 1; ++i) {
            Point p = lines.get(i);
            if ( p != null) {
                graphics.drawImage(bullet,(int)p.getX() - bullet.getHeight() / 2,(int)p.getY() - bullet.getWidth() / 2,this);
                p.setLocation(p.getX(),p.getY() - 4);
                if (BoxTest(Hakurei_Reimu, p, cartesian) && !doGameOver) {
                    Reimu.addHurt();
                    lines.remove(i);
                }
                if ( isOutWindow(width,height,new Cartesian(p.x,p.y)) ) {
                    lines.remove(i);
                }
            }
        }
        //处理灵梦发射的子弹事件
        for (int i = 0; i < lines2.size() - 1 ; i++) {
            bullet p = lines2.get(i);
            if ( p != null ) {
                graphics.drawImage(rotatedImage.get(i),(int)p.getX() - bullet2.getHeight() / 2,(int)p.getY() - bullet2.getWidth() / 2,this);
                p.addRadius(addRadius);
                p.addTheta(addTheta);
                p.PolarPosition();
                if (BoxTest(player_image, p, position.Point()) && !doGameOver) {
                    if (!doImmutable) player.addHurt();
                    lines2.remove(i);
                    rotatedImage.remove(i);
                }
                if ( isOutWindow(width,height,p) ) {
                    lines2.remove(i);
                    rotatedImage.remove(i);
                }
            }
        }
        //处理小实体的子弹事件
        if (enemy.size() >= 1){
            for (int i = 0; i < enemy.size() - 1; i++) {
            }
        }
        //绘制博丽灵梦的血量条
        graphics.setColor(Color.CYAN);
        graphics.drawString(String.format("%.1f%%",(Reimu.proportion() * 100)),400,85);
        graphics.drawRoundRect(250,30,200,30,5,5);
        graphics.fillRoundRect(260,35,(int)Math.floor(180 * Reimu.proportion()),20,5,5);
        //绘制玩家的血量条
        graphics.setColor(Color.ORANGE);
        graphics.drawString(String.format("%.1f%%",(player.proportion() * 100)),400,455);
        graphics.drawRoundRect(250,400,200,30,5,5);
        graphics.fillRoundRect(260,405,(int)Math.floor(180 * player.proportion()),20,5,5);
        graphics.setFont(font2);
        //结束后
        if (Reimu.proportion() <= 0) {
            graphics.drawString("You Win",400,105);
            graphics.drawString("欺负灵梦awa",width / 2 - 100, height / 2);
        }
        if (player.proportion() <= 0) {
            graphics.drawString("You Lose",400,475);
            graphics.drawString("作者都打不过，你打不过很正常的",width / 2 - 100, height / 2);
        }
        graphics.drawImage(player_image,(int)position.x - player_image.getHeight() / 2,(int)position.y - player_image.getWidth() / 2,this);
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
        System.setProperty("user.language", "en");
        System.setProperty("user.region", "US");
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
