/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jscape.communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.HashMap;
import jscape.JScape;

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
    
    private JScape jscape;
    
    public ServerConnection(String host, int port, JScape jscape) {
        this.host = host;
        this.port = port;
        this.jscape = jscape;
        
        serverConnection = new Thread(this);
    }
    
    public void start() {
        serverConnection.start();
        System.out.println("Start method called");
    }

    @Override
    public void run() {
        System.out.println("Run method called");
        while(true) {
            try {
                Message reply = (Message) oin.readObject();
                
                switch (reply.getMessageCode()) {
                    case PROFILE_INFO:
                        profileInfo(reply);
                        break;
                }
                
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void profileInfo(Message reply) {
        HashMap<String,String> payload = reply.getPayload();
        
        jscape.updateJSCAPE(payload);
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
    
    public void writeMessage(Message message) {
        System.out.println("Write method called");
        
        try {
            oos.writeObject(message);
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    } 
}
