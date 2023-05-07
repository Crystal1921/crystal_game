package Thread;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MP3PlayerThread {
    public static boolean isPlaying = false;
    private static AdvancedPlayer player;

    public MP3PlayerThread(String filename) throws FileNotFoundException, JavaLayerException {
        FileInputStream fis = new FileInputStream(filename);
        player = new AdvancedPlayer(fis, FactoryRegistry.systemRegistry().createAudioDevice());
    }

    public void play() throws JavaLayerException, FileNotFoundException {
        player.play();
    }

    public static void setFilename () {
    }

    public static void main(String[] args) {
        try {
            MP3PlayerThread player = new MP3PlayerThread("src/sound/Eternal_Night.mp3");
            player.play();
        } catch (FileNotFoundException | JavaLayerException e) {
            e.printStackTrace();
        }
    }

    public static class MusicThread implements Runnable {
        public void run() {
            isPlaying = true;
            try {
                InputStream is = MP3PlayerThread.class.getClassLoader().getResourceAsStream("sound/Eternal_Night.mp3");
                if (is != null) {
                    player = new AdvancedPlayer(is, FactoryRegistry.systemRegistry().createAudioDevice());
                }
                player.play();
            } catch (JavaLayerException e) {
                e.printStackTrace();
            }
        }
    }
}

