package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.ClientMessageHandler;

import java.util.Map;

/**
 * Message with the winner and the standing.
 */
public class GameOverMessage extends ClientMessage{
    private final String winner;
    private final Map<String, Integer> standing;

    public GameOverMessage(String winner, Map<String, Integer> standing) {
        this.winner = winner;
        this.standing = standing;
    }

    public String getWinner() {
        return winner;
    }

    public Map<String, Integer> getStanding() {
        return standing;
    }

    public void accept(ClientMessageHandler handler) {
        handler.handle(this);
    }
}
