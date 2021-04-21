package it.polimi.ingsw.client;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.game.GameMessage;
import it.polimi.ingsw.network.game.SelectCardMessage;
import it.polimi.ingsw.network.setup.CreateRoomMessage;
import it.polimi.ingsw.network.setup.JoinPublicRoomMessage;
import it.polimi.ingsw.view.CLI;
import it.polimi.ingsw.view.UI;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Client {
    private UI ui;
    private Socket socket;

    public void run() throws IOException {
        socket = new Socket("localhost", 31415);
        ui = new CLI();
        System.out.println("sopra");
        ServerConnection serverConnection = new ServerConnection(socket, this);
        System.out.println("entro");
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(serverConnection::waitForMessages);
        serverConnection.sendMessage(new CreateRoomMessage(false, 2, "Mac"));



    }

    public UI getUi() {
        return ui;
    }
}
