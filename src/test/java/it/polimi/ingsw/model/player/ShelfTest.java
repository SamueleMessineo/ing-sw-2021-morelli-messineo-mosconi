package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.shared.Resource;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ShelfTest {
    @Before
    public void setUp() {
    }

    @Test
    public void getResourceType() {
        Shelf shelf = new Shelf(3);
        assertNull(shelf.getResourceType());
        shelf.addResources(new HashMap<>(){{
            put(Resource.COIN, 2);
        }});
        assertEquals(Resource.COIN, shelf.getResourceType());
    }

    @Test
    public void getResourceNumber() {
        Shelf shelf = new Shelf(3);
        assertEquals(0, shelf.getResourceNumber());
        shelf.addResources(new HashMap<>(){{
            put(Resource.COIN, 2);
        }});
        assertEquals(2, shelf.getResourceNumber());
    }

    @Test
    public void getResources() {
        Shelf shelf = new Shelf(3);
        Map<Resource, Integer> resources = new HashMap<>();
        resources.put(Resource.SHIELD, 0);
        resources.put(Resource.COIN, 0);
        resources.put(Resource.SERVANT, 0);
        resources.put(Resource.STONE, 0);
        assertEquals(resources, shelf.getResources());
    }

    @Test
    public void getMaxSize() {
        Shelf shelf = new Shelf(3);
        assertEquals(3, shelf.getMaxSize());
    }

    @Test
    public void addResources() {
        Shelf shelf = new Shelf(3);
        Map<Resource, Integer> resources = new HashMap<>();
        resources.put(Resource.SHIELD, 0);
        resources.put(Resource.COIN, 2);
        resources.put(Resource.SERVANT, 0);
        resources.put(Resource.STONE, 0);

        Map<Resource, Integer> newResources = new HashMap<>();
        newResources.put(Resource.COIN, 2);

        shelf.addResources(newResources);
        assertEquals(resources, shelf.getResources());
    }

    @Test
    public void canPlace() {
        Shelf shelf = new Shelf(3);

        Map<Resource, Integer> newResources = new HashMap<>();
        newResources.put(Resource.STONE, 2);

        shelf.addResources(newResources);

        newResources.put(Resource.STONE, 2);
        assertFalse(shelf.canPlace(newResources));

        newResources.put(Resource.STONE, 1);
        assertTrue(shelf.canPlace(newResources));
    }

    @Test
    public void useResources() {
        Shelf shelf = new Shelf(3);

        Map<Resource, Integer> newResources = new HashMap<>();
        newResources.put(Resource.STONE, 2);

        shelf.addResources(newResources);
        newResources.put(Resource.STONE, 1);
        shelf.useResources(newResources);

        Map<Resource, Integer> expected = new HashMap<>();
        expected.put(Resource.SHIELD, 0);
        expected.put(Resource.COIN, 0);
        expected.put(Resource.SERVANT, 0);
        expected.put(Resource.STONE, 1);

        assertEquals(expected, shelf.getResources());

        shelf = new Shelf(3);

        newResources = new HashMap<>();
        newResources.put(Resource.STONE, 3);

        shelf.addResources(newResources);
        newResources.put(Resource.STONE, 1);
        shelf.useResources(newResources);

        expected = new HashMap<>();
        expected.put(Resource.SHIELD, 0);
        expected.put(Resource.COIN, 0);
        expected.put(Resource.SERVANT, 0);
        expected.put(Resource.STONE, 2);

        assertEquals(expected, shelf.getResources());
    }
}