package it.polimi.ingsw.server;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.network.game.GameMessage;
import it.polimi.ingsw.network.setup.SetupMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class ClientConnection implements Runnable{

    private final Socket socket;
    private final Server server;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private final SetupMessageHandler setupMessageHandler;
    private GameMessageHandler gameMessageHandler;

    public ClientConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        try {
            inputStream  = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

        this.setupMessageHandler=new SetupMessageHandler(server, this);
    }

    @Override
    public void run() {
        while (true) {
            Message m = receiveMessage();
            switch (m.getType()) {
                case "GAME":
                    ((GameMessage) m).accept(gameMessageHandler);
                    break;
                case "SETUP":
                    ((SetupMessage) m).accept(setupMessageHandler);
                    break;
            }
        }
    }

    public Message receiveMessage() {
        Message m = null;
        try {
            m = (Message) inputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return m;
    }

    public void sendMessage(Message m) {
        try {
            outputStream.writeObject(m);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setGameMessageHandler(GameMessageHandler gameMessageHandler) {
        this.gameMessageHandler = gameMessageHandler;
    }
}
