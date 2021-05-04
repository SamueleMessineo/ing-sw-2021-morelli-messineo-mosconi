package it.polimi.ingsw.server;

import it.polimi.ingsw.network.*;
import it.polimi.ingsw.network.game.GameMessage;
import it.polimi.ingsw.network.setup.SetupMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientConnection implements Runnable{

    private final Socket socket;
    private final Server server;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private final SetupMessageHandler setupMessageHandler;
    private GameMessageHandler gameMessageHandler;
    private Logger logger;

    public ClientConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream  = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

        logger = Logger.getLogger(ClientConnection.class.getName());

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
            logger.info(m.toString());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return m;
    }

    public void sendMessage(Message m) {
        try {
            outputStream.reset();
            outputStream.writeObject(m);
            outputStream.flush();
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
