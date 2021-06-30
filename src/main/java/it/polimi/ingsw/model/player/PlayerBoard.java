package it.polimi.ingsw.model.player;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.shared.*;
import it.polimi.ingsw.utils.GameUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The PlayerBoard contains the warehouse, the strongbox, and all the cards that the
 * player has bought during the game.
 */
public class PlayerBoard implements Serializable {

    private Warehouse warehouse;
    private final ArrayList<PlayerCardStack> cardStacks = new ArrayList<>();
    private final Strongbox strongbox;
    private ArrayList<ProductionPower> extraProductionPowers = new ArrayList<>();

    /**
     * PLayerBoard class constructor.
     */
    public PlayerBoard() {
        warehouse = new Warehouse();
        for (int i = 0; i < 3; i++) {
            cardStacks.add(new PlayerCardStack());
        }
        strongbox = new Strongbox();
    }

    /**
     * Returns the player's warehouse.
     * @return The player's Warehouse object.
     */
    public Warehouse getWarehouse() {
        return warehouse;
    }

    /**
     * Returns the list of stacks of Development cards bought by the player.
     * @return The list of stacks of Development cards.
     */
    public ArrayList<PlayerCardStack> getCardStacks() {
        return cardStacks;
    }

    /**
     * Returns the player's strongbox.
     * @return The Strongbox object.
     */
    public Strongbox getStrongbox() {
        return strongbox;
    }

    /**
     * Returns the list of extra production powers.
     * @return The list of extra production powers.
     */
    public ArrayList<ProductionPower> getExtraProductionPowers() {
        return extraProductionPowers;
    }

    /**
     * Sets the list of extra production powers.
     * @param inputResource the input rescource the new extra production power.
     */
    public void setExtraProductionPowers(Resource inputResource) {
        Map<Resource, Integer> inputResources = new HashMap<>();
        inputResources.put(inputResource, 1);
        Map<Resource, Integer> outputResources = new HashMap<>();
        outputResources.put(Resource.ANY, 1);
        outputResources.put(Resource.FAITH, 1);
        ProductionPower productionPower = new ProductionPower(inputResources, outputResources);
        extraProductionPowers.add(productionPower);
    }

    /**
     * Calculates the Vatican Points based on the contents of the Warehouse and Strongbox,
     * and on all the Player's development cards.
     * @return The total VPs of warehouse, strongbox and cards.
     */
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

    /**
     * Adds a new expanded slot to the warehouse with the given resource.
     * @param resource The resource of the new slot.
     */
    public void expandWarehouse(Resource resource){
        this.warehouse.addNewShelf("extra" + (this.warehouse.getShelfNames().size()-2),
                new Shelf(2, resource));
    }

    /**
     * Activates the selected production powers.
     */
    public void activateProduction(List<Integer> selectedStacks){
        for (Integer i:
             selectedStacks) {
            ProductionPower productionPower = getCardStacks().get(i).peek().getProductionPower();
            activateProductionPower(productionPower);
        }
    }

    /**
     * Removes productionPowers inputs and adds ProductionPowerOutputs.
     * @param productionPower a ProductionPower to activate.
     */
    public void activateProductionPower(ProductionPower productionPower){
        payResourceCost(productionPower.getInput());
        Map<Resource,Integer> output = GameUtils.emptyResourceMap();
        output.putAll(productionPower.getOutput());
        strongbox.addResources(output);
    }

    /**
     * Chcks if a development card can be placed.
     * @param card a Development card to place.
     * @return true if there is a stack where the development card can be place.
     */
    public boolean canPlaceDevelopmentCard(DevelopmentCard card) {
        for (PlayerCardStack playerCardStack: cardStacks){
            if(playerCardStack.canPlaceCard(card))
                return true;
        }
        return false;
    }

    /**
     * Checks if there are enough resources to pay a cost.
     * @param cost a Map<Resource, Integer>.
     * @return true if for each value of  the  map the player has more or equal resources of that type.
     */
    public boolean canPayResources(Map<Resource, Integer>cost) {
        Map<Resource, Integer> allResources = new HashMap<>(getResources());
        for (Resource resource : cost.keySet()) {
            if (allResources.get(resource) == null || allResources.get(resource) < cost.get(resource)) return false;
        }
        return true;
    }

    /**
     * Removes the resources of cost from user resources prioritizing the warehouse.
     * @param cost a Map<Resource, Integer>.
     */
    public void payResourceCost(Map<Resource, Integer>cost){
        GameUtils.debug("paying: " + cost);
        if (!canPayResources(cost)) return;
        GameUtils.debug("can pay");
        for (Resource resource : cost.keySet()) {
            boolean placed = false;
            // try to remove from shelves and extra slots
            for (Shelf shelf : warehouse.getShelves()) {
                if (shelf.getResourceType() != null && shelf.getResourceType().equals(resource)) {
                    int n = shelf.getResourceNumber() - cost.get(resource);
                    if (n >= 0) {
                        Map<Resource, Integer> resourceCost = new HashMap<>();
                        resourceCost.put(resource, cost.get(resource));
                        shelf.useResources(resourceCost);
                        placed = true;
                        break;
                    } else {
                        Map<Resource, Integer> fullShelfResources = new HashMap<>();
                        fullShelfResources.put(shelf.getResourceType(), shelf.getResourceNumber());
                        shelf.useResources(fullShelfResources);
                        cost.put(resource, -n);
                    }
                }
            }
            // remove from strongbox
            if (!placed) {
                Map<Resource, Integer> resourceCost = GameUtils.emptyResourceMap();
                resourceCost.put(resource, cost.get(resource));
                strongbox.useResources(resourceCost);
            }
        }
    }

    /**
     * Returns a map of all the resources contained in the Strongbox and in the Warehouse.
     * @return A map with all the resources.
     */
    public Map<Resource, Integer> getResources() {
        Map<Resource, Integer> strongboxResources = strongbox.getResources();
        Map<Resource, Integer> warehouseResources = warehouse.getResources();
        Map<Resource, Integer> allResources = GameUtils.emptyResourceMap();

        allResources = GameUtils.sumResourcesMaps(allResources, warehouseResources);
        allResources = GameUtils.sumResourcesMaps(allResources, strongboxResources);


        return allResources;

    }

    @Override
    public String toString() {
        return "Top shelf: " + warehouse.getShelf("top").toString() + "\nMiddle shelf: "
                + warehouse.getShelf("middle").toString() + "\nBottom shelf: "
                + warehouse.getShelf("bottom").toString() + "\n 🧰 Strongbox: " + strongbox.toString();
    }
}
