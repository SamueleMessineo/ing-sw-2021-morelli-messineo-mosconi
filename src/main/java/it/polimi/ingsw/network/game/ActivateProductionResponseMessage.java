package it.polimi.ingsw.network.game;

import it.polimi.ingsw.client.LocalMessageHandler;
import it.polimi.ingsw.model.shared.ProductionPower;
import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.network.GameMessageHandler;

import java.util.List;

public class ActivateProductionResponseMessage extends GameMessage {
    private final List<Integer> selectedStacks;
    private final ProductionPower basicProduction;
    private final List<Integer> extraProductionPowers;
    private final List<Resource> extraOutput;

    public ActivateProductionResponseMessage(List<Integer> selectedStacks, ProductionPower basicProduction, List<Integer> extraProductionPowers, List<Resource> extraOutput) {
        this.selectedStacks = selectedStacks;
        this.basicProduction = basicProduction;
        this.extraProductionPowers = extraProductionPowers;
        this.extraOutput = extraOutput;
    }

    public List<Integer> getSelectedStacks() {
        return selectedStacks;
    }

    public ProductionPower getBasicProduction() {
        return basicProduction;
    }

    public List<Integer> getExtraProductionPowers() {
        return extraProductionPowers;
    }

    public List<Resource> getExtraOutput() { return extraOutput; }

    public void accept(GameMessageHandler handler) {
        handler.handle(this);
    }
    public void accept(LocalMessageHandler handler){handler.handle(this);}
}
