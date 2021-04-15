package it.polimi.ingsw.client;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.game.EndTurnMessage;
import it.polimi.ingsw.network.game.GameMessage;
import it.polimi.ingsw.network.game.SelectCardMessage;
import it.polimi.ingsw.network.setup.CreateGameMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class Client {
    public void run() throws IOException {
        Socket socket = new Socket("localhost", 31415);
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        GameMessage m = new SelectCardMessage();
        outputStream.reset();
        outputStream.writeObject(m);
        outputStream.flush();
        Message m2 = new CreateGameMessage();
        outputStream.reset();
        outputStream.writeObject(m2);
        outputStream.flush();
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
