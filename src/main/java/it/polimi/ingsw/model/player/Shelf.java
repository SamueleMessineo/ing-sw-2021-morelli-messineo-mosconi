package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.shared.Resource;

import java.util.HashMap;
import java.util.Map;

public class Shelf implements Storage {
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

    public Resource getResourceType() {
        return resourceType;
    }

    public int getResourceNumber() {
        return resourceNumber;
    }

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

    public int getMaxSize() {
        return maxSize;
    }

    public void addResources(Map<Resource, Integer> resources){
        if (resources.keySet().toArray().length == 1) {
            Map.Entry<Resource, Integer> entry = resources.entrySet().iterator().next();
            if ((resourceType == null || resourceType.equals(entry.getKey())) && entry.getValue() <= maxSize - resourceNumber) {
                resourceNumber += entry.getValue();
                resourceType = entry.getKey();
            }
        }
    }

    public boolean canPlace(Map<Resource, Integer> resources) {
        if (resources.keySet().toArray().length == 1) {
            Map.Entry<Resource, Integer> entry = resources.entrySet().iterator().next();
            return (resourceNumber == 0 || entry.getKey().equals(resourceType))
                    && entry.getValue() <= maxSize - resourceNumber;
        }
        return false;
    }

    public void useResources(Map<Resource, Integer> resources) {

        if (resources.keySet().toArray().length == 1) {
            Map.Entry<Resource, Integer> entry = resources.entrySet().iterator().next();
            if (entry.getKey().equals(resourceType) && entry.getValue() <= resourceNumber) {
                resourceNumber -= entry.getValue();
                if (resourceNumber == 0 && !fixed) resourceType = null;
            }
        }
    }
}
