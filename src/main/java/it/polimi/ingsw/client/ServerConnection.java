package it.polimi.ingsw.client;

import it.polimi.ingsw.network.ClientMessageHandler;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.client.ClientMessage;
import it.polimi.ingsw.network.pingpong.PongMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerConnection implements Runnable {
    private final Socket socket;
    private final Client client;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private ClientMessageHandler clientMessageHandler;

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

    public Message receiveMessage() {
        Message m = null;
        try {
            m = (Message) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return m;
    }

    public Client getClient() {
        return client;
    }

    public void sendMessage(Message m) {
        try {
            //outputStream.reset();
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

    @Override
    public void run() {
        while (true) {
            Message m = receiveMessage();
            //System.out.println(m + " " + m.getType());
            if (m.getType().equals("CONNECTION")) {
                sendMessage(new PongMessage());
            } else {
                ((ClientMessage) m).accept(clientMessageHandler);
            }
        }
    }
}
