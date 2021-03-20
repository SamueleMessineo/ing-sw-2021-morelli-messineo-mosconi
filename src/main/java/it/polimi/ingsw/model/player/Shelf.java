package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.shared.Resource;

import java.util.ArrayList;

public class Shelf {
    private ArrayList<Resource> shelf = new ArrayList<Resource>();
    private int maxSize;

    public Shelf( int maxSize) {
        this.maxSize = maxSize;
    }

    public ArrayList<Resource> getShelf() {
        return shelf;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public boolean checkTypeConsistency(Resource resource){
        if (shelf.isEmpty()) return true;
        else return (resource == shelf.get(0));
    }

    public void addResource(Resource resource){
        if(checkTypeConsistency(resource)) {
            shelf.add(resource);
        }
    }
}
