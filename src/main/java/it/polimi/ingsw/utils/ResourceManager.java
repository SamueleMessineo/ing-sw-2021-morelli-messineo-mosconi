package it.polimi.ingsw.utils;

import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.model.shared.ProductionPower;
import it.polimi.ingsw.model.shared.Resource;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.CacheHint;
import javafx.scene.Cursor;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
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
     * Plays a click sound.
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
     * Builds the ImageView of the arrows in the marbles grid.
     * @param direction the direction of the arrow.
     * @return the ImageView of the arrow.
     */
    public static ImageView getMarblesArrow(String direction) {
        Image arrowImage = new Image(Objects.requireNonNull(ResourceManager.class.getClassLoader()
                .getResourceAsStream("images/marbles/marbles_arrow_"+direction+".png")));
        ImageView imageView = new ImageView(arrowImage);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(60);
        imageView.setFitWidth(60);
        imageView.setCursor(Cursor.HAND);
        GridPane.setValignment(imageView, VPos.CENTER);
        GridPane.setHalignment(imageView, HPos.CENTER);
        return imageView;
    }

    public static DropShadow getGlowEffect() {
        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(Color.rgb(255, 255, 0, 0.7));
        borderGlow.setOffsetX(0f);
        borderGlow.setOffsetY(0f);
        borderGlow.setHeight(90);
        borderGlow.setWidth(90);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().setAll(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(borderGlow.heightProperty(), 0),
                        new KeyValue(borderGlow.widthProperty(), 0)
                ),
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(borderGlow.heightProperty(), 40),
                        new KeyValue(borderGlow.widthProperty(), 40)
                ),
                new KeyFrame(Duration.millis(1500),
                        new KeyValue(borderGlow.heightProperty(), 50),
                        new KeyValue(borderGlow.widthProperty(), 50)
                )
        );
        timeline.setAutoReverse(true);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        return borderGlow;
    }
}
