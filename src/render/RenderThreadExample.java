package render;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RenderThreadExample extends JFrame implements Runnable {
    private volatile boolean running;
    private Thread gameThread;
    public RenderThreadExample (){
    }

    protected void  creadAndShowGUI() {
        setSize(320,240);
        setTitle("Render Example");
        setVisible( true );
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
    public void run() {
        running = true;
        while ( running ) {
            System.out.println("Looping");
            sleep(10);
        }
    }

    private void sleep ( long sleep) {
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected void onWindowClosing() {
        try {
            System.out.println("Thread Stop");
            running = false;
            gameThread.join();
            System.out.println("Stopped");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        final RenderThreadExample render = new RenderThreadExample();
        render.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                render.onWindowClosing();
            }
        });
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                render.creadAndShowGUI();
            }
        });
    }
}
