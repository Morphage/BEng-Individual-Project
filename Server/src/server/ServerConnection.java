/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author achantreau
 */
public class ServerConnection implements Runnable {
    
    Thread serverConnection;
    
    private ObjectInputStream oin;
    private ObjectOutputStream oos;
    
    private final String host;
    private final int    port;
    
    public ServerConnection(String host, int port) {
        this.host = host;
        this.port = port;
        
        serverConnection = new Thread(this);
    }

    @Override
    public void run() {
        System.out.println("Run method called");
        while(true) { /*
            try {
                Object obj = oin.readObject();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } */
        }
    }
    
    public void start() {
        connect(host, port);
        serverConnection.start();
    }
    
    public void connect(String host, int port) {
        try {
            Socket socket = new Socket(host, port);
            oos = new ObjectOutputStream(socket.getOutputStream());
            oin = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        String host = "localhost";
        int port = 9000;
        
        ServerConnection sc = new ServerConnection(host, port);
        sc.start();
        sc.writeMessage();
    }
    
    public void writeMessage() {
        System.out.println("Write method called");
        Message message = new Message(17, "ac6609", "Alexis", "Chantreau");
        try {
            oos.writeObject(message);
            message = new Message(23, "lg9078", "Luke", "Grant");
            oos.writeObject(message);
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }    
}
