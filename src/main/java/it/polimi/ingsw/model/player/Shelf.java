package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.utils.GameUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * The shelf that makes up the warehouse, it hold a maximum
 * number of resources all of the same type.
 */
public class Shelf implements Storage, Serializable {
    private Resource resourceType=Resource.ANY;
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
        Map<Resource, Integer> contents = GameUtils.emptyResourceMap();
        if (resourceType != Resource.ANY) {
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
            if ((resourceType == Resource.ANY || resourceType.equals(entry.getKey())) && entry.getValue() <= maxSize - resourceNumber) {
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
        Map<Resource,Integer> resourcesToUse= GameUtils.emptyResourceMap();
        resourcesToUse.putAll(resources);
        Resource resourceToUse = Resource.ANY;
        int i=0;
        for(Resource resource: resourcesToUse.keySet()){
            if(resourcesToUse.get(resource)==0)
                i++;
            else
                resourceToUse=resource;
        }
        
        if(i!=3)
            return;

        if(resourceToUse.equals(resourceType) && resourcesToUse.get(resourceToUse)<=resourceNumber){
            resourceNumber-=resourcesToUse.get(resourceToUse);
            if(resourceNumber==0 && !fixed) resourceType=Resource.ANY;
        }
    }

    @Override
    public String toString() {
        if (resourceType==Resource.ANY
        ){
            return "";
        } else if(resourceNumber == 0){
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
