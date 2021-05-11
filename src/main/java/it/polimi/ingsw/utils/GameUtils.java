package it.polimi.ingsw.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.shared.Resource;

import java.io.*;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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
}
