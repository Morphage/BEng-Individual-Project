/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author achantreau
 */
public class Server implements Runnable {

    private final Thread server;
    
    private final int port;
    
    private Set<ServerThread> connections = new HashSet<ServerThread>();
    
    private static boolean isRunning = true;
    
    private Server(int port) {
        this.port = port;
        
        server = new Thread(this);
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            
            while (isRunning) {
                Socket socket = serverSocket.accept();

                ServerThread serverThread = new ServerThread(this, socket);
                addConnection(serverThread);
                serverThread.start();
            }
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }
    
    public void start() {
        serverOutput("Starting on port " + port + ". Ready to accept incoming connections.");
        server.start();
    }
    
    public void stop() {
        serverOutput("Shutting down");
        isRunning = false;
    }
    
    synchronized void addConnection(ServerThread serverThread) {
        InetAddress ia = serverThread.getInet();
        serverOutput("Receiving connection from " + ia.getHostName() + " (" + ia.getHostAddress() + ")");
        connections.add(serverThread);
    }
    
    synchronized void removeConnection(ServerThread serverThread) {
        InetAddress ia = serverThread.getInet();
        serverOutput("Removing connection from " + ia.getHostName() + " (" + ia.getHostAddress() + ")");
        connections.remove(serverThread);
    }
    
    public void serverOutput(String output) {
        System.out.println("[Server] " + output);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java Server <port number>");
            System.exit(-1);
        }

        int port = Integer.parseInt(args[0]);

        new Server(port).start();
    }
}
