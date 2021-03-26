package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.shared.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Shelf {
    private Resource resourceType=null;
    private int resourceNumber=0;
    private int maxSize;

    public Shelf(int maxSize) {
        this.maxSize = maxSize;
    }

    public Resource getResourceType() {
        return resourceType;
    }

    public int getResourceNumber() {
        return resourceNumber;
    }

    public Map<Resource, Integer> getContents() {
        Map<Resource, Integer> contents = new HashMap<>();
        contents.put(resourceType, resourceNumber);
        return contents;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void addResources(ArrayList<Resource> resources){
        if (canPlace(resources)) {
            resourceNumber += resources.size();
        }
    }

    public boolean canPlace(ArrayList<Resource> resources) {
        return (resourceNumber == 0 || resources.get(0) == resourceType) && resources.size() <= maxSize - resourceNumber;
    }
}
