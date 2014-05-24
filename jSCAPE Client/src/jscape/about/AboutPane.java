/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jscape.about;

import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import jscape.JScape;

/**
 *
 * @author achantreau
 */
public class AboutPane extends BorderPane {
        
    private Text textArea;
    
    public AboutPane() {
        super();
        
        VBox vbox = new VBox();
        textArea = new Text("jSCAPE (Java Self-assessment Center of Adaptive Programming Exercises) is a web application designed to give"
                + " student the opportunity to practice their knowledge of programming concepts and theory, while tracking their progress"
                + " using statistical data.\n\n"
                + "This application was developed as part of my BEng Computing final year project in 2014. Technologies used include Java,"
                + " JavaFX, Javascript, HTML/CSS and SQL.\n\n"
                + "Copyright " +  "\u00a9"  + " 2014 Alexis Chantreau\n");
        textArea.setWrappingWidth(600);        
        textArea.setFont(Font.font("Arial", 14));
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().add(textArea);
        getStyleClass().add("category-page");
        this.setCenter(vbox);              
    }
    
}
