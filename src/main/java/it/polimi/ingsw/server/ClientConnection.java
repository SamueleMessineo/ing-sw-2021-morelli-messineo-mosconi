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

/**
 * This class has the socket to connect with a client, it sends messages to the client and receives new messages.
 */
public class ClientConnection implements Runnable{

    private final Socket socket;
    private final Server server;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private final SetupMessageHandler setupMessageHandler;
    private GameMessageHandler gameMessageHandler;
    private boolean receivedPong = false;
    private boolean connected = true;

    /**
     * ClientConnection constructor.
     */
    public ClientConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream  = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

        this.setupMessageHandler=new SetupMessageHandler(server, this);
    }

    /**
     * Receives message and depending on  the type forwards it to the right handler.
     */
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

    /**
     * Reads the message in the input stream, if there aren't any pong messages it sets receivedPong as false.
     * @return the message in the input stream.
     */
    public Message receiveMessage() {
        Message m = null;
        try {
            m = (Message) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            receivedPong = false;
        }
        return m;
    }

    /**
     * Puts m in the output socket.
     * @param m the new message to send.
     */
    public void sendMessage(Message m) {
        try {
            outputStream.reset();
            outputStream.writeObject(m);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("pipe broke so client disconnected, wait for ping to fail");
        }
    }

    /**
     * Sets the gameMessageHandler.
     * @param gameMessageHandler a gameMessageHandler
     */
    public void setGameMessageHandler(GameMessageHandler gameMessageHandler) {
        this.gameMessageHandler = gameMessageHandler;
    }

    /**
     * Checks if the player is still connected with a Ping-Pong protocol.
     */
    public void checkConnection() {
        while (isConnected()) {
            sendMessage(new PingMessage());
            long start = System.currentTimeMillis();
            long end = start + 10*1000; // 10 seconds * 1000 ms/sec
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

    /**
     * Gets the gameMessageHandler
     * @return gameMessageHanndler
     */
    public GameMessageHandler getGameMessageHandler() {
        return gameMessageHandler;
    }

    /**
     * True if the player is connected
     * @return true if the player is connected
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Sets connected.
     * @param connected true if the player is connected, false otherwise.
     */
    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    /**
     * Gets the server.
     * @return server.
     */
    public Server getServer() {
        return this.server;
    }
}
