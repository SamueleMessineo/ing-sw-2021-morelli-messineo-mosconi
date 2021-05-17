package it.polimi.ingsw.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.shared.PopesFavorTileState;
import it.polimi.ingsw.model.shared.Resource;

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

}
