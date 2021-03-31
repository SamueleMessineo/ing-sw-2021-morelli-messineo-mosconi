package it.polimi.ingsw.model.player;
import it.polimi.ingsw.model.shared.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class Warehouse {

//    private final Shelf[] shelves = new Shelf[3];
    private final ArrayList<Shelf> shelves = new ArrayList<>();

    private final ArrayList<String> shelfNames = new ArrayList<>(Arrays.asList("top", "middle", "bottom"));

    public Warehouse() {
        for (int i = 1; i <= 3; i++) {
//            shelves[i-1] = new Shelf(i);
            shelves.add(new Shelf(i, false));
        }
    }

    public ArrayList<Shelf> getShelves() {
        return shelves;
    }

    public ArrayList<String> getShelfNames() {
        return shelfNames;
    }

    protected int getShelfIndex(String shelf) {
        shelf = shelf.toLowerCase().trim();
        return shelfNames.indexOf(shelf);
    }

    //@ensures if (shelf == "top" => result.size <= 1) else
    //@ if (shelf == "middle" => result.size <= 2) else
    //@ if (shelf == "bottom" => result.size <= 3)
    public Shelf getShelf(String shelf){
        return shelves.get(getShelfIndex(shelf));
    }

    public Map<Resource, Integer> getShelfResources(String shelf){
        return shelves.get(getShelfIndex(shelf)).getResources();
    }

    //@requires resources.size <= 3 && forAll (Resource i in resources;;i=resources.get(0));
    public boolean canPlaceOnShelf(String shelf, Map<Resource, Integer> resources){
        Shelf currentShelf = shelves.get(getShelfIndex(shelf));
        return currentShelf.canPlace(resources);
    }

    public void placeOnShelf(String shelf, Map<Resource, Integer> resources) {
        int shelfIndex = getShelfIndex(shelf);
        shelves.get(shelfIndex).addResources(resources);
    }

    public void switchShelves(String firstShelfName, String secondShelfName){
        Shelf firstShelf = shelves.get(getShelfIndex(firstShelfName));
        Shelf secondShelf = shelves.get(getShelfIndex(secondShelfName));

        if (firstShelf.getResourceNumber() <= secondShelf.getMaxSize()
                && secondShelf.getResourceNumber() <= firstShelf.getMaxSize()) {

            Resource tempResourceType = firstShelf.getResourceType();
            int tempResourceNumber = firstShelf.getResourceNumber();
            firstShelf.useResources(new HashMap<>(){{put(tempResourceType, tempResourceNumber);}});
            firstShelf.addResources(new HashMap<>(){{put(secondShelf.getResourceType(), secondShelf.getResourceNumber());}});
            secondShelf.useResources(new HashMap<>(){{put(secondShelf.getResourceType(), secondShelf.getResourceNumber());}});
            secondShelf.addResources(new HashMap<>(){{put(tempResourceType, tempResourceNumber);}});
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
