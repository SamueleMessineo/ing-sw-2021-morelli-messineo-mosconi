package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.shared.Resource;

import java.util.ArrayList;
import java.util.Map;

public class ExtraSpaceDecorator extends Warehouse {
    private final Warehouse warehouse;

    public ExtraSpaceDecorator(Warehouse warehouse, Resource type) {
        this.warehouse = warehouse;
        warehouse.addNewShelf("extra" + (warehouse.getShelfNames().size()-2),
                                new Shelf(2, type));
    }

    @Override
    public ArrayList<Shelf> getShelves() {
        return warehouse.getShelves();
    }

    @Override
    public ArrayList<String> getShelfNames() {
        return warehouse.getShelfNames();
    }

    @Override
    protected int getShelfIndex(String shelf) {
        return warehouse.getShelfIndex(shelf);
    }

    @Override
    public Shelf getShelf(String shelf) {
        return warehouse.getShelf(shelf);
    }

    @Override
    public void addNewShelf(String newShelfName, Shelf shelf) {
        warehouse.addNewShelf(newShelfName, shelf);
    }

    @Override
    public Map<Resource, Integer> getShelfResources(String shelf) {
        return warehouse.getShelfResources(shelf);
    }

    @Override
    public boolean canPlaceOnShelf(String shelf, Map<Resource, Integer> resources) {
        return warehouse.canPlaceOnShelf(shelf, resources);
    }

    @Override
    public void placeOnShelf(String shelf, Map<Resource, Integer> resources) {
        warehouse.placeOnShelf(shelf, resources);
    }

    @Override
    public void switchShelves(String firstShelfName, String secondShelfName) {
        warehouse.switchShelves(firstShelfName, secondShelfName);
    }

    @Override
    public Map<Resource, Integer> getResources() {
        return warehouse.getResources();
    }
}
