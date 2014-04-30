/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jscape;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.DepthTest;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jscape.controls.BreadcrumbBar;

/**
 *
 * @author achantreau
 */
public class JScape extends Application {
    
    private BorderPane rootPane;
    private ToolBar toolBar;
    private ToolBar pageToolBar;
    private BreadcrumbBar breadcrumbBar;
    
    private BorderPane startPane;
    
    private ProfilePane profilePane;
    private PracticePane practicePane;
    private HelpPane helpPane;
    private AboutPane aboutPane;
    
    private final Button startButton = new Button("Welcome to jSCAPE - click one of the buttons above to change display");
    
    // DEBUG variables/constants - remove when program is done
    private static final boolean DEBUG = true;
        
    // GUI elements constants, e.g. size, orientation, etc...
    private static final int MAIN_TOOLBAR_HEIGHT = 66;
    
    @Override
    public void start(final Stage stage) {
        StackPane layerPane = new StackPane();
                
        rootPane = new BorderPane();
        rootPane.setId("root");
        rootPane.getStyleClass().add("applet");
        layerPane.setDepthTest(DepthTest.DISABLE);
        layerPane.getChildren().add(rootPane);
                        
        Scene scene = new Scene(layerPane, 1020, 700);
        scene.getStylesheets().add(JScape.class.getResource("jscape.css").toExternalForm());
        
        toolBar = new ToolBar();
        toolBar.setId("mainToolBar");
        ImageView logo = new ImageView(new Image(JScape.class.getResourceAsStream("images/logo.png")));
        HBox.setMargin(logo, new Insets(0,10,0,5));
        //HBox.setMargin(logo, new Insets(0,0,0,5));
        toolBar.getItems().add(logo);
        
        Separator separator = new Separator();
        separator.setPrefHeight(MAIN_TOOLBAR_HEIGHT);
        separator.setMinHeight(MAIN_TOOLBAR_HEIGHT);
        separator.setMaxHeight(MAIN_TOOLBAR_HEIGHT);
        HBox.setMargin(separator, new Insets(0,150,0,5));
        toolBar.getItems().add(separator);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        //toolBar.getItems().add(spacer);
        
        Button profileButton = new Button("PROFILE");
        //profileButton.setId("profileButton");
        profileButton.setMinSize(120, MAIN_TOOLBAR_HEIGHT);
        profileButton.setPrefSize(120, MAIN_TOOLBAR_HEIGHT);
        profileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                startPane.setCenter(profilePane);
            }
        });
        toolBar.getItems().add(profileButton);
        
        Button practiceButton = new Button();
        practiceButton.setText("PRACTICE");
        practiceButton.setId("practiceButton");
        practiceButton.setMinSize(120, MAIN_TOOLBAR_HEIGHT);
        practiceButton.setPrefSize(120, MAIN_TOOLBAR_HEIGHT);
        practiceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                startPane.setCenter(practicePane);
            }
        });
        toolBar.getItems().add(practiceButton);
        
        Button helpButton = new Button();
        helpButton.setText("HELP");
        helpButton.setId("helpButton");
        helpButton.setMinSize(120, MAIN_TOOLBAR_HEIGHT);
        helpButton.setPrefSize(120, MAIN_TOOLBAR_HEIGHT);
        helpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                startPane.setCenter(helpPane);
            }
        });
        toolBar.getItems().add(helpButton);
        
        Button aboutButton = new Button();
        aboutButton.setText("ABOUT");
        aboutButton.setId("aboutButton");
        aboutButton.setMinSize(120, MAIN_TOOLBAR_HEIGHT);
        aboutButton.setPrefSize(120, MAIN_TOOLBAR_HEIGHT);
        aboutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                startPane.setCenter(aboutPane);
            }
        });
        toolBar.getItems().add(aboutButton);        
        
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        toolBar.getItems().add(spacer2);
                
        /*toolBar.setPrefHeight(MAIN_TOOLBAR_HEIGHT);
        toolBar.setMinHeight(MAIN_TOOLBAR_HEIGHT);
        toolBar.setMaxHeight(MAIN_TOOLBAR_HEIGHT);*/
        toolBar.setPrefHeight(MAIN_TOOLBAR_HEIGHT+20);
        toolBar.setMinHeight(MAIN_TOOLBAR_HEIGHT+20);
        toolBar.setMaxHeight(MAIN_TOOLBAR_HEIGHT+20);
        GridPane.setConstraints(toolBar, 0, 0);
        
        pageToolBar = new ToolBar();
        pageToolBar.setId("page-toolbar");
        pageToolBar.setMinHeight(29);
        pageToolBar.setMaxWidth(Double.MAX_VALUE);        
        
        breadcrumbBar = new BreadcrumbBar();
        pageToolBar.getItems().add(new Button("Toolbar Test"));
        pageToolBar.getItems().add(breadcrumbBar);
                
        startPane = new BorderPane();
        startPane.setCenter(startButton);
        startPane.setTop(pageToolBar);
        
        GridPane.setConstraints(startPane, 0, 1);
        
        profilePane = new ProfilePane();
        practicePane = new PracticePane();
        helpPane = new HelpPane();
        aboutPane = new AboutPane();
        
        
        
        this.rootPane.setTop(toolBar);
        this.rootPane.setCenter(startPane);
                                
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
