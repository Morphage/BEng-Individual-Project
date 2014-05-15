/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jscape.communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javafx.concurrent.Task;

/**
 *
 * @author achantreau
 */
public class RequestServerTask extends Task<Message> {
    
    private Socket socket;
    
    private ObjectInputStream oin;
    private ObjectOutputStream oos;
    
    private final String host;
    private final int    port;
    
    private final Message requestMessage;
    
    public RequestServerTask(String host, int port, Message requestMessage) {
        this.host = host;
        this.port = port;
        this.requestMessage = requestMessage;
    }

    @Override
    protected Message call() throws Exception {
        System.out.println("Call to " + Thread.currentThread());
        Message replyMessage = null;
        
        try {
            connect();
            oos.writeObject(requestMessage);
            replyMessage = (Message) oin.readObject();
        } catch(IOException ie) {
            
        } finally {
            oos.close();
            oin.close();
            socket.close();
        }
        
        return replyMessage;
    }
    
    public void connect() throws IOException {
        socket = new Socket(host, port);
        oos = new ObjectOutputStream(socket.getOutputStream());
        oin = new ObjectInputStream(socket.getInputStream());
    }
    
}
