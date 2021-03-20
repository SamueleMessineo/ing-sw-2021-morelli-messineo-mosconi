package it.polimi.ingsw.model.shared;

import java.util.ArrayList;
import java.util.List;

public class ProductionPower {
    private final List<Resource> input;
    private final List<Resource> output;

    public ProductionPower(List<Resource> input, List<Resource> output){
        this.input=input;
        this.output=output;
    }

    public List<Resource> getInput(){
        return input;
    }

    public List<Resource> getOutput(){
        return output;
    }
}
