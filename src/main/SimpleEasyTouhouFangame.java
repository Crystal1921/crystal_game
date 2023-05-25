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

import static java.lang.Math.PI;
import static util.ImageRotatorExample.rotateImage;
import static util.gameMath.*;
import static Thread.MP3PlayerThread.*;
import static util.StarDrawing.*;
import Thread.MP3PlayerThread;
import Thread.moveThread;

@SuppressWarnings("ALL")
public class SimpleEasyTouhouFangame extends JFrame implements Runnable, HyperlinkListener {

    private FrameRate frameRate;
    private BufferStrategy bs;
    private volatile boolean running;
    private Thread gameThread;
    private Thread move;
    private Thread backgroundmusic;
    private MP3PlayerThread.MusicThread musicThread = new MP3PlayerThread.MusicThread();
    private moveThread moveThread = new moveThread();
    private SimpleMouseInput mouse;
    private KeyboardInput keyboard;
    public static Cartesian cartesian = new Cartesian(300,100);
    final BufferedImage player_image = ImageIO.read(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/crystal_small.png"));
    final BufferedImage yin_yang_yu = ImageIO.read(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/yin_yang_yu.png"));
    final BufferedImage bullet = ImageIO.read(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/bullet.png"));
    final BufferedImage bullet2 = ImageIO.read(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/bullet2.png"));
    final BufferedImage Hakurei_bullet1 = ImageIO.read(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/Hakurei_bullet1.png"));
    final BufferedImage Hakurei_bullet2 = ImageIO.read(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/Hakurei_bullet2.png"));
    final BufferedImage Hakurei_bullet3 = ImageIO.read(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/Hakurei_bullet3.png"));
    final BufferedImage Hakurei_bullet_1 = ImageIO.read(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/Hakurei_bullet_1.png"));
    final BufferedImage Hakurei_bullet_2 = ImageIO.read(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/Hakurei_bullet_2.png"));
    final BufferedImage Hakurei_bullet_3 = ImageIO.read(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/Hakurei_bullet_3.png"));
    final BufferedImage Hakurei_bullet_4 = ImageIO.read(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/Hakurei_bullet_4.png"));
    final BufferedImage Hakurei_Reimu = ImageIO.read(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/Hakurei_Reimu_big.png"));
    final BufferedImage background = ImageIO.read(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/background.png"));
    final Image icon = ImageIO.read(SimpleEasyTouhouFangame.class.getClassLoader().getResourceAsStream("image/happy.png"));
    final Font font1 = new Font("微软雅黑", Font.PLAIN,12);
    final Font font2 = new Font("微软雅黑", Font.BOLD,24);
    private ArrayList<OnmyouDama> YinYangYu = new ArrayList<>();
    private ArrayList<Point> lines = new ArrayList<>();
    private ArrayList<Cartesian> enemy = new ArrayList<>();
    private ArrayList<ArrayList<Bullet>> bulletLists = new ArrayList<>();
    private boolean doGameOver = false;
    private boolean MotionControl = true;
    private boolean doImmutable = false;
    private int enemyNum = 0;
    private int emitter = 1;
    private double timer;
    private int MoveSpeed = 3;
    public static int RoundEmitterNum = 15;
    public static int level = 2;
    final private int width = 640;
    final private int height = 480;
    private entity Reimu = new entity(100,6);
    private entity player = new entity(50,6,new Cartesian(320,300));
    private Cartesian position = player.getPosition();
    public static double RoundEmitterRotation = 1;
    public static double addRadius = 4;
    public static double addTheta = 0.5;
    private double rotation;

    public SimpleEasyTouhouFangame() throws IOException {
        frameRate = new FrameRate();
    }
    protected void createAndShowGUI() {
        for (int i = 0; i < 10; i++) {
            bulletLists.add(new ArrayList<Bullet>());
        }
        BufferedImage Starimage = dotImage(dotImageGenerate(23,75,5,20));
        ImageEmitter(Starimage,bulletLists.get(3));
        for (int i = 0; i < 6; i++) {
            YinYangYu.add(new OnmyouDama(5,70));
        }
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
        if ( Reimu.hurt >= Reimu.health && level == 1) {
            level = 2;
            Reimu.hurt = 0;
            player.hurt = 0;
        }
        processInput();
        renderFrame();
        emitter++;
        if(!doGameOver) timer = emitter;
        sleep((long)(40-delta));
        //游戏结束条件--有一方血量为零
        if (player.proportion() <= 0) {
            doGameOver = true;
            move.stop();
        }
        if (Reimu.proportion() <= 0 && level == 2) {
            doGameOver = true;
            move.stop();
        }
        //阴阳玉
        for (int i = 0; i < YinYangYu.size() - 1; i++) {
            if (YinYangYu.get(i).proportion() <= 0) {
                YinYangYu.remove(i);
            }
        }
        /*
        //每tick有0.1%的概率随机生成一个浮空敌人（待开发）
        if (random.nextInt(100) <= 1 && enemyNum <= 4) {
            enemy.add(new Cartesian(random.nextInt(250)+70, random.nextInt(100)+50) );
            enemyNum++;
        }
         */
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
        //切换键盘控制和鼠标控制
        if ( keyboard.keyDownOnce( KeyEvent.VK_Q ) ) {
            MotionControl = !MotionControl;
        }
        //真-作弊码 无敌
        if ( keyboard.keyDownOnce( KeyEvent.VK_F ) ) {
            doImmutable = true;
        }
        //虚假的作弊码，按下c后全屏清零
        if ( keyboard.keyDownOnce( KeyEvent.VK_C ) ) {
            lines.clear();
            bulletLists.get(1).clear();
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
        if ( (keyboard.keyDown( KeyEvent.VK_DOWN ) || keyboard.keyDown( KeyEvent.VK_S )) && position.y <= height) {
            position.addY(MoveSpeed);
        }
        if (( keyboard.keyDown( KeyEvent.VK_UP ) || keyboard.keyDown( KeyEvent.VK_W )) && position.y >= 0) {
            position.addY(-MoveSpeed);
        }
        if (( keyboard.keyDown( KeyEvent.VK_RIGHT ) || keyboard.keyDown( KeyEvent.VK_D )) && position.x >= 0) {
            position.addX(MoveSpeed);
        }
        if (( keyboard.keyDown( KeyEvent.VK_LEFT ) || keyboard.keyDown( KeyEvent.VK_A ))  && position.x <= width) {
            position.addX(-MoveSpeed);
        }
        if (position.x <= 0) position.x = 1;
        if (position.x >= width) position.x = width;
    }

    //圆形发射器，以输入的坐标为原点，等角度生成n个目标
    private void RoundEmitter (int n, double theta, ArrayList lines) {
        if (n != 0) {
            int angle = 360 / n;
            rotation += theta;
            if (emitter % (Reimu.emitterSpeed + (level - 1) * 60 ) == 0) {
                for (int i = 0; i <= n; i++) {
                    PolarCoordinate polar1 = new PolarCoordinate(angle * i + rotation + 90, 15);
                    switch (level){
                        case 1 :
                            lines.add(new Bullet(polar1,cartesian.x,cartesian.y,angle * i + rotation + 90,rotateImage(Hakurei_bullet_1,angle * i + rotation)));
                            break;
                        case 2 :
                            lines.add(new Bullet(polar1,cartesian.x,cartesian.y,angle * i + rotation + 90,rotateImage(Hakurei_bullet_4,angle * i + rotation)));
                            break;
                    }
                }
            }
        }
    }
    private void ImageEmitter (BufferedImage image, ArrayList lines) {
        int width = image.getWidth();
        int height = image.getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                Color color = new Color(rgb);
                if (color.getRed() == 255 && color.getGreen() == 255 && color.getBlue() == 255) {
                    lines.add(new Bullet(x - width / 2 + cartesian.x,y - height / 2 + cartesian.y,cartesian.x,cartesian.y));
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
        graphics.setFont(font1);
        //计算帧数
        frameRate.calculate();
        graphics.setColor(Color.ORANGE.WHITE);
        graphics.drawString(frameRate.getFrameRate(),30,30);
        graphics.drawString(String.format("%.2f",timer / 45) + "s",30,45);
        //处理玩家发射的子弹事件
        for (int i = 0; i < lines.size() - 1; ++i) {
            Point p = lines.get(i);
            if ( p != null) {
                graphics.drawImage(bullet,(int)p.getX() - bullet.getHeight() / 2,(int)p.getY() - bullet.getWidth() / 2,this);
                p.setLocation(p.getX(),p.getY() - 6);
                if (BoxTest(Hakurei_Reimu, p, cartesian) && !doGameOver) {
                    Reimu.addHurt();
                    lines.remove(i);
                }
                for (int j = 0; j < YinYangYu.size() - 1; j++) {
                    if (BoxTest(yin_yang_yu, p, YinYangYu.get(j).getPosition()) && !doGameOver) {
                        //YinYangYu.get(j).addHurt();
                        //lines.remove(i);
                    }
                }
                if ( isOutWindow(width,height,new Cartesian(p.x,p.y)) ) {
                    lines.remove(i);
                }
            }
        }
        //处理灵梦发射的子弹事件
        if (!doGameOver && level == 1) RoundEmitter(RoundEmitterNum,RoundEmitterRotation,bulletLists.get(1));
        if (!doGameOver && level == 2) RoundEmitter(30,0,bulletLists.get(1));
        for (int i = 0; i < bulletLists.get(1).size() - 1 ; i++) {
            Bullet p = bulletLists.get(1).get(i);
            if ( p != null ) {
                graphics.drawImage(p.image,(int)p.getX() - bullet2.getHeight() / 2,(int)p.getY() - bullet2.getWidth() / 2,this);
                if (BoxTest(player_image, p, position.Point()) && !doGameOver && level == 1) {
                    if (!doImmutable) player.addHurt();
                    bulletLists.get(1).remove(i);
                }
                if(distance(p.Point(),position.Point()) <= Hakurei_bullet_4.getWidth() / 2 && level == 2) {
                    if (!doImmutable) player.addHurt();
                    bulletLists.get(1).remove(i);
                }
                p.addRadius(addRadius);
                p.addTheta(addTheta);
                p.PolarPosition();
                if ( isOutWindow(width,height,p) ) {
                    bulletLists.get(1).remove(i);
                }
            }
        }
        //处理小实体的子弹事件
        if (enemy.size() >= 1){
            for (int i = 0; i < enemy.size() - 1; i++) {
            }
        }
        //阴阳玉子弹-梦想封印
        if (level == 2) {
            if (bulletLists.get(2).size() != 0) {
                for (int i = 0; i < bulletLists.get(2).size() - 1; i++) {
                    Bullet bullet1 = bulletLists.get(2).get(i);
                    if (bullet1 != null) {
                        if (bullet1.isContinue())
                        {
                            bullet1.addLife();
                        }   else {
                            bullet1.addPauseTime();
                        }
                        if (bullet1.getLifeTime() == 1) {
                            double deltaTheta = Math.toDegrees(Math.atan2(bullet1.getY() - position.y, bullet1.getX() - position.x));
                            bullet1.setTheta(deltaTheta);
                            switch (bullet1.stage) {
                                case 1 :
                                    bullet1.setImage(rotateImage(Hakurei_bullet1,deltaTheta + 90));
                                    break;
                                case 2 :
                                    bullet1.setImage(rotateImage(Hakurei_bullet2,deltaTheta + 90));
                                    break;
                                case 3 :
                                    bullet1.setImage(rotateImage(Hakurei_bullet3,deltaTheta + 90));
                                    break;
                            }
                        }
                        if (bullet1.getPauseTime() >= 50) {
                            bullet1.pause();
                            bullet1.resetPauseTime();
                        }
                        if (bullet1.getLifeTime() >= 50 && bullet1.stage <= 2) {
                            bullet1.resetLife();
                            bullet1.addStage();
                            bullet1.pause();
                            bullet1.addSpeed();
                            switch (bullet1.stage) {
                                case 1 :
                                    bullet1.setImage(Hakurei_bullet_2);
                                    break;
                                case 2:
                                    bullet1.setImage(Hakurei_bullet_3);
                                    break;
                            }
                        }
                        graphics.drawImage(bullet1.image,(int)bullet1.getX(),(int)bullet1.getY(),this);
                        if (bullet1.isContinue()) bullet1.setLocation(bullet1.getX() - Math.cos(Math.toRadians(bullet1.getTheta())) * bullet1.speed,bullet1.getY() - Math.sin(Math.toRadians(bullet1.getTheta())) * bullet1.speed);
                        if (BoxTest(player_image, bullet1, position.Point()) && !doGameOver) {
                            if (!doImmutable) player.addHurt();
                            bulletLists.get(2).remove(i);
                        }
                        if ( isOutWindow(width,height,bullet1)) {
                            bulletLists.get(2).remove(i);
                        }
                    }
                }
            }
        }
        //绘制阴阳玉并生成子弹
        if (level == 2) {
            for (int i = 0; i < YinYangYu.size(); i++) {
                OnmyouDama yinyangyu = YinYangYu.get(i);
                if (yinyangyu != null) {
                    yinyangyu.setOrigin(cartesian);
                    yinyangyu.RoundPosition(i * 60 + emitter * 4);
                    if (emitter % yinyangyu.emitterSpeed == 0 && !doGameOver) {
                        PolarCoordinate polar1 = new PolarCoordinate(60 * i + emitter * 4, 60);
                        bulletLists.get(2).add(new Bullet(polar1, cartesian.x, cartesian.y));
                        bulletLists.get(2).add(new Bullet(polar1.addRadius(20), cartesian.x , cartesian.y));
                    }
                    graphics.drawImage(yin_yang_yu, (int) yinyangyu.x - yin_yang_yu.getWidth() / 2, (int) yinyangyu.y - yin_yang_yu.getHeight() / 2, this);
                }
            }
        }
        //绘制新弹幕
        if (level == 3) {
            for (int i = 0; i < bulletLists.get(3).size() - 1; i++) {
                Bullet bullet1 = bulletLists.get(3).get(i);
                graphics.drawImage(bullet2,(int)bullet1.getX(),(int)bullet1.getY(),this);
                bullet1.toPosition();
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
        if (Reimu.proportion() <= 0 && level == 2) {
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
        final SimpleEasyTouhouFangame app = new SimpleEasyTouhouFangame();
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
