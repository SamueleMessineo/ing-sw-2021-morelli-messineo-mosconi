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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SoloGameControllerTest {
    private Game game1;
    private Game game2;
    private Game game3;
    private ClassicGameController game1Controller;
    ClassicGameController game2Controller;

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
        path = "src/main/resources/testGames/007.ser";
        try {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            game2 = (Game) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
        }
        game1Controller = new SoloGameController(game1);
        game2Controller = new SoloGameController(game2);
    }

    @Test
    public void isGameOver() {
        assertFalse(game1Controller.isGameOver());
        SoloActionType soloActionTypeGreen = SoloActionType.GREEN;

        for (int i = 0; i < 8; i++) {
            game1.removeCardsByLorenzo(soloActionTypeGreen);
        }
        assertTrue(game1Controller.isGameOver());

       game2Controller.movePlayer("q", 24);
       assertTrue(game2Controller.isGameOver());

    }

    @Test
    public void computeStanding() {
        game2Controller.movePlayer("q", 24);
        assertTrue(game2Controller.isGameOver());
        assertTrue(game2Controller.computeStanding().get(game2.getCurrentPlayer().getUsername()) > game2Controller.computeStanding().get(game2.getLorenzoIlMagnifico().getUsername()));
    }

    @Test
    public void computeWinner() {
        SoloActionType soloActionTypeGreen = SoloActionType.GREEN;

        for (int i = 0; i < 8; i++) {
            game1.removeCardsByLorenzo(soloActionTypeGreen);
        }
        assertTrue(game1Controller.isGameOver());
        assertEquals(game1Controller.computeWinner().trim().toLowerCase(), "lorenzoilmagnifico");

        game2Controller.movePlayer("q", 24);
        assertTrue(game2Controller.isGameOver());
        assertEquals(game2Controller.computeWinner().toLowerCase().trim(), "q");
    }

    @Test
    public void computeNextPlayer() {
        SoloActionType nextSoloAction = game1.getSoloActionTypes().peek();
        int initialLorenzoPosition = game1.getLorenzoIlMagnifico().getFaithTrack().getPosition();
        game1Controller.computeNextPlayer();
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
        game1Controller.movePlayer("p1", 4);
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

        game1Controller.dropPlayerResources(obtainedResources, resourcesToDrop, "p1");
        assertEquals(initialLorenzoPosition+2, game1.getLorenzoIlMagnifico().getFaithTrack().getPosition());
    }
}