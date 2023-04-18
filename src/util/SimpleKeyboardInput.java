package util;

import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SimpleKeyboardInput implements KeyListener {
    private boolean[] keys;
    public SimpleKeyboardInput () {
        keys = new boolean[ 256 ];
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }

    public synchronized boolean keyDown(int keyCode) {
        return keys [ keyCode ];
    }

    @Override
    public synchronized void keyPressed(@NotNull KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode >= 0 && keyCode < keys.length) {
            keys [ keyCode ] = true;
        }
    }

    @Override
    public synchronized void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode >= 0 && keyCode < keys.length) {
            keys [ keyCode ] = false;
        }
    }
}
