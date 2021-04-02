package it.polimi.ingsw.model.player;
import it.polimi.ingsw.model.shared.*;

import java.util.ArrayList;
import java.util.Map;

public class PlayerBoard {

    private Warehouse warehouse;
    private ArrayList<PlayerCardStack> cardStacks = new ArrayList<>();
    private Strongbox strongbox;
    private ArrayList<ProductionPower> extraProductionPowers = new ArrayList<>();

    public PlayerBoard() {
        warehouse = new Warehouse();
        for (int i = 0; i < 3; i++) {
            cardStacks.add(new PlayerCardStack());
        }
        strongbox = new Strongbox();
    }


    public Warehouse getWarehouse() {
        return warehouse;
    }


    public ArrayList<PlayerCardStack> getCardStacks() {
        return cardStacks;
    }



    public Strongbox getStrongbox() {
        return strongbox;
    }


    public ArrayList<ProductionPower> getExtraProductionPowers() {
        return extraProductionPowers;
    }

    public void setExtraProductionPowers(ArrayList<ProductionPower> extraProductionPowers) {
        this.extraProductionPowers = extraProductionPowers;
    }

    public int getPoints() {
        // TODO
        return -1;
    }

    public void expandWarehouse(Resource resource){
        this.warehouse = new ExtraSpaceDecorator(warehouse, resource);
    }

    public void activateProduction(){
        System.out.println("metodo da implementare successivamente");
    }

    public Map<Resource, Integer> getResources() {
        Map<Resource, Integer> strongboxResources = strongbox.getResources();
        Map<Resource, Integer> warehouseResources = warehouse.getResources();

        Map<Resource, Integer> allResources = strongboxResources;
        allResources.forEach(((resource, integer) -> {
            allResources.put(resource, integer + warehouseResources.get(resource));
        }));

        return allResources;
    }
}
