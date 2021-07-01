package it.polimi.ingsw.client;

import it.polimi.ingsw.network.ClientMessageHandler;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.client.ClientMessage;
import it.polimi.ingsw.network.client.ErrorMessage;
import it.polimi.ingsw.network.pingpong.PongMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * The SeverConnection class, it has the sockets to communicate with the Server.
 */
public class ServerConnection implements Runnable {
    private final Socket socket;
    private final Client client;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private final ClientMessageHandler clientMessageHandler;

    /**
     * ServerConnection class constructor.
     */
    public ServerConnection(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
        this.clientMessageHandler = new ClientMessageHandler(client, this);

        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream  = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Waits for messages from the Server.
     */
    public void waitForMessages() {
        while (true) {
            Message m = receiveMessage();
            if (m.getType().equals("CONNECTION")) {
                sendMessage(new PongMessage());
            } else {
                ((ClientMessage) m).accept(clientMessageHandler);
            }
        }
    }

    /**
     * Gets the next message in the inputStream, if connection is broken signals it to the user and closes the App.
     * @return the received message.
     */
    public Message receiveMessage() {
        Message m = null;
        try {
            m = (Message) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            ErrorMessage message = new ErrorMessage("Server unavailable :(");
            message.accept(clientMessageHandler);
            System.exit(404);
        }
        return m;
    }

    /**
     * Gets the client of the connection.
     * @return the client associated to that connection.
     */
    public Client getClient() {
        return client;
    }

    /**
     * Sends a new message  to the server.
     * @param m the new message to send to the server.
     */
    public void sendMessage(Message m) {
        try {
            outputStream.reset();
            outputStream.writeObject(m);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the socket.
     */
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the message received is a connection of game message. If is connection sends the pong else forwards
     * it to the ui.
     */
    @Override
    public void run() {
        while (true) {
            Message m = receiveMessage();
            if (m.getType().equals("CONNECTION")) {
                sendMessage(new PongMessage());
            } else {
                ((ClientMessage) m).accept(clientMessageHandler);
            }
        }
    }
}
