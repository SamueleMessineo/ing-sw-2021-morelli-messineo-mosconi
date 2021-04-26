package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.shared.Resource;

import java.util.Map;

/**
 * The storage interface is used for all classes that hold any number of resources,
 * such as the Shelf and the Strongbox.
 */
public interface Storage {

    Map<Resource, Integer> getResources();

    void addResources(Map<Resource, Integer> resources);

    void useResources(Map<Resource, Integer> resources);
}
