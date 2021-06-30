package it.polimi.ingsw.client;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.view.UI;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Client class, it has the calls the serverConnection to communicate with the server.
 */
public class Client {
    private final UI ui;
    private Socket socket;
    ServerConnection serverConnection;

    /**
     * Client class constructor.
     */
    public Client(UI ui) {
        this.ui = ui;
    }

    /**
     * Connects to the server.
     * @param ip server ip.
     * @param port server port.
     * @throws IOException if it cannot connect to the server.
     */
    public void connect(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        serverConnection = new ServerConnection(socket, this);
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(serverConnection);
    }

    /**
     * Starts the ui.
     */
    public void run() {
            ui.setup();
    }

    /**
     * Sends a new message to the server.
     * @param m the message to send.
     */
    public void sendMessage(Message m){
        serverConnection.sendMessage(m);
    }

    /**
     * Gets the UI.
     * @return ui.
     */
    public UI getUi() {
        return ui;
    }

    /**
     * Closes the server connection.
     */
    public void closeConnection(){
        serverConnection.close();
    }
}
