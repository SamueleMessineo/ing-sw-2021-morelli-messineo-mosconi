package it.polimi.ingsw.server;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageHandler;
import it.polimi.ingsw.network.MessageTypeHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnection implements Runnable{

    private final Socket socket;
    private final Server server;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public ClientConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        try {
            inputStream  = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            Message m = receiveMessage();
        }
    }

    public Message receiveMessage() {
        Message m = null;
        try {
            m = (Message) inputStream.readObject();
            MessageTypeHandler handler = new MessageTypeHandler();
            m.accept(handler);
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
}
