package it.polimi.ingsw.client;

import it.polimi.ingsw.network.setup.JoinPublicRoomMessage;
import it.polimi.ingsw.view.CLI;
import it.polimi.ingsw.view.UI;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class client2 extends Client{
    private UI ui;

    public void run() throws IOException {
        Socket socket = new Socket("localhost", 31415);
        ui = new CLI(this);
        System.out.println("fatta ui");
        ServerConnection serverConnection = new ServerConnection(socket, this);
        System.out.println("connected");
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(serverConnection::waitForMessages);

        serverConnection.sendMessage( new JoinPublicRoomMessage(2, "iPad"));
        System.out.println("sent");
    }
}
