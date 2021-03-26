package it.polimi.ingsw.model.player;
import it.polimi.ingsw.model.shared.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Warehouse {

    private Shelf[] shelves = new Shelf[3];

    public Warehouse() {
        for (int i = 1; i <= 3; i++) {
            shelves[i-1] = new Shelf(i);
        }
    }

    private int getShelfIndex(String shelf) {
        shelf = shelf.toLowerCase().trim();
        return shelf.equals("top") ? 0 : shelf.equals("middle") ? 1 : 2;
    }

    //@ensures if (shelf == "top" => result.size <= 1) else
    //@ if (shelf == "middle" => result.size <= 2) else
    //@ if (shelf == "bottom" => result.size <= 3)
    public Shelf getShelf(String shelf){
        return shelves[getShelfIndex(shelf)];
    }

    public Map<Resource, Integer> getShelfResources(String shelf){
        return shelves[getShelfIndex(shelf)].getContents();
    }

    //@requires resources.size <= 3 && forAll (Resource i in resources;;i=resources.get(0));
    public boolean canPlaceOnShelf(String shelf, ArrayList<Resource> resources){
        Shelf currentShelf = shelves[getShelfIndex(shelf)];
        return currentShelf.canPlace(resources);
    }

    public void placeOnShelf(String shelf, ArrayList<Resource> resources) {
        int shelfIndex = getShelfIndex(shelf);
        shelves[shelfIndex].addResources(resources);
    }

    public void switchShelves(String firstShelfName, String secondShelfName){
        if (getShelf(firstShelf).getShelf().size() <= getShelf(secondShelf).getMaxSize() && getShelf(secondShelf).getShelf().size() <= getShelf(firstShelf).getMaxSize()){

            Resource rfirst;
            rfirst = getShelf(firstShelf).getShelf().get(0);
            int firstSize = getShelf(firstShelf).getMaxSize();

            Resource rsecond;
            rsecond = getShelf(secondShelf).getShelf().get(0);
            int secondSize = getShelf(secondShelf).getMaxSize();

            for (int i = 0; i<firstSize;i++){
                getShelf(secondShelf).addResource(rfirst);
            }
            for (int i =0; i<secondSize; i++){
                getShelf(firstShelf).addResource(rsecond);
            }
        }

//        Shelf firstShelf = shelves[getShelfIndex(firstShelfName)];
//        Shelf secondShelf = shelves[getShelfIndex(secondShelfName)];
//
//        if (firstShelf.getResourceNumber() > secondShelf.getMaxSize()
//                || secondShelf.getResourceNumber() > firstShelf.getMaxSize()) {
//
//        }

    }
    public void useResources(ArrayList<Resource> resources){
        for (Resource r :
             resources) {
            if (topShelf.getShelf().get(0) == resources.get(0))topShelf.getShelf().remove(0);
            else  if (middleShelf.getShelf().get(0) == resources.get(0))middleShelf.getShelf().remove(0);
            else  if (bottomShelf.getShelf().get(0) == resources.get(0))bottomShelf.getShelf().remove(0);
            else System.out.println("risorse non disponibili");
        }
    }

    public Map<Resource, Integer> getResources(){
        Map<Resource, Integer> allResources = new HashMap<>();
        for (Shelf shelf : shelves) {
            allResources.put(shelf.getResourceType(),
                    allResources.get(shelf.getResourceType()) + shelf.getResourceNumber());
        }

        return allResources;
    }
}
