package it.polimi.ingsw.server;

import it.polimi.ingsw.network.*;
import it.polimi.ingsw.network.game.GameMessage;
import it.polimi.ingsw.network.pingpong.PingMessage;
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
    private final Logger logger;
    private boolean receivedPong = false;
    private boolean connected = true;

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
                case "CONNECTION":
                    receivedPong = true;
                    break;
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
            //logger.info(m.toString());
        } catch (IOException | ClassNotFoundException e) {
            receivedPong = false;
            //e.printStackTrace();
        }
        return m;
    }

    public void sendMessage(Message m) {
        try {
            outputStream.reset();
            outputStream.writeObject(m);
            outputStream.flush();
            //logger.warning(m.toString());
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

    public void checkConnection() {
        while (isConnected()) {
            sendMessage(new PingMessage());
            long start = System.currentTimeMillis();
            long end = start + 10*1000; // 5 seconds * 1000 ms/sec
            while (System.currentTimeMillis() < end);
            if (receivedPong) {
                receivedPong = false;
            } else {
                System.out.println("client disconnected");
                gameMessageHandler.deactivateConnection(this);
                break;
            }
        }
    }

    public GameMessageHandler getGameMessageHandler() {
        return gameMessageHandler;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }


}
