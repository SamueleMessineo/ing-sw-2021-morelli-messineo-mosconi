package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.shared.Resource;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * The shelf that makes up the warehouse, it hold a maximum
 * number of resources all of the same type.
 */
public class Shelf implements Storage, Serializable {
    private Resource resourceType=null;
    private int resourceNumber=0;
    private final int maxSize;
    private boolean fixed = false;

    public Shelf(int maxSize) {
        this.maxSize = maxSize;
    }

    public Shelf (int maxSize, Resource initialResourceType) {
        this.maxSize = maxSize;
        resourceType = initialResourceType;
    }

    /**
     * Returns the type of the resource type of the shelf.
     * @return The resource type of the shelf.
     */
    public Resource getResourceType() {
        return resourceType;
    }

    /**
     * Returns the number of resources currently placed on this shelf.
     * @return The number of resources currently placed on this shelf.
     */
    public int getResourceNumber() {
        return resourceNumber;
    }

    /**
     * Returns the resources placed on this shelf in the form of a Map.
     * @return The map of the resources on this shelf.
     */
    public Map<Resource, Integer> getResources() {
        Map<Resource, Integer> contents = new HashMap<>();
        contents.put(Resource.SHIELD, 0);
        contents.put(Resource.COIN, 0);
        contents.put(Resource.SERVANT, 0);
        contents.put(Resource.STONE, 0);
        if (resourceType != null) {
            contents.put(resourceType, resourceNumber);
        }
        return contents;
    }

    /**
     * Returns the maximum number of resources that are placeable on this shelf.
     * @return The maximum number of resources allowed on this shelf.
     */
    public int getMaxSize() {
        return maxSize;
    }

    /**
     * Places the given resources on the shelf.
     * @param resources The map of resources to be added.
     */
    public void addResources(Map<Resource, Integer> resources){
        if (resources.keySet().toArray().length == 1) {
            Map.Entry<Resource, Integer> entry = resources.entrySet().iterator().next();
            if ((resourceType == null || resourceType.equals(entry.getKey())) && entry.getValue() <= maxSize - resourceNumber) {
                resourceNumber += entry.getValue();
                resourceType = entry.getKey();
            }
        }
    }

    /**
     * Checks if the given resources can be placed on the shelf.
     * @param resources The map of resources whose placement to check.
     * @return True if the resources can be placed, false otherwise.
     */
    public boolean canPlace(Map<Resource, Integer> resources) {
        if (resources.keySet().toArray().length == 1) {
            Map.Entry<Resource, Integer> entry = resources.entrySet().iterator().next();
            return (resourceNumber == 0 || entry.getKey().equals(resourceType))
                    && entry.getValue() <= maxSize - resourceNumber;
        }
        return false;
    }

    /**
     * Removes the given resources from the shelf.
     * @param resources The map of resources to remove from the shelf.
     */
    public void useResources(Map<Resource, Integer> resources) {

        if (resources.keySet().toArray().length == 1) {
            Map.Entry<Resource, Integer> entry = resources.entrySet().iterator().next();
            if (entry.getKey().equals(resourceType) && entry.getValue() <= resourceNumber) {
                resourceNumber -= entry.getValue();
                if (resourceNumber == 0 && !fixed) resourceType = null;
            }
        }
    }

    @Override
    public String toString() {
        if (resourceType==null || resourceNumber == 0){
            return "EMPTY";
        }
        else{
            return "Shelf{" +
                    "resourceType=" + resourceType +
                    ", resourceNumber=" + resourceNumber +
                    '}';
        }
    }
}
