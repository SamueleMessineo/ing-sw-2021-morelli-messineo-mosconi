package it.polimi.ingsw.model.player;
import it.polimi.ingsw.model.shared.*;

import java.util.ArrayList;
import java.util.Map;

public class PlayerBoard {

    private Warehouse warehouse;
    private final ArrayList<PlayerCardStack> cardStacks = new ArrayList<>();
    private final Strongbox strongbox;
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
        Map<Resource, Integer> allResources = getResources();
        int points = 0;
        for (Resource resourceType : allResources.keySet()) {
            points += allResources.get(resourceType);
        }
        points /= 5;

        for (PlayerCardStack playerCardStack : cardStacks) {
            for (DevelopmentCard playerCard : playerCardStack) {
                points += playerCard.getScore();
            }
        }

        return points;
    }

    public void expandWarehouse(Resource resource){
        this.warehouse.addNewShelf("extra" + (this.warehouse.getShelfNames().size()-2),
                new Shelf(2, resource));
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
