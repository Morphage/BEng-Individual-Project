/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jscape.server.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import jscape.communication.Message;
import jscape.communication.MessageCode;

/**
 *
 * @author achantreau
 */
public class ServerConnectionTest implements Runnable {

    Thread serverConnection;

    private ObjectInputStream oin;
    private ObjectOutputStream oos;

    private final String host;
    private final int port;

    private static Message reply = null;

    public ServerConnectionTest(String host, int port) {
        this.host = host;
        this.port = port;

        serverConnection = new Thread(this);
    }

    @Override
    public void run() {
        System.out.println("Run method called");
        //writeMessage();
        //while(true) { 
        try {
            reply = (Message) oin.readObject();
            ArrayList<String> payload = reply.getPayload();

            for (int i = 0; i < payload.size(); i++) {
                System.out.println(payload.get(i));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        //}
    }

    public void start() {
        serverConnection.start();
        System.out.println("Start method called");
    }

    public void connect() {
        try {
            Socket socket = new Socket(host, port);
            oos = new ObjectOutputStream(socket.getOutputStream());
            oin = new ObjectInputStream(socket.getInputStream());
        } catch (ConnectException ce) {
            System.out.println("The server at " + host + ":" + port + " is down");
            System.exit(-1);
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public void writeMessage() {
        System.out.println("Write method called");
        ArrayList<String> payload = new ArrayList<>();
        payload.add("ac6609");

        Message message = new Message(MessageCode.EXERCISE_CATEGORIES, payload);
        try {
            oos.writeObject(message);
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String host = "localhost";
        int port = 9000;

        ServerConnectionTest sc = new ServerConnectionTest(host, port);
        sc.connect();
        sc.start();
        sc.writeMessage();
    }
}
