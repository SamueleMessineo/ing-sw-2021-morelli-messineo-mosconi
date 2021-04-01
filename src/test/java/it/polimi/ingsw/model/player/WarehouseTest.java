package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.shared.Resource;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class WarehouseTest {

    private final Warehouse warehouse = new Warehouse();

    @Test
    public void getShelfIndex() {
        assertEquals(0, warehouse.getShelfIndex("top"));
        assertEquals(1, warehouse.getShelfIndex("middle"));
        assertEquals(2, warehouse.getShelfIndex("bottom"));
    }

    @Test
    public void getShelf() {
        assertEquals(warehouse.getShelves().get(0), warehouse.getShelf("top"));
        assertEquals(warehouse.getShelves().get(1), warehouse.getShelf("middle"));
        assertEquals(warehouse.getShelves().get(2), warehouse.getShelf("bottom"));
    }

    @Test
    public void getShelfResources() {
        assertEquals(warehouse.getShelves().get(0).getResources(), warehouse.getShelfResources("top"));
        assertEquals(warehouse.getShelves().get(1).getResources(), warehouse.getShelfResources("middle"));
        assertEquals(warehouse.getShelves().get(2).getResources(), warehouse.getShelfResources("bottom"));
    }

    @Test
    public void canPlaceOnShelf() {
        Map<Resource, Integer> resources = new HashMap<>(){{
            put(Resource.COIN, 2);
        }};
        assertFalse(warehouse.canPlaceOnShelf("top", resources));
        assertTrue(warehouse.canPlaceOnShelf("middle", resources));
        assertTrue(warehouse.canPlaceOnShelf("bottom", resources));
        resources.put(Resource.COIN, 3);
        assertFalse(warehouse.canPlaceOnShelf("middle", resources));
    }

    @Test
    public void placeOnShelf() {
        Map<Resource, Integer> resources = new HashMap<>(){{
            put(Resource.COIN, 0);
            put(Resource.STONE, 0);
            put(Resource.SHIELD, 0);
            put(Resource.SERVANT, 0);
        }};
        assertEquals(resources, warehouse.getResources());

        Map<Resource, Integer> newResources = new HashMap<>(){{
            put(Resource.STONE, 2);
        }};
        resources.put(Resource.STONE, 2);
        warehouse.placeOnShelf("middle", newResources);
        assertEquals(resources, warehouse.getResources());
        assertEquals(resources, warehouse.getShelfResources("middle"));
    }

    @Test
    public void switchShelves() {
        Map<Resource, Integer> firstResources = new HashMap<>(){{
            put(Resource.COIN, 2);
        }};
        Map<Resource, Integer> secondResources = new HashMap<>(){{
            put(Resource.STONE, 2);
        }};
        warehouse.placeOnShelf("middle", firstResources);
        warehouse.placeOnShelf("bottom", secondResources);
        warehouse.switchShelves("middle", "bottom");
        assertEquals(new HashMap<Resource, Integer>(){{
            put(Resource.STONE, 2);
            put(Resource.COIN, 0);
            put(Resource.SHIELD, 0);
            put(Resource.SERVANT, 0);
        }}, warehouse.getShelfResources("middle"));
        assertEquals(new HashMap<Resource, Integer>(){{
            put(Resource.STONE, 0);
            put(Resource.COIN, 2);
            put(Resource.SHIELD, 0);
            put(Resource.SERVANT, 0);
        }}, warehouse.getShelfResources("bottom"));
    }

    @Test
    public void switchShelves_oneEmpty() {
        Map<Resource, Integer> firstResources = new HashMap<>(){{
            put(Resource.COIN, 2);
        }};
        warehouse.placeOnShelf("middle", firstResources);
        warehouse.switchShelves("middle", "bottom");
        assertEquals(new HashMap<Resource, Integer>(){{
            put(Resource.STONE, 0);
            put(Resource.COIN, 0);
            put(Resource.SHIELD, 0);
            put(Resource.SERVANT, 0);
        }}, warehouse.getShelfResources("middle"));
        assertEquals(new HashMap<Resource, Integer>(){{
            put(Resource.STONE, 0);
            put(Resource.COIN, 2);
            put(Resource.SHIELD, 0);
            put(Resource.SERVANT, 0);
        }}, warehouse.getShelfResources("bottom"));
    }

    @Test
    public void getResources() {
        Map<Resource, Integer> firstResources = new HashMap<>(){{
            put(Resource.COIN, 2);
        }};
        Map<Resource, Integer> secondResources = new HashMap<>(){{
            put(Resource.STONE, 2);
        }};
        warehouse.placeOnShelf("middle", firstResources);
        warehouse.placeOnShelf("bottom", secondResources);

        assertEquals(new HashMap<Resource, Integer>(){{
            put(Resource.STONE, 2);
            put(Resource.COIN, 2);
            put(Resource.SHIELD, 0);
            put(Resource.SERVANT, 0);
        }}, warehouse.getResources());
    }
}