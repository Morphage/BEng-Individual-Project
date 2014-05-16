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
import java.util.ArrayList;
import jscape.communication.Message;
import jscape.communication.MessageCode;
import jscape.database.CategoryTable;
import jscape.database.ExerciseBankTable;
import jscape.database.PerformanceTable;

/**
 *
 * @author achantreau
 */
class ServerThread implements Runnable {
        
    private Server server;
    private Socket socket;
    
    private ObjectInputStream oin;
    private ObjectOutputStream oos;

    public ServerThread(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
    } 

    @Override
    public void run() {
        try {
            setupStreams();
            handleCommunication();
        } catch (SocketException se) {
            server.serverOutput("Connection closed from " + getConnectionInfo());
        } catch (IOException ie) {
            ie.printStackTrace();
            server.serverOutput("Unable to setup input and output streams.");
        } catch (ClassNotFoundException ex) {
            server.serverOutput("Error in communication protocol. Unable to determine data format.");
        } finally {
            server.removeConnection(this);
            try {
                oos.close();
                oin.close();
                socket.close();
            } catch (IOException ex) {
                
            }
        }
    }
    
    private void setupStreams() throws IOException {
        oin = new ObjectInputStream(socket.getInputStream());
        oos = new ObjectOutputStream(socket.getOutputStream());
    }
    
    private void handleCommunication() throws IOException, ClassNotFoundException {
        Message request = (Message) oin.readObject();

        MessageCode messageCode = request.getMessageCode();
        server.serverOutput("Received request for " + messageCode
                + " (Message code: " + messageCode.getValue() + ")");

        Message reply = createReply(request);
        oos.writeObject(reply);
    }
    
    private Message createReply(Message request) {
        MessageCode messageCode = request.getMessageCode();
        ArrayList<String> payload = null;
        
        switch (messageCode) {
            case PROFILE_INFO:
                payload = StudentTable.getProfileInfo(request.getPayload().get(0));
                StudentTable.updateLastLogin(request.getPayload().get(0));
                break;
            case PERFORMANCE_STATS:
                payload = PerformanceTable.getPerformanceStats(request.getPayload().get(0));
                break;
            case EXERCISE_CATEGORIES:
                payload = CategoryTable.getExerciseCategories();
                break;
            case GET_EXERCISE:
                payload = ExerciseBankTable.getExercise(request.getPayload().get(0), 
                        request.getPayload().get(1));
                break;                
        }
        
        return new Message(messageCode, payload);
    }

    public String getConnectionInfo() {
        InetAddress ia  = socket.getInetAddress();
        return (ia.getHostName() + " (" + ia.getHostAddress() + ")");
    }
}