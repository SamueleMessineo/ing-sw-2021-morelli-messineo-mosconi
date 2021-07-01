package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.shared.PopesFavorTileState;
import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.model.shared.SoloActionType;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import static org.junit.Assert.*;

public class SoloGameControllerTest {
    private Game game1;
    private ClassicGameController gameController;

    @Before
    public void setUp() throws Exception {
        String path = "src/main/resources/testGames/0000.ser";
        try {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            game1 = (Game) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
        }
        gameController = new SoloGameController(game1);
    }

    @Test
    public void isGameOver() {
        assertFalse(gameController.isGameOver());
    }

    @Test
    public void computeStanding() {
    }

    @Test
    public void computeWinner() {
    }

    @Test
    public void computeNextPlayer() {
        SoloActionType nextSoloAction = game1.getSoloActionTypes().peek();
        int initialLorenzoPosition = game1.getLorenzoIlMagnifico().getFaithTrack().getPosition();
        gameController.computeNextPlayer();
        switch (nextSoloAction) {
            case PLUS_ONE:
                assertEquals(initialLorenzoPosition+1, game1.getLorenzoIlMagnifico().getFaithTrack().getPosition());
                break;
            case PLUS_TWO:
                assertEquals(initialLorenzoPosition+2, game1.getLorenzoIlMagnifico().getFaithTrack().getPosition());
                break;
            default:
                break;
        }
    }

    @Test
    public void activatePopeReport() {
        assertEquals(game1.getPlayerByUsername("p1").getFaithTrack().getPopesFavorTiles().get(0).getState(), PopesFavorTileState.INACTIVE);
        assertEquals(game1.getLorenzoIlMagnifico().getFaithTrack().getPopesFavorTiles().get(0).getState(), PopesFavorTileState.INACTIVE);
        gameController.movePlayer("p1", 4);
        assertEquals(game1.getPlayerByUsername("p1").getFaithTrack().getPopesFavorTiles().get(0).getState(), PopesFavorTileState.ACTIVE);
        assertEquals(game1.getLorenzoIlMagnifico().getFaithTrack().getPopesFavorTiles().get(0).getState(), PopesFavorTileState.DISCARDED);
    }

    @Test
    public void dropPlayerResources() {
        int initialLorenzoPosition = game1.getLorenzoIlMagnifico().getFaithTrack().getPosition();

        Map<Resource, Integer> obtainedResources = new HashMap<>();
        obtainedResources.put(Resource.COIN, 2);
        obtainedResources.put(Resource.STONE, 1);
        Map<Resource, Integer> resourcesToDrop = new HashMap<>();
        resourcesToDrop.put(Resource.COIN, 2);

        gameController.dropPlayerResources(obtainedResources, resourcesToDrop, "p1");
        assertEquals(initialLorenzoPosition+2, game1.getLorenzoIlMagnifico().getFaithTrack().getPosition());
    }
}