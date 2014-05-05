/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jscape.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.HashMap;
import jscape.communication.Message;
import jscape.communication.MessageCode;

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
    
    private static Message reply = null;
    
    public ServerConnection(String host, int port) {
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
                HashMap<String,String> payload = reply.getPayload();
                System.out.println("First name= " + payload.get("firstName"));
                System.out.println("Last name= " + payload.get("lastName"));
                System.out.println("Last login= " + payload.get("lastLogin"));
                System.out.println("Last question answered= " + payload.get("lastQuestionAnswered"));
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
    
    public static void main(String[] args) {
        String host = "localhost";
        int port = 9000;
        
        ServerConnection sc = new ServerConnection(host, port);
        sc.connect();
        sc.start();
        sc.writeMessage();
    }
    
    public void writeMessage() {
        System.out.println("Write method called");
        HashMap<String,String> payload = new HashMap<>();
        payload.put("loginName", "jd4510");
        
        Message message = new Message(MessageCode.PROFILE_INFO, payload);
        try {
            oos.writeObject(message);
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }    
}
