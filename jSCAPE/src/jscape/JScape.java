/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jscape;

import jscape.practice.PracticePane;
import jscape.profile.ProfilePane;
import jscape.about.AboutPane;
import jscape.help.HelpPane;
import javafx.application.Application;
import javafx.scene.DepthTest;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author achantreau
 */
public class JScape extends Application {
    
    // GUI elements constants, e.g. size, orientation, etc...
    
    private static final String HOST = "localhost";
    //private static final String HOST = "10.187.195.124";
    private static final int    PORT = 9000;
    
    private BorderPane rootPane;
    
    private TabPane tabPane;
    
    private ProfilePane  profilePane;
    private PracticePane practicePane;
    private HelpPane     helpPane;
    private AboutPane    aboutPane;
    
    private static JScape jSCAPE;
    
    // DEBUG variables/constants - remove when program is done
    private static final boolean DEBUG = true;
    
    public static JScape getJSCAPE() {
        return jSCAPE;
    }
    
    @Override
    public void start(final Stage stage) {
        jSCAPE = this;
        StackPane layerPane = new StackPane();
                
        rootPane = new BorderPane();
        rootPane.setId("Window");
        layerPane.setDepthTest(DepthTest.DISABLE);
        layerPane.getChildren().add(rootPane);
                        
        Scene scene = new Scene(layerPane, 1020, 700);
        scene.getStylesheets().add(JScape.class.getResource("jscape.css").toExternalForm());       
        
        profilePane = new ProfilePane();
        profilePane.runFetchPerformanceStatsTask();
        profilePane.runFetchProfileInfoTask();
        
        practicePane = new PracticePane();
        practicePane.runFetchCategoriesTask();
        
        helpPane = new HelpPane();
        aboutPane = new AboutPane();
        
        tabPane = new TabPane();
        tabPane.setId("MainTabs");
        
        Tab profileTab = new Tab("PROFILE");
        profileTab.setContent(profilePane);
        
        Tab practiceTab = new Tab("PRACTICE");
        practiceTab.setContent(practicePane);
        
        Tab helpTab = new Tab("HELP");
        helpTab.setContent(helpPane);
        
        Tab aboutTab = new Tab("ABOUT");
        aboutTab.setContent(aboutPane);
        
        tabPane.getTabs().addAll(profileTab, practiceTab, helpTab, aboutTab);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        this.rootPane.setCenter(tabPane);
                                
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Java Main Method for launching application when not using JavaFX 
     * Launcher, e.g. from IDE without JavaFX support
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}
