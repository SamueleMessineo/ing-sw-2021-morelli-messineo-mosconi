package it.polimi.ingsw.network.setup;

import it.polimi.ingsw.network.SetupMessageHandler;

/**
 * Message with the parameters of the public game to join.
 */
public class JoinPublicRoomMessage extends SetupMessage{

    private final int numberOfPlayers;
    private final String username;

    public JoinPublicRoomMessage(int numberOfPlayers, String username) {
        this.numberOfPlayers = numberOfPlayers;
        this.username=username;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public String getUsername() {
        return username;
    }

    public void accept(SetupMessageHandler handler) {
        handler.handle(this);
    }

}
