package it.polimi.ingsw.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.shared.Resource;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

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
}
