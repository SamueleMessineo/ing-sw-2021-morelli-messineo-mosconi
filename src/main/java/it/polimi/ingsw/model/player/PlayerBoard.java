package it.polimi.ingsw.model.player;
import it.polimi.ingsw.model.shared.*;

import java.util.ArrayList;

public class PlayerBoard {

    private Warehouse warehouse;
    private PlayerCardStack[] cardStacks = new PlayerCardStack[3];
    private Strongbox strongbox;
    private ArrayList<ProductionPower> extraProductionPowers = new ArrayList<>();

    public PlayerBoard(Warehouse warehouse, PlayerCardStack[] cardStacks, Strongbox strongbox, ArrayList<ProductionPower> extraProductionPowers) {
        this.warehouse = warehouse;
        this.cardStacks = cardStacks;
        this.strongbox = strongbox;

    }


    public Warehouse getWarehouse() {
        return warehouse;
    }


    public PlayerCardStack[] getCardStacks() {
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
        System.out.println("Mettodo da implementare successivamente");
        return -1;
    }

    public void expandWarehouse(Resource resource){
        this.warehouse = new ExtraSpaceDecorator(warehouse, resource);
    }

    public void activateProduction(){
        System.out.println("metodo da implementare successivamente");
    }
}
