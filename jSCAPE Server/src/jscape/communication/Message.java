/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jscape.communication;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author achantreau
 */
public class Message implements Serializable {
    private MessageCode messageCode;
    private ArrayList<String> payload;
    
    public Message(MessageCode messageCode, ArrayList<String> payload) {
        this.messageCode = messageCode;
        this.payload = payload;
    }
    
    public MessageCode getMessageCode() {
        return messageCode;
    }
    
    public ArrayList<String> getPayload() {
        return payload;
    }
}
