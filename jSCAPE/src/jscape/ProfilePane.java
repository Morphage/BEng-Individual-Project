/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jscape;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author achantreau
 */
class ProfilePane extends BorderPane {
    
    private Button button = new Button("PROFILE PANE");
    
    public ProfilePane() {
        super();
        
        button.setVisible(true);
        //this.getChildren().add(button);
        this.setCenter(button);        
    }
    
}
