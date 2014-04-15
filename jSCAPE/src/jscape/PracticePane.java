/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jscape;

import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.HTMLEditor;

/**
 *
 * @author achantreau
 */
class PracticePane extends SplitPane {
    
    private BorderPane leftSplitPane;
    private BorderPane rightSplitPane;
    
    public PracticePane() {
        super();
        
        leftSplitPane = new BorderPane();
        leftSplitPane.setCenter(new HTMLEditor());
        
        rightSplitPane = new BorderPane();
        rightSplitPane.setCenter(new TextArea());
        
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        getItems().addAll(leftSplitPane, rightSplitPane);
        setDividerPosition(0, 0.5);
    }
    
    
}
