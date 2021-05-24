package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.shared.CardType;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.model.shared.ProductionPower;
import it.polimi.ingsw.model.shared.Resource;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class PlayerCardStackTest {

    private Player player;
    @Before
    public void setUp() {
        player=new Player("prova");
    }

    @Test
    public void canPlaceCard() {
        Map<Resource, Integer> emptyMap1 = new HashMap<>();
        ProductionPower productionPower=new ProductionPower(emptyMap1, emptyMap1);
        DevelopmentCard developmentCard1=new DevelopmentCard(1, CardType.GREEN, emptyMap1, productionPower,0);
        DevelopmentCard developmentCard2=new DevelopmentCard(2, CardType.GREEN, emptyMap1, productionPower,0);

        assertFalse(player.getPlayerBoard().getCardStacks().get(0).canPlaceCard(developmentCard2));
        assertTrue(player.getPlayerBoard().getCardStacks().get(0).canPlaceCard(developmentCard1));
        player.getPlayerBoard().getCardStacks().get(0).push(developmentCard1);
        assertTrue(player.getPlayerBoard().getCardStacks().get(0).canPlaceCard(developmentCard2));
        assertFalse(player.getPlayerBoard().getCardStacks().get(0).canPlaceCard(developmentCard1));
    }
}