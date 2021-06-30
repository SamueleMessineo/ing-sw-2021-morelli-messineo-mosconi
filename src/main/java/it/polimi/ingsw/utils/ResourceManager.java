package it.polimi.ingsw.utils;

import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.model.shared.ProductionPower;
import it.polimi.ingsw.model.shared.Resource;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Objects;

public class ResourceManager {
    private static MediaPlayer mediaPlayer = null;
    private static AudioClip buttonClickSound = null;

    /**
     * Sets the music and click sound.
     */
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

    /**
     * Starts the background music.
     */
    public static void playBackgroundMusic() {
        mediaPlayer.play();
    }

    /**
     * Toggles sounds.
     */
    public static void toggleSound() {
        mediaPlayer.setMute(!mediaPlayer.isMute());
        buttonClickSound.setVolume(buttonClickSound.getVolume() == 0 ? 0.05 : 0);
    }

    /**
     * Starts click sounds.
     */
    public static void playClickSound() {
        buttonClickSound.play();
    }

    /**
     * Gets the ImageView of a resource.
     * @param resource resource of which the view is wanted.
     * @return the ImageView associated with the resource.
     */
    public static ImageView getImageView(Resource resource) {
        Image resourceImage = new Image(Objects.requireNonNull(GameUtils.class.getClassLoader()
                .getResourceAsStream("images/resources/" + resource.name().toLowerCase() + ".png")));
        ImageView resourceImageView = new ImageView(resourceImage);
        resourceImageView.setPreserveRatio(true);
        return resourceImageView;
    }

    /**
     * Gets the ImageView of a developmentCard.
     * @param card developmentCard of which the view is wanted.
     * @return the ImageView associated with the card.
     */
    public static ImageView getImageView(DevelopmentCard card) {
        Image cardImage = new Image(Objects.requireNonNull(GameUtils.class.getClassLoader()
                .getResourceAsStream("images/development/development_" + card.getCardType().name().toLowerCase()
                        + "_" + card.getScore() + ".png")));
        ImageView cardImageView = new ImageView(cardImage);
        cardImageView.setPreserveRatio(true);
        return cardImageView;
    }

    /**
     * Gets the ImageView of a leaderCard.
     * @param card leaderCard of which the view is wanted.
     * @return the ImageView associated with the card.
     */
    public static ImageView getImageView(LeaderCard card) {
        Image cardImage = new Image(Objects.requireNonNull(GameUtils.class.getClassLoader()
                .getResourceAsStream("images/leaders/leader_" + card.getEffectScope().toLowerCase()
                        + "_" + card.getEffectObject().name().toLowerCase() + ".png")));
        ImageView cardImageView = new ImageView(cardImage);
        cardImageView.setPreserveRatio(true);
        return cardImageView;
    }

    /**
     * Darkens the colors of an imageView of a double value.
     * @param imageView that has to change its colors.
     * @param value of how much to darkens the image.
     */
    public static void setDarkImageView(ImageView imageView, double value){
        ColorAdjust blackout = new ColorAdjust();
        blackout.setBrightness(-value);
        imageView.setEffect(blackout);
        imageView.setCache(true);
        imageView.setCacheHint(CacheHint.SPEED);
    }

    /**
     * Builds the Image of a ProductionPower.
     * @param power the production power of which the image has to be created.
     * @return an AnchorPane containing the Image associated with the ProductionPower.
     */
    public static AnchorPane buildProductionPowerBook(ProductionPower power) {
        AnchorPane powerPane = new AnchorPane();
        powerPane.setPrefSize(212, 150);
        Image bookImage = new Image(Objects.requireNonNull(GameUtils.class.getClassLoader()
                .getResourceAsStream("images/board/production_book.png")));
        ImageView bookImageView = new ImageView(bookImage);
        bookImageView.setPreserveRatio(true);
        bookImageView.setFitHeight(150);
        bookImageView.setFitWidth(212);
        bookImageView.setLayoutX(0);
        bookImageView.setLayoutY(0);
        powerPane.getChildren().add(bookImageView);
        HBox singleCardPowerContainer = new HBox();
        singleCardPowerContainer.setPrefSize(212, 150);
        singleCardPowerContainer.setAlignment(Pos.CENTER);
        singleCardPowerContainer.setSpacing(60);
        VBox inputContainer = new VBox();
        inputContainer.setAlignment(Pos.CENTER);
        for (Resource inputResource : power.getInput().keySet()) {
            HBox singleResourceContainer = new HBox();
            singleResourceContainer.setAlignment(Pos.CENTER);
            singleResourceContainer.setSpacing(3);
            ImageView resourceImageView = ResourceManager.getImageView(inputResource);
            resourceImageView.setFitWidth(35);
            resourceImageView.setFitHeight(35);
            singleResourceContainer.getChildren().addAll(
                    new Text(power.getInput().get(inputResource).toString()), resourceImageView);
            inputContainer.getChildren().add(singleResourceContainer);
        }
        singleCardPowerContainer.getChildren().add(inputContainer);
        VBox outputContainer = new VBox();
        outputContainer.setAlignment(Pos.CENTER);
        for (Resource outputResource : power.getOutput().keySet()) {
            HBox singleResourceContainer = new HBox();
            singleResourceContainer.setAlignment(Pos.CENTER);
            singleResourceContainer.setSpacing(3);
            ImageView resourceImageView = ResourceManager.getImageView(outputResource);
            resourceImageView.setFitWidth(35);
            resourceImageView.setFitHeight(35);
            singleResourceContainer.getChildren().addAll(
                    new Text(power.getOutput().get(outputResource).toString()), resourceImageView);
            outputContainer.getChildren().add(singleResourceContainer);
        }
        singleCardPowerContainer.getChildren().add(outputContainer);
        singleCardPowerContainer.setLayoutX(0);
        singleCardPowerContainer.setLayoutY(0);
        powerPane.getChildren().add(singleCardPowerContainer);
        return powerPane;
    }

    /**
     * Gets the Inkwell image.
     * @return inkwell image.
     */
    public static Image getInkwell() {
        return new Image(Objects.requireNonNull(GameUtils.class.getClassLoader()
                .getResourceAsStream("images/punchboard/calamaio.png")));
    }
}
