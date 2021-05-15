package it.polimi.ingsw.client;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.view.UI;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    private final UI ui;
    private Socket socket;
    ServerConnection serverConnection;

    public Client(UI ui) {
        this.ui = ui;
    }

    public void connect(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        serverConnection = new ServerConnection(socket, this);
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(serverConnection);
    }

    public void run() {
        try {
            socket = new Socket("localhost", 31415);
            serverConnection = new ServerConnection(socket, this);
            ExecutorService executor = Executors.newCachedThreadPool();
            executor.submit(serverConnection);
            ui.setup();
        } catch (IOException e) {
            System.out.println("Server not available :(");
            System.out.println("Exiting...");
        }
    }

    public void sendMessage(Message m){
        serverConnection.sendMessage(m);
    }

    public UI getUi() {
        return ui;
    }

    public void closeConnection(){
        serverConnection.close();
    }
}
