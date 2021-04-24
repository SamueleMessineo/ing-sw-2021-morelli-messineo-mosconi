package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.client.StringMessage;
import it.polimi.ingsw.network.setup.Room;

public class GameController {
    private final Room room;
    private final Game game;

    public GameController(Room room) {
        this.room = room;
        this.game = room.getGame();
    }

    public void start(){
        room.sendAll(new StringMessage("Game started"));
        for (Player player:
             game.getPlayers()) {
            System.out.println(player.getUsername() + ":");
            System.out.println(player.getLeaderCards());

        }
    }
}
