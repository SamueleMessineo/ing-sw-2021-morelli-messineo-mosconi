package it.polimi.ingsw.client;

import it.polimi.ingsw.network.ClientMessageHandler;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.client.ClientMessage;
import it.polimi.ingsw.network.setup.CreateRoomMessage;
import it.polimi.ingsw.server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerConnection{
    private final Socket socket;
    private final Client client;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private ClientMessageHandler clientMessageHandler;

    public ServerConnection(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
        this.clientMessageHandler = new ClientMessageHandler(client, this);
        System.out.println("costructor");

        try {
            inputStream  = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("ok");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void waitForMessages() {
        while (true) {
            System.out.println("running");
            sendMessage(new CreateRoomMessage(false, 2, "Mac"));

            Message m = receiveMessage();

            ((ClientMessage) m).accept(clientMessageHandler);


            break;
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
            outputStream.writeObject(m);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
