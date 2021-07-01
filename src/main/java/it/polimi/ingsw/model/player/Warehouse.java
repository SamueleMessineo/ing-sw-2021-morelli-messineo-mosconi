package it.polimi.ingsw.model.player;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.shared.*;
import it.polimi.ingsw.utils.GameUtils;
import it.polimi.ingsw.view.Display;

import java.io.Serializable;
import java.util.*;

/**
 * Structure containing a list of shelves the player can store resources onto.
 */
public class Warehouse implements Serializable {

    private final ArrayList<Shelf> shelves = new ArrayList<>();
    private final ArrayList<String> shelfNames = new ArrayList<>(Arrays.asList("top", "middle", "bottom"));

    /**
     * Warehouse class constructor.
     */
    public Warehouse() {
        for (int i = 1; i <= 3; i++) {
            shelves.add(new Shelf(i));
        }
    }

    /**
     * Returns the list of Shelves in the warehouse.
     * @return The list of shelves.
     */
    public ArrayList<Shelf> getShelves() {
        return shelves;
    }

    /**
     * Returns the list of names of the shelves in the warehouse.
     * @return The list of shelf names.
     */
    public ArrayList<String> getShelfNames() {
        return shelfNames;
    }

    /**
     * Returns the index of a shelf in the shelf list given the shelf name.
     * @param shelf string containing the shelf name.
     * @return the index of the shelf in the list. If is not contained it is returned -1
     */
    protected int getShelfIndex(String shelf) {
        shelf = shelf.toLowerCase().trim();
        if(shelfNames.contains(shelf)) {
            return shelfNames.indexOf(shelf);
        } else return -1;
    }

    /**
     * Returns the Shelf object given the shelf name.
     * @param shelf string containing the shelf name.
     * @return the Shelf object corresponding to the given name.
     */
    public Shelf getShelf(String shelf){
        if (getShelfIndex(shelf) == -1)return null;
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
        Shelf currentShelf = shelves.get(getShelfIndex(shelf));
        for (Resource resource:
             resources.keySet()) {
            if(resources.get(resource) != 0){
                for (Shelf shelf1:
                     shelves) {
                    if(shelf1!= currentShelf && shelf1.getResourceType()!=null && shelf1.getResourceType().equals(resource))return false;
                }
            }
        }

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
    public boolean switchShelves(String firstShelfName, String secondShelfName){
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
           return true;
        }
        return false;
    }

    /**
     * Returns all the shelf resources grouped together.
     * @return all resources in a Map.
     */
    public Map<Resource, Integer> getResources(){
        Map<Resource, Integer> allResources = GameUtils.emptyResourceMap();
        for (Shelf shelf : shelves) {
            if (shelf.getResourceType() != Resource.ANY) {
                allResources.put(shelf.getResourceType(),
                        allResources.get(shelf.getResourceType()) + shelf.getResourceNumber());
            }
        }

        return allResources;
    }

    /**
     * Check the contents of two shelves can be swapped
     * @param shelf1Name name of the first shelf
     * @param shelf2Name name of the second shelf
     * @return true if contents of shelf1 and shelf2 can be swapped, false otherwise
     */
    public boolean canSwitchShelves(String shelf1Name, String shelf2Name) {
        Shelf shelf1 = getShelf(shelf1Name);
        Shelf shelf2 = getShelf(shelf2Name);
        if (shelf1 == null || shelf2 == null) return false;
        if (shelf1.getResourceNumber() == 0 && shelf2.getResourceNumber() == 0) return false;
        if (shelves.indexOf(shelf1) <= 2 && shelves.indexOf(shelf2) <= 2) {
            return shelf1.getResourceNumber() <= shelf2.getMaxSize() &&
                    shelf2.getResourceNumber() <= shelf1.getMaxSize();
        } else {
            return (shelf1.getResourceType().equals(shelf2.getResourceType())
                    || shelf1.getResourceType().equals(Resource.ANY)
                    || shelf2.getResourceType().equals(Resource.ANY))
                    && shelf1.getResourceNumber() <= shelf2.getMaxSize()
                    && shelf2.getResourceNumber() <= shelf1.getMaxSize();
        }
    }

    /**
     * Checks if some resources can be placed in the warehouse.
     * @param resourcesToPlace the resources that the player is trying to place.
     * @return true if the resources can be placed in the warehouse according to the game rules.
     */
    public boolean canPlaceResources(Map<Resource,Integer> resourcesToPlace){
        resourcesToPlace = GameUtils.sumResourcesMaps(resourcesToPlace, GameUtils.emptyResourceMap());
        Map<Resource,Integer> resourcesInWarehouse=new HashMap<>(getResources());
        int numOfResourcesToPlaceInWarehouse=0;
        int maxResourcesInWarehouse=0;

        Map<Resource,Integer> resourcesToPlaceInWarehouse=GameUtils.sumResourcesMaps(resourcesToPlace,resourcesInWarehouse);
        for(Resource resource:resourcesToPlaceInWarehouse.keySet()){
            numOfResourcesToPlaceInWarehouse+=resourcesToPlaceInWarehouse.get(resource);
        }

        for(String nameShelf:getShelfNames()){
            maxResourcesInWarehouse+=getShelf(nameShelf).getMaxSize();
        }

        if(numOfResourcesToPlaceInWarehouse>maxResourcesInWarehouse)
            return false;

        if(getShelfNames().contains("extra1")||getShelfNames().contains("extra2")){
            return canPlaceResourcesInWarehouseWithExtraShelves(resourcesToPlace);
        }else {
            return canPlaceResourcesInWarehouseWithoutExtraShelves(resourcesToPlaceInWarehouse);
        }
    }

    /**
     * Checks if some resources can be placed in the warehouse without considering.
     * @param resourcesToPlace the resources that the player is trying to place.
     * @return true if the resources can be placed in the standard shelves of the warehouse according to the game rules.
     */
    private boolean canPlaceResourcesInWarehouseWithoutExtraShelves(Map<Resource,Integer> resourcesToPlace){
        int numOfTypeRes=0;
        for(Resource resource:resourcesToPlace.keySet()){
            if(resourcesToPlace.get(resource)>0)
                numOfTypeRes++;
        }
        if(numOfTypeRes>3){
            return false;
        }
        for(int i=1; i<=3;i++){
            for(Resource resource:resourcesToPlace.keySet()){
                if(resourcesToPlace.get(resource)>=1 && resourcesToPlace.get(resource)<=i) {
                    resourcesToPlace.put(resource, 0);
                    break;
                }
            }
        }
        return resourcesToPlace.equals(GameUtils.emptyResourceMap());
    }

    /**
     * Checks if some resources can be placed in the warehouse's extra shelves.
     * @param resourcesToPlace the resources that the player is trying to place.
     * @return true if the resources can be placed in the extra shelves of the warehouse according to the game rules.
     */
    private boolean canPlaceResourcesInWarehouseWithExtraShelves(Map<Resource,Integer> resourcesToPlace) {
        List<Resource> resTypeExtraShelf=new ArrayList<>();
        if (shelfNames.contains("extra1"))
            resTypeExtraShelf.add(getShelf("extra1").getResourceType());
        if (shelfNames.contains("extra2"))
            resTypeExtraShelf.add(getShelf("extra2").getResourceType());
        else
            resTypeExtraShelf.add(Resource.ANY);

        for (Resource resource : resourcesToPlace.keySet()) {
            if (resTypeExtraShelf.contains(resource)) {
                if (resourcesToPlace.get(resource) > 5) return false;
                resourcesToPlace.put(resource, Math.max(resourcesToPlace.get(resource)-2, 0));
            }
        }
        return canPlaceResourcesInWarehouseWithoutExtraShelves(resourcesToPlace);
    }

    /**
     * Adds some resources to the warehouse.
     * @param resourcesToPlace a Map<Resource, Integer>
     */
    public void placeResources(Map<Resource, Integer> resourcesToPlace) {
        if (!canPlaceResources(resourcesToPlace)) return;
        Map<Resource, Integer> allResources = GameUtils.sumResourcesMaps(getResources(), resourcesToPlace);
        // sort by biggest amount
        allResources = GameUtils.sortResourceMapByValues(allResources);
        // empty all the shelves
        for (Shelf shelf : shelves) {
            shelf.useResources(shelf.getResources());
        }
        List<String> shelfNames = new ArrayList<>(this.shelfNames);
        Collections.reverse(shelfNames);
        List<Resource> notPlacedResources = new ArrayList<>(allResources.keySet());
        // place in the shelves starting from the extras,
        // when the bottom shelf is reached, re-sort the map by biggest value
        for (String shelfName : shelfNames) {
            if (shelfName.equals("bottom")) {
                allResources = GameUtils.sortResourceMapByValues(allResources);
                notPlacedResources = new ArrayList<>(allResources.keySet());
            }
            Resource shelfType = getShelf(shelfName).getResourceType();
            if (shelfType == Resource.ANY) {
                // when the shelf doesn't have a resourceType, fill it with the resource
                // in the highest quantity
                shelfType = notPlacedResources.get(0);
                notPlacedResources.remove(0);
            }
            int resourceAmount = allResources.get(shelfType);

            if (resourceAmount < 1 && !shelfName.equals("extra1") && !shelfName.equals("extra2")){
                //GameUtils.debug("return");
                return;
            }


            int leftoverAmount = Math.max(resourceAmount - getShelf(shelfName).getMaxSize(), 0);

            Map<Resource, Integer> shelfResources = new HashMap<>();
            shelfResources.put(shelfType, resourceAmount - leftoverAmount);
            getShelf(shelfName).addResources(shelfResources);
            allResources.put(shelfType, leftoverAmount);
        }
    }
}

