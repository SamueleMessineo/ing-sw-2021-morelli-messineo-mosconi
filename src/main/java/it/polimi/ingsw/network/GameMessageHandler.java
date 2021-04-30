package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.Turn;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.client.*;
import it.polimi.ingsw.network.game.*;
import it.polimi.ingsw.network.setup.Room;
import it.polimi.ingsw.server.ClientConnection;

import java.util.ArrayList;

public class GameMessageHandler {

    private final GameController gameController;
    private final ClientConnection clientConnection;
    private final Room room;
    private Turn currentTurn;

    public GameMessageHandler(GameController gameController, ClientConnection clientConnection, Room room) {
        this.gameController = gameController;
        this.clientConnection = clientConnection;
        this.room = room;
        clientConnection.sendMessage(new DropLeaderCardsRequestMessage(room.getPlayerFromConnection(clientConnection).getLeaderCards()));
    }

    public void handle(SelectCardMessage message) {
        System.out.println(message.getNum());
    }

    public void handle(EndTurnMessage message) {
        System.out.println(message.getName());
    }


    public void handle(DropLeaderCardsResponseMessage message){
        gameController.dropInitialLeaderCards(message.getCard1(), message.getCard2(), room.getPlayerFromConnection(clientConnection).getUsername());
        startPlayingIfReady();
    }

    private void startPlayingIfReady() {
        for (Player p : room.getGame().getPlayers()) {
            if (p.getLeaderCards().size() != 2) return;
        }

        room.sendAll(new GameStateMessage(room.getGame()));

        ClientConnection currentPlayer = room.getConnections().get(room.getGame().getPlayers().indexOf(
                room.getGame().getCurrentPlayer()));

        currentTurn = new Turn(room.getPlayerFromConnection(currentPlayer).getUsername(),new ArrayList<String>(){{
            add("DROP_LEADER");
            add("GET_MARBLES");
        }});

        SelectMoveRequestMessage selectMoveRequestMessage = new SelectMoveRequestMessage(currentTurn.getMoves());
        currentPlayer.sendMessage(selectMoveRequestMessage);
    }

    public void handle(SelectMoveResponseMessage message){
        System.out.println(message.getMove());
        System.out.println(currentTurn.getMoves());
        System.out.println(room.getPlayerFromConnection(clientConnection).getUsername());
        System.out.println(currentTurn.getCurrentPlayer());
        if(currentTurn.isValidMove(message.getMove(), room.getPlayerFromConnection(clientConnection).getUsername())){
            System.out.println("valid");
            System.out.println(room.getGame().getMarket().getMarbleStructure());
            clientConnection.sendMessage(new SelectMarblesRequestMessage(room.getGame().getMarket().getMarbleStructure()));
        } else {
            System.out.println("invalid");
            clientConnection.sendMessage(new ErrorMessage("Invalid Move"));
        }

    }

    public void handle(SelectMarblesResponseMessage message){
        System.out.println("select marbles response message");
    }

}
