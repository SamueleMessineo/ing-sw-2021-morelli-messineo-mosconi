package it.polimi.ingsw.model.player;
import it.polimi.ingsw.model.shared.*;

import java.io.Serializable;
import java.util.ArrayList;
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
     * @param extraProductionPowers The new list of extra production powers.
     */
    public void setExtraProductionPowers(ArrayList<ProductionPower> extraProductionPowers) {
        this.extraProductionPowers = extraProductionPowers;
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
    public void activateProduction(){
        System.out.println("metodo da implementare successivamente");
    }

    /**
     * Returns a map of all the resources contained in the Strongbox and in the Warehouse.
     * @return A map with all the resources.
     */
    public Map<Resource, Integer> getResources() {
        Map<Resource, Integer> strongboxResources = strongbox.getResources();
        Map<Resource, Integer> warehouseResources = warehouse.getResources();

        Map<Resource, Integer> allResources = strongboxResources;
        allResources.forEach(((resource, integer) -> {
            allResources.put(resource, integer + warehouseResources.get(resource));
        }));

        return allResources;
    }

    @Override
    public String toString() {
        return "Top shelf: " + warehouse.getShelf("top").toString() + "\nMiddle shelf: "
                + warehouse.getShelf("middle").toString() + "\nBottom shelf: "
                + warehouse.getShelf("bottom").toString() + "\nStrongbox: " + strongbox.toString();
    }
}
