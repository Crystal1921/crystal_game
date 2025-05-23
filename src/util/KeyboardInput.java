package util;

import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInput implements KeyListener {
    private final boolean[] keys;
    private final int[] polled;
    public KeyboardInput() {
        keys = new boolean[ 256 ];
        polled = new int[ 256 ];
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }

    public synchronized boolean keyDown(int keyCode) {
        return polled [ keyCode ] > 0;
    }
    public synchronized boolean keyDownOnce(int keyCode) {
        return polled [ keyCode ] == 1;
    }


    public synchronized void poll() {
        for (int i = 0; i < keys.length; i++) {
            if (keys[i]) {
                polled[i]++;
            }
            else polled[i] = 0;
        }
    }

    @Override
    public synchronized void keyPressed(@NotNull KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode >= 0 && keyCode < keys.length) {
            keys [ keyCode ] = true;
        }
    }

    @Override
    public synchronized void keyReleased(@NotNull KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode >= 0 && keyCode < keys.length) {
            keys [ keyCode ] = false;
        }
    }
}
