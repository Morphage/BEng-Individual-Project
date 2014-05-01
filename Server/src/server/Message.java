/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import java.io.Serializable;

/**
 *
 * @author achantreau
 */
public class Message implements Serializable {
    
    public int messageCode;
    public String parameter1;
    public String parameter2;
    public String parameter3;
    
    public Message(int messageCode, String parameter1, String parameter2, String parameter3) {
        this.messageCode = messageCode;
        this.parameter1 = parameter1;
        this.parameter2 = parameter2;
        this.parameter3 = parameter3;
    }
    
    @Override
    public String toString() {
        return "(" + messageCode + ", " + parameter1 + ", " + parameter2 + ", " + parameter3 + ")";
    }
}
