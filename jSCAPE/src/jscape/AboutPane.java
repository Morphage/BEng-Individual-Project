/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jscape;

import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author achantreau
 */
class AboutPane extends BorderPane {
    
    private JScape jscape;
    
    private Text textArea;
    
    public AboutPane() {
        super();
        
        textArea = new Text("This project was developed as part of my BEng Computing"
                + " final year project in 2014. All rights reserved, Alexis Chantreau,"
                + " Copyright C 2014 \n"
                + "The project was implemented using JavaFX for the GUI, Java for the "
                + " client side and server side, and PostreSQL for the database management"
                + " system");
        
        /* jSCAPE (Java Self-assessment Center of Adaptive Programming Exercises) is a
           blablabla + description + jSCAPE logo + version number
        */
        
        //textArea.getStyleClass().add("right-sidebar");
        textArea.setFont(Font.font("Verdana", 14));
        getStyleClass().add("category-page");
        this.setCenter(textArea);              
    }
    
}
