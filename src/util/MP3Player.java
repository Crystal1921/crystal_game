package util;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MP3Player {
<<<<<<< HEAD
    public static boolean isPlaying = false;
    private static String musicfilename;
    private static AdvancedPlayer player;

    public MP3Player(String filename) throws FileNotFoundException, JavaLayerException {
        musicfilename = filename;
=======
    private static String musicfilename;
    private AdvancedPlayer player;
    private AudioDevice audioDevice;

    public MP3Player(String filename) throws FileNotFoundException, JavaLayerException {
        this.musicfilename = filename;
>>>>>>> b803afd441c29eba8edec9a3bc006c6caf1299bd
        FileInputStream fis = new FileInputStream(filename);
        player = new AdvancedPlayer(fis, FactoryRegistry.systemRegistry().createAudioDevice());
    }

    public void play() throws JavaLayerException, FileNotFoundException {
        player.play();
    }

    public static void setFilename (String file) {
        musicfilename = file;
<<<<<<< HEAD
=======
    }

    public void stop() {
        player.stop();
>>>>>>> b803afd441c29eba8edec9a3bc006c6caf1299bd
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
<<<<<<< HEAD
                InputStream is = MP3Player.class.getClassLoader().getResourceAsStream("sound/Eternal_Night.mp3");
                if (is != null) {
                    player = new AdvancedPlayer(is, FactoryRegistry.systemRegistry().createAudioDevice());
                }
=======
                MP3Player player = new MP3Player(musicfilename);
>>>>>>> b803afd441c29eba8edec9a3bc006c6caf1299bd
                player.play();
            } catch (JavaLayerException e) {
                e.printStackTrace();
            }
        }
    }
}

