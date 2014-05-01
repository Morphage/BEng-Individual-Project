/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

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
        } catch (IOException ie) {
            ie.printStackTrace();
            server.removeConnection(this);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
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
        int i = 0;
        while(i < 2) {
            Object message = oin.readObject();
            
            server.serverOutput("Received message " + message.toString());
            i++;
        }
    }

    public InetAddress getInet() {
        return socket.getInetAddress();
    }
}
