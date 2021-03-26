package it.polimi.ingsw.model.shared;

import java.util.ArrayList;
import java.util.List;

/**
 * The productionPower class represents the production power of the Development Card
 */

public class ProductionPower {
    private final List<Resource> input;
    private final List<Resource> output;

    public ProductionPower(List<Resource> input, List<Resource> output){
        this.input=input;
        this.output=output;
    }

    /**
     * Fetch the resources to pay to activate the production power
     * @return A list of the Resources
     */
    public List<Resource> getInput(){
        return input;
    }

    /**
     * Fetch the resources received from the production power
     * @return A list of the Resources
     */
    public List<Resource> getOutput(){
        return output;
    }
}
