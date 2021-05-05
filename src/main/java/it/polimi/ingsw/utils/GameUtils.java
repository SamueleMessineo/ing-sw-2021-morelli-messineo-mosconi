package it.polimi.ingsw.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.Game;

import java.io.*;

public class GameUtils {

    public static void saveGameState(Game game, int roomID) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        String gameJSON = gson.toJson(game);

        File outputFile = new File("src/main/resources/games/" + roomID + ".json");
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
        Game game = null;

        try {
            reader = new BufferedReader(new FileReader("src/main/resources/games/" + roomID + ".json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        game = (Game) gson.fromJson(reader, Game.class);

        return game;
    }

}
