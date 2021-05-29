package it.polimi.ingsw.utils;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.Objects;

public class ResourceManager {
    private static MediaPlayer mediaPlayer = null;
    private static AudioClip buttonClickSound = null;

    public static void initializeSound() {
        if (mediaPlayer != null) mediaPlayer.stop();
        Media backgroundMusic = new Media(Objects.requireNonNull(ResourceManager.class.getClassLoader()
                .getResource("music/bg.mp3")).toExternalForm());
        mediaPlayer = new MediaPlayer(backgroundMusic);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(0.2);
        mediaPlayer.setMute(true);
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.seek(Duration.ZERO);
            mediaPlayer.play();
        });
        buttonClickSound = new AudioClip(Objects.requireNonNull(ResourceManager.class.getClassLoader()
                .getResource("music/buttonClick.mp3")).toString());
        buttonClickSound.setVolume(0);
    }

    public static void playBackgroundMusic() {
        mediaPlayer.play();
    }

    public static void toggleSound() {
        mediaPlayer.setMute(!mediaPlayer.isMute());
        buttonClickSound.setVolume(buttonClickSound.getVolume() == 0 ? 0.05 : 0);
    }

    public static void playClickSound() {
        buttonClickSound.play();
    }
}
