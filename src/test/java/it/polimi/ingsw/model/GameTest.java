package it.polimi.ingsw.model;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.shared.LeaderCard;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;


public class GameTest {

    private Game structure;
    Player player1;
    Player player2;
    Player player3;
    Player player4;
    ArrayList<LeaderCard> oldLeaderCards;

    @Before
    public void setUp(){
        player1 = new Player("Bruno");
        player2 = new Player("Alby");
        player3 = new Player("Samu");
        player4 = new Player("LorenzoIlMagnifico");

        structure = new Game();




    }

    @Test
    public void PlayersTest() {
        structure.addPlayer("Bruno");
        structure.addPlayer("Alby");
        structure.addPlayer("Samu");
        structure.addPlayer("LorenzoNonMagnifico");


        assertEquals(player1.getUsername(), structure.getActivePlayers().get(0).getUsername());
        assertEquals(player2.getUsername(), structure.getActivePlayers().get(1).getUsername());
        assertEquals(player3.getUsername(), structure.getActivePlayers().get(2).getUsername());
        assertNotEquals(player4.getUsername(), structure.getActivePlayers().get(3).getUsername());

        ArrayList<LeaderCard> leaderCardsSum = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            leaderCardsSum.addAll(structure.getActivePlayers().get(i).getLeaderCards());

            assertEquals(4, structure.getActivePlayers().get(i).getLeaderCards().stream().count());
            assertEquals(4, structure.getActivePlayers().get(i).getLeaderCards().stream().count());
        }


        assertEquals(16, leaderCardsSum.stream().distinct().count());

    }



}
