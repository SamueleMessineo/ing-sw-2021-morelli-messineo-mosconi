package it.polimi.ingsw.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.shared.*;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;

import java.io.*;
import java.security.InvalidParameterException;
import java.util.*;

public class GameUtils {

    public static void saveGameState(Game game, int roomID) {
        String gamesFolderPath = "src/main/resources/games/";

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        String gameJSON = gson.toJson(game);

        new File(gamesFolderPath).mkdirs();
        File outputFile = new File(gamesFolderPath + roomID + ".json");
        FileWriter writer;
        try {
            writer = new FileWriter(outputFile);

            writer.write(gameJSON);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Game loadGameState(int roomID) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("src/main/resources/games/" + roomID + ".json"));
            return gson.fromJson(reader, Game.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<Resource, Integer> emptyResourceMap(){
        Map<Resource,Integer> emptyMap=new HashMap<>();
        emptyMap.put(Resource.SHIELD,0);
        emptyMap.put(Resource.COIN,0);
        emptyMap.put(Resource.SERVANT,0);
        emptyMap.put(Resource.STONE,0);
        return emptyMap;
    }

    public static Map<Resource, Integer> sumResourcesMaps(Map<Resource, Integer> map1, Map<Resource, Integer> map2){
        Map<Resource, Integer> mapToAdd1=emptyResourceMap();
        mapToAdd1.putAll(map1);
        Map<Resource, Integer> mapToAdd2=emptyResourceMap();
        mapToAdd2.putAll(map2);

        Map<Resource, Integer> sum=emptyResourceMap();

        for(Resource resource:sum.keySet()){
            sum.put(resource, mapToAdd1.get(resource) + mapToAdd2.get(resource));
        }
        return sum;
    }

    public static LinkedHashMap<Resource, Integer> sortResourceMapByValues(Map<Resource, Integer> resources) {
        LinkedHashMap<Resource, Integer> sorted = new LinkedHashMap<>(resources);

        List<Map.Entry<Resource, Integer>> entries = new ArrayList<>(sorted.entrySet());
        entries.sort((o1, o2) -> o2.getValue() - o1.getValue());
        sorted.clear();

        for (Map.Entry<Resource, Integer> e : entries) {
            sorted.put(e.getKey(), e.getValue());
        }
        return sorted;
    }

    public static int askIntegerInput(String message, int minBoundary, int maxBoundary, PrintStream output, Scanner input) {
        int selection;
        while (true) {
            output.println(message);
            try {
                selection = Integer.parseInt(input.nextLine());
                if (selection < minBoundary || selection > maxBoundary) {
                    throw new InvalidParameterException();
                } else {
                    break;
                }
            } catch (NumberFormatException | InvalidParameterException e) {
                output.println("selection not valid");
            }
        }
        return selection;
    }

    public static Map<Resource, Integer> incrementValueInResourceMap(Map<Resource, Integer> resourceMap, Resource resource, int amount) {
        if (resourceMap.containsKey(resource)) amount += resourceMap.get(resource);
        resourceMap.put(resource, amount);
        return resourceMap;
    }

    public static Resource convertMarbleToResource(Marble marble) {
        switch (marble) {
            case BLUE: // convert to shied
                return Resource.SHIELD;
            case GREY: // convert to stone
                return Resource.STONE;
            case PURPLE: // convert to servant
                return Resource.SERVANT;
            case YELLOW: // convert to coin:
                return Resource.COIN;
        }
        return null;
    }

    public static void debug(String string){
        System.out.println("\u001B[36m" + string + "\u001B[0m");
    }

    public static void writeGama(Game game, Integer roomID){
        try {
            FileOutputStream fileOut = new FileOutputStream("src/main/resources/games/" + roomID + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(game);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static Game readGame(Integer roomID){
        Game game = null;
        try {
            FileInputStream fileIn = new FileInputStream("src/main/resources/games/" + roomID + ".ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            game = (Game) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();

        }
        return game;
    }

    public static List<Integer> getFaithTrackPositionCoordinates(int pos) {
        switch (pos) {
            case 0:
                return Arrays.asList(12, 103);
            case 1:
                return Arrays.asList(54, 103);
            case 2:
                return Arrays.asList(96, 103);
            case 3:
                return Arrays.asList(96, 61);
            case 4:
                return Arrays.asList(96, 19);
            case 5:
                return Arrays.asList(139, 19);
            case 6:
                return Arrays.asList(181, 19);
            case 7:
                return Arrays.asList(223, 19);
            case 8:
                return Arrays.asList(265, 19);
            case 9:
                return Arrays.asList(308, 19);
            case 10:
                return Arrays.asList(308, 61);
            case 11:
                return Arrays.asList(308, 103);
            case 12:
                return Arrays.asList(350, 103);
            case 13:
                return Arrays.asList(392, 103);
            case 14:
                return Arrays.asList(434, 103);
            case 15:
                return Arrays.asList(476, 103);
            case 16:
                return Arrays.asList(518, 103);
            case 17:
                return Arrays.asList(518, 61);
            case 18:
                return Arrays.asList(518, 19);
            case 19:
                return Arrays.asList(561, 19);
            case 20:
                return Arrays.asList(603, 19);
            case 21:
                return Arrays.asList(645, 19);
            case 22:
                return Arrays.asList(687, 19);
            case 23:
                return Arrays.asList(729, 19);
            case 24:
                return Arrays.asList(771, 19);
            default:
                return null;
        }
    }

    public static List<Integer> getPopeTileCoordinates(int index) {
        switch (index) {
            case 0:
                return Arrays.asList(190,69);
            case 1:
                return Arrays.asList(402,28);
            case 2:
                return Arrays.asList(655,69);
            default:
                return null;
        }
    }

    public static ImageView getImageView(Resource resource) {
        Image resourceImage = new Image(Objects.requireNonNull(GameUtils.class.getClassLoader()
                .getResourceAsStream("images/resources/" + resource.name().toLowerCase() + ".png")));
        ImageView resourceImageView = new ImageView(resourceImage);
        resourceImageView.setPreserveRatio(true);
        return resourceImageView;
    }

    public static ImageView getImageView(DevelopmentCard card) {
        Image cardImage = new Image(Objects.requireNonNull(GameUtils.class.getClassLoader()
                .getResourceAsStream("images/development/development_" + card.getCardType().name().toLowerCase()
                        + "_" + card.getScore() + ".png")));
        ImageView cardImageView = new ImageView(cardImage);
        cardImageView.setPreserveRatio(true);
        return cardImageView;
    }

    public static ImageView getImageView(LeaderCard card) {
        Image cardImage = new Image(Objects.requireNonNull(GameUtils.class.getClassLoader()
                .getResourceAsStream("images/leaders/leader_" + card.getEffectScope().toLowerCase()
                        + "_" + card.getEffectObject().name().toLowerCase() + ".png")));
        ImageView cardImageView = new ImageView(cardImage);
        cardImageView.setPreserveRatio(true);
        return cardImageView;
    }

    public static void setDarkImageView(ImageView imageView, double value){
        ColorAdjust blackout = new ColorAdjust();
        blackout.setBrightness(-value);
        imageView.setEffect(blackout);
        imageView.setCache(true);
        imageView.setCacheHint(CacheHint.SPEED);
    }

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
            ImageView resourceImageView = GameUtils.getImageView(inputResource);
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
            ImageView resourceImageView = GameUtils.getImageView(outputResource);
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
}
