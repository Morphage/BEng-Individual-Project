/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jscape.help;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 *
 * @author achantreau
 */
public class HelpPane extends BorderPane {
    
    public static final String DEFAULT_URL = "http://www.doc.ic.ac.uk/~ac6609/manual.html";
    
    public HelpPane() {
        super();
        
        WebView webView = new WebView();
         
        final WebEngine webEngine = webView.getEngine();
        webEngine.load(DEFAULT_URL);
         
        VBox vBox = new VBox(5);
        vBox.getChildren().setAll(webView);
        VBox.setVgrow(webView, Priority.ALWAYS);
        
        setCenter(vBox);
        
        /* FOr HelpPane use a WebEditor where I can display manual as a HTML file
        or website, with hyperlinks to access parts of the manual. CSS for images, styling,
        then link the manual in as appendix for the report!
        */
    }
}
