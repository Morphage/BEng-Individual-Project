/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jscape.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author achantreau
 */
public class Server {
    
    private final int port;
    
    private Set<ServerThread> connections;
    
    private static boolean isRunning = true;
    
    private Server(int port) {
        this.port = port;
        connections = new HashSet<>();
    }

    public void run() {
        serverOutput("Starting on port " + port + ". Ready to accept incoming connections.");
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            
            while (isRunning) {
                Socket socket = serverSocket.accept();

                ServerThread serverThread = new ServerThread(this, socket);
                addConnection(serverThread);
                new Thread(serverThread).start();
            }
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }
        
    public void stop() {
        serverOutput("Shutting down");
        isRunning = false;
    }
    
    synchronized void addConnection(ServerThread serverThread) {
        serverOutput("Receiving connection from " + serverThread.getConnectionInfo());
        connections.add(serverThread);
    }
    
    synchronized void removeConnection(ServerThread serverThread) {
        serverOutput("Removing connection from " + serverThread.getConnectionInfo());
        connections.remove(serverThread);
    }
    
    public synchronized void serverOutput(String output) {
        System.out.println("[Server] " + output);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java Server <port number>");
            System.exit(-1);
        }

        int port = Integer.parseInt(args[0]);

        Server server = new Server(port);
        server.run();
    }
}
