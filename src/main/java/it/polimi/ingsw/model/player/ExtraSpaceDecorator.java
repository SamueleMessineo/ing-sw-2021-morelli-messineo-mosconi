package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.shared.Resource;

//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;

public class ExtraSpaceDecorator extends Warehouse {
    private final Warehouse warehouse;
//    private final Resource resourceType;
//    private int resourceNumber;

    public ExtraSpaceDecorator(Warehouse warehouse, Resource type) {
        this.warehouse = warehouse;
//        resourceType = type;
//        resourceNumber = 0;
        warehouse.getShelves().add(new Shelf(2, true));
        warehouse.getShelfNames().add("extra" + (warehouse.getShelfNames().size() - 2));
    }

//    @Override
//    public Shelf getShelf(String shelf) {
//        if (shelf.toLowerCase().trim().equals("extra")) {
//            Shelf extraShelf = new Shelf(2, true);
//            extraShelf.addResources(resourceType, resourceNumber);
//            return extraShelf;
//        }
//        return warehouse.getShelf(shelf);
//    }
//
//    @Override
//    public Map<Resource, Integer> getResources() {
//        Map<Resource, Integer> warehouseResources = new HashMap<>(warehouse.getResources());
//        warehouseResources.put(resourceType, warehouseResources.get(resourceType) + resourceNumber);
//        return warehouseResources;
//    }
//
//    @Override
//    public boolean canPlaceOnShelf(String shelf, Resource rType, int rNumber) {
//        if (shelf.toLowerCase().trim().equals("extra")) {
//            if (rType.equals(resourceType) && resourceNumber + rNumber <= 2) return true;
//        }
//        return warehouse.canPlaceOnShelf(shelf, rType, rNumber);
//    }
//
//    @Override
//    public void placeOnShelf(String shelf, Resource rType, int rNumber) {
//        if (shelf.toLowerCase().trim().equals("extra")) {
//            if (rType.equals(resourceType) && resourceNumber + rNumber <= 2) resourceNumber += rNumber;
//        }
//        warehouse.placeOnShelf(shelf, rType, rNumber);
//    }
//
//    @Override
//    public int useResources(Map<Resource, Integer> resources) {
//        return -1;
//    }
//
//    @Override
//    public Map<Resource, Integer> getShelfResources(String shelf) {
//        if (shelf.toLowerCase().trim().equals("extra")) {
//            Map<Resource, Integer> extraShelfResources = new HashMap<>();
//            extraShelfResources.put(Resource.SHIELD, 0);
//            extraShelfResources.put(Resource.COIN, 0);
//            extraShelfResources.put(Resource.SERVANT, 0);
//            extraShelfResources.put(Resource.STONE, 0);
//            extraShelfResources.put(resourceType, resourceNumber);
//            return extraShelfResources;
//        }
//        return warehouse.getShelfResources(shelf);
//    }
//
//    @Override
//    public void switchShelves(String firstShelfName, String secondShelfName) {
//
//    }
}
