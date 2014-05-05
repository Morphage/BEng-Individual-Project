/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jscape.server;

import jscape.database.StudentTable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import jscape.communication.Message;
import jscape.communication.MessageCode;

/**
 *
 * @author achantreau
 */
class ServerThread implements Runnable {
    
    private final Thread serverThread;
    
    private Server server;
    private Socket socket;
    
    private ObjectInputStream oin;
    private ObjectOutputStream oos;
    
    private static boolean isRunning = true;

    public ServerThread(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
        
        serverThread = new Thread(this);
    } 

    @Override
    public void run() {
        try {
            setupStreams();
            handleCommunication();
        } catch (SocketException se) {
            server.serverOutput("Connection closed from " + getConnectionInfo());
        } catch (IOException ie) {
            server.serverOutput("Unable to setup input and output streams.");
        } catch (ClassNotFoundException ex) {
            server.serverOutput("Error in communication protocol. Unable to determine data format.");
        } finally {
            server.removeConnection(this);
        }
    }
    
    public void start() {
        serverThread.start();
    }
    
    public void stop() {
        isRunning = false;
    }
    
    private void setupStreams() throws IOException {
        oin = new ObjectInputStream(socket.getInputStream());
        oos = new ObjectOutputStream(socket.getOutputStream());
    }
    
    private void handleCommunication() throws IOException, ClassNotFoundException {
        while(isRunning) {
            Message request = (Message) oin.readObject();
            
            MessageCode messageCode = request.getMessageCode();
            server.serverOutput("Received request for " + messageCode
                    + " (Message code: " + messageCode.getValue() + ")");
            
            Message reply = createReply(request);
            oos.writeObject(reply);
        }
    }
    
    private Message createReply(Message request) {
        switch (request.getMessageCode()) {
            case PROFILE_INFO:
                return getProfileInfo(request);
            case EXERCISE_CATEGORIES:
                return null;
        }
        
        return null;
    }
    
    private Message getProfileInfo(Message request) {        
        String loginName = request.getPayload().get("loginName");
        
        MessageCode messageCode = MessageCode.PROFILE_INFO;
        HashMap<String,String> payload = StudentTable.getProfileInfo(loginName);
        
        return new Message(messageCode, payload);
    }

    public String getConnectionInfo() {
        InetAddress ia  = socket.getInetAddress();
        return (ia.getHostName() + " (" + ia.getHostAddress() + ")");
    }
}
