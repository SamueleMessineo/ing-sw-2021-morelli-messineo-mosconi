package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.shared.Resource;

import java.util.Map;

public class Strongbox {

    private Map<Resource, Integer> resources;

    public Strongbox(Map<Resource,Integer> resources){
        this.resources=resources;
    }

    public Map<Resource,Integer> getResources() { return resources; }

    public void useResources(Map<Resource,Integer> res){
        this.resources.forEach((resource,integer)->
                this.resources.put(resource,integer - res.get(resource)));
    }
}