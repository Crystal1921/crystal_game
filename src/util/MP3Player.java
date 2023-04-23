package util;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MP3Player {
    public static boolean isPlaying = false;
    private static String musicfilename;
    private static AdvancedPlayer player;

    public MP3Player(String filename) throws FileNotFoundException, JavaLayerException {
        musicfilename = filename;
        FileInputStream fis = new FileInputStream(filename);
        player = new AdvancedPlayer(fis, FactoryRegistry.systemRegistry().createAudioDevice());
    }

    public void play() throws JavaLayerException, FileNotFoundException {
        player.play();
    }

    public static void setFilename (String file) {
        musicfilename = file;
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
            isPlaying = true;
            try {
                FileInputStream fis = new FileInputStream("src/sound/Eternal_Night.mp3");
                player = new AdvancedPlayer(fis, FactoryRegistry.systemRegistry().createAudioDevice());
                player.play();
            } catch (FileNotFoundException | JavaLayerException e) {
                e.printStackTrace();
            }
        }
    }
}

