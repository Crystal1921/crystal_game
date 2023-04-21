package util;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MP3Player {
    private String filename;
    private AdvancedPlayer player;
    private AudioDevice audioDevice;

    public MP3Player(String filename) throws FileNotFoundException, JavaLayerException {
        this.filename = filename;
        FileInputStream fis = new FileInputStream(filename);
        player = new AdvancedPlayer(fis, FactoryRegistry.systemRegistry().createAudioDevice());
    }

    public void play() throws JavaLayerException {
        player.play();
    }

    public void stop() {
        player.stop();
    }
    public static void main(String[] args) {
        try {
            MP3Player player = new MP3Player("src/sound/Eternal_Night.mp3");
            player.play();
        } catch (FileNotFoundException | JavaLayerException e) {
            e.printStackTrace();
        }
    }

    public static class MusicThread implements Runnable {
        public void run() {
            try {
                MP3Player player = new MP3Player("src/sound/Eternal_Night.mp3");
                player.play();
            } catch (FileNotFoundException | JavaLayerException e) {
                e.printStackTrace();
            }
        }
    }
}
