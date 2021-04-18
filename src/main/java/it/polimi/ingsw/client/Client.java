package it.polimi.ingsw.client;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.game.GameMessage;
import it.polimi.ingsw.network.game.SelectCardMessage;
import it.polimi.ingsw.network.setup.CreateRoomMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class Client {
    public void run() throws IOException {
        Socket socket = new Socket("localhost", 31415);
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        CreateRoomMessage createRoomMessage=new CreateRoomMessage(false, 4, "prova");
        outputStream.writeObject(createRoomMessage);
    }
}
