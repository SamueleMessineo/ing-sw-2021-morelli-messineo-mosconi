package it.polimi.ingsw.client;

import it.polimi.ingsw.network.setup.JoinPublicRoomMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class client2 {
    public void run() throws IOException {
        Socket socket = new Socket("localhost", 31415);
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        JoinPublicRoomMessage joinPublicRoomMessage = new JoinPublicRoomMessage(2, "iPad");
        outputStream.writeObject(joinPublicRoomMessage);
    }
}
