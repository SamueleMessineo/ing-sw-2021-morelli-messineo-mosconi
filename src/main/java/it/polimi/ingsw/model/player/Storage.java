package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.shared.Resource;

import java.util.Map;

/**
 * The storage interface is used for all classes that hold any number of resources,
 * such as the Shelf and the Strongbox.
 */
public interface Storage {

    /**
     * Gets the reosurces of the storage.
     * @return a Map<Resource, Integer>
     */
    Map<Resource, Integer> getResources();

    /**
     * Adds some resources to the storage,
     * @param resources a Map<Resource, Integer>.
     */
    void addResources(Map<Resource, Integer> resources);

    /**
     * Removes some resources from the storage,
     * @param resources a Map<Resource, Integer>.
     */
    void useResources(Map<Resource, Integer> resources);
}
