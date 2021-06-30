package it.polimi.ingsw.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.shared.*;
import java.io.*;
import java.security.InvalidParameterException;
import java.util.*;

public class GameUtils {

    /**
     * Creates a new map with all the resources as keys with a value of 0.
     * @return the create emptyResourceMap.
     */
    public static Map<Resource, Integer> emptyResourceMap(){
        Map<Resource,Integer> emptyMap=new HashMap<>();
        emptyMap.put(Resource.SHIELD,0);
        emptyMap.put(Resource.COIN,0);
        emptyMap.put(Resource.SERVANT,0);
        emptyMap.put(Resource.STONE,0);
        return emptyMap;
    }

    /**
     * Sums the values of each key of 2 Maps<Resource, Integer>
     * @param map1 first map.
     * @param map2 second map.
     * @return a new map with all the resources and values as the sum of the values of the input map for that resource.
     */
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

    /**
     * Sorts a resource Map in descending order.
     * @param resources a Map<Resource, Integer>
     * @return a LinkedHashMap with same keys and values as the input Map but sorted in descending order.
     */
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

    /**
     * Takes an input from the user, converts it to int, checks if is in a range and prints it on the PrintStream.
     * @param message a String that ask a number to the user.
     * @param minBoundary minimum value of the user input.
     * @param maxBoundary maximum value of the user input.
     * @param output a PrintStream on which the converted int is printed.
     * @param input a Scanner to read what the user is writing.
     * @return the user char input converted to an int.
     */
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

    /**
     * Increments the resource value of an amount.
     * @param resourceMap a Map<Resource, Integer>.
     * @param resource the resource of which the value is to increment.
     * @param amount how much the value of resource has to be incremented.
     * @return the input map with an incremented value.
     */
    public static Map<Resource, Integer> incrementValueInResourceMap(Map<Resource, Integer> resourceMap, Resource resource, int amount) {
        if (resourceMap.containsKey(resource)) amount += resourceMap.get(resource);
        resourceMap.put(resource, amount);
        return resourceMap;
    }

    /**
     * Converts a marble to its resource.
     * @param marble the marble to convert.
     * @return the resource associated to the marble.
     */
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

    /**
     * Used for debug, prints a string in a light blue color.
     * @param string to print.
     */
    public static void debug(String string){
        System.out.println("\u001B[36m" + string + "\u001B[0m");
    }

    /**
     * Checks the OS and finds the path to the directory in which the games should be saved called "/.mor".
     * @return path of the directory in which the game are to be saved, if it does not exists it is created.
     */
    private static String getPath() {
        String OS = (System.getProperty("os.name")).toUpperCase();
        String workingDirectory;
        if (OS.contains("WIN")) {
            // windows
            workingDirectory = System.getenv("AppData");
            workingDirectory += "/mor";
        } else if (OS.contains("MAC")){
            // mac
            workingDirectory = System.getProperty("user.home");
            workingDirectory += "/Library/Application Support/mor";
        } else {
            // linux
            workingDirectory = System.getProperty("user.home");
            workingDirectory += "/.mor";
        }
        new File(workingDirectory).mkdirs();
        return workingDirectory;
    }

    /**
     * Saves the game in the "/.mor" directory.
     * @param game to save.
     * @param roomID id of the game room, used to identify the file.
     */
    public static void writeGame(Game game, Integer roomID){
        String basePath = getPath();
        try {
            FileOutputStream fileOut = new FileOutputStream(basePath + "/" + roomID + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(game);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    /**
     * Reads a game from the "/.mor" directory.
     * @param roomID the id of the room associated to the game, is used to identify the file.
     * @return "/.mor/"+roomId+".ser"
     */
    public static Game readGame(Integer roomID){
        Game game = null;
        String basePath = getPath();
        try {
            FileInputStream fileIn = new FileInputStream(basePath + "/" + roomID + ".ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            game = (Game) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();

        }
        return game;
    }

    /**
     * Deletes a saved game from memory.
     * @param roomId the id Associated to the game that has to be deleted.
     */
    public static void deleteSavedGame(int roomId){
        String basePath = getPath();
        try {
            File file = new File(basePath + "/" + roomId + ".ser");
            file.delete();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Gets the display coordinates of a faith track position.
     * @param pos an integer between 0 and 24.
     * @return the coordinates associated with pos.
     */
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

    /**
     * Gets the display coordinates of a PopeTile.
     * @param index an integer between 0 and 2.
     * @return the coordinates associated with index.
     */
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
}
