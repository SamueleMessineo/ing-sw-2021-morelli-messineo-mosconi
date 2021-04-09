package it.polimi.ingsw.model.player;
import it.polimi.ingsw.model.shared.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Structure containing a list of shelves the player can store resources onto.
 */
public class Warehouse {

    private final ArrayList<Shelf> shelves = new ArrayList<>();
    private final ArrayList<String> shelfNames = new ArrayList<>(Arrays.asList("top", "middle", "bottom"));

    public Warehouse() {
        for (int i = 1; i <= 3; i++) {
            shelves.add(new Shelf(i));
        }
    }

    public ArrayList<Shelf> getShelves() {
        return shelves;
    }

    public ArrayList<String> getShelfNames() {
        return shelfNames;
    }

    /**
     * Returns the index of a shelf in the shelf list given the shelf name.
     * @param shelf string containing the shelf name.
     * @return the index of the shelf in the list.
     */
    protected int getShelfIndex(String shelf) {
        shelf = shelf.toLowerCase().trim();
        return shelfNames.indexOf(shelf);
    }

    /**
     * Returns the Shelf object given the shelf name.
     * @param shelf string containing the shelf name.
     * @return the Shelf object corresponding to the given name.
     */
    public Shelf getShelf(String shelf){
        return shelves.get(getShelfIndex(shelf));
    }

    /**
     * Adds the given Shelf and shelf name to their respective lists.
     * @param newShelfName name of the new shelf.
     * @param shelf new shelf object.
     */
    public void addNewShelf(String newShelfName, Shelf shelf) {
        shelves.add(shelf);
        shelfNames.add(newShelfName);
    }

    /**
     * Returs a Map of the resources of a single shelf.
     * @param shelf name of the shelf.
     * @return the resources in the given shelf.
     */
    public Map<Resource, Integer> getShelfResources(String shelf){
        return shelves.get(getShelfIndex(shelf)).getResources();
    }

    /**
     * Checks whether a certain number of resources can be placed on a given shelf.
     * @param shelf name of the shelf.
     * @param resources Map of the resources to check.
     * @return if the placement is possible or not.
     */
    public boolean canPlaceOnShelf(String shelf, Map<Resource, Integer> resources){
        // TODO check that other shelves don't have same resource type
        Shelf currentShelf = shelves.get(getShelfIndex(shelf));
        return currentShelf.canPlace(resources);
    }

    /**
     * Places some resources on a given shelf.
     * @param shelf shelf name on which to place the resources.
     * @param resources resources to be placed.
     */
    public void placeOnShelf(String shelf, Map<Resource, Integer> resources) {
        int shelfIndex = getShelfIndex(shelf);
        shelves.get(shelfIndex).addResources(resources);
    }

    /**
     * Switches the resources between two shelves.
     * @param firstShelfName name of the first shelf.
     * @param secondShelfName name of the second shelf.
     */
    public void switchShelves(String firstShelfName, String secondShelfName){
        Shelf firstShelf = shelves.get(getShelfIndex(firstShelfName));
        Shelf secondShelf = shelves.get(getShelfIndex(secondShelfName));

        if (firstShelf.getResourceNumber() <= secondShelf.getMaxSize()
                && secondShelf.getResourceNumber() <= firstShelf.getMaxSize()) {

            Resource tempResourceType = firstShelf.getResourceType();
            int tempResourceNumber = firstShelf.getResourceNumber();
            if (tempResourceType != null) {
                firstShelf.useResources(new HashMap<>() {{
                    put(tempResourceType, tempResourceNumber);
                }});
            }
            if (secondShelf.getResourceType() != null) {
                firstShelf.addResources(new HashMap<>() {{
                    put(secondShelf.getResourceType(), secondShelf.getResourceNumber());
                }});
                secondShelf.useResources(new HashMap<>(){{put(secondShelf.getResourceType(), secondShelf.getResourceNumber());}});
            }
            if (tempResourceType != null) {
                secondShelf.addResources(new HashMap<>(){{put(tempResourceType, tempResourceNumber);}});
            }
        }
    }

    /**
     * Returns all the shelf resources grouped together.
     * @return all resources in a Map.
     */
    public Map<Resource, Integer> getResources(){
        Map<Resource, Integer> allResources = new HashMap<>(){{
            put(Resource.COIN, 0);
            put(Resource.STONE, 0);
            put(Resource.SHIELD, 0);
            put(Resource.SERVANT, 0);
        }};
        for (Shelf shelf : shelves) {
            if (shelf.getResourceType() != null) {
                allResources.put(shelf.getResourceType(),
                        allResources.get(shelf.getResourceType()) + shelf.getResourceNumber());
            }
        }

        return allResources;
    }
}
