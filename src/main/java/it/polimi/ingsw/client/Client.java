package it.polimi.ingsw.client;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.game.EndTurnMessage;
import it.polimi.ingsw.network.messages.game.SelectCardMessage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    public void run() throws IOException {
        Socket socket = new Socket("localhost", 31415);
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        Message m = new SelectCardMessage();
        outputStream.reset();
        outputStream.writeObject(m);
        outputStream.flush();
        Message m2 = new EndTurnMessage();
        outputStream.reset();
        outputStream.writeObject(m2);
        outputStream.flush();
    }
}
