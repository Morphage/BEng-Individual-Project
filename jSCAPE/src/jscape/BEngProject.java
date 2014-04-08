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
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author achantreau
 */
public class BEngProject extends Application {
    
    private BorderPane root;
    private ToolBar toolBar;
    
    private double mouseDragOffsetX = 0;
    private double mouseDragOffsetY = 0;
    
    @Override
    public void start(final Stage stage) {
        StackPane layerPane = new StackPane();
        
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
        
        stage.initStyle(StageStyle.UNDECORATED);
        
        root = new BorderPane();
        root.setId("root");
        root.getStyleClass().add("applet");
        root.getChildren().add(btn);
        layerPane.setDepthTest(DepthTest.DISABLE);
        layerPane.getChildren().add(root);
                
        Scene scene = new Scene(layerPane, 1020, 700);
        scene.getStylesheets().add(BEngProject.class.getResource("ensemble2.css").toExternalForm());
        
        toolBar = new ToolBar();
        toolBar.setId("mainToolBar");
        ImageView logo = new ImageView(new Image(BEngProject.class.getResourceAsStream("images/logo.png")));
        HBox.setMargin(logo, new Insets(0,0,0,5));
        toolBar.getItems().add(logo);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        toolBar.getItems().add(spacer);
        
        Button highlightsButton = new Button();
        highlightsButton.setId("highlightsButton");
        highlightsButton.setMinSize(120, 66);
        highlightsButton.setPrefSize(120, 66);
        highlightsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                
            }
        });
        toolBar.getItems().add(highlightsButton);
        
        Button newButton = new Button();
        newButton.setId("newButton");
        newButton.setMinSize(120,66);
        newButton.setPrefSize(120,66);
        newButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                
            }
        });
        toolBar.getItems().add(newButton);
        
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        toolBar.getItems().add(spacer2);
        
        
        toolBar.setPrefHeight(66);
        toolBar.setMinHeight(66);
        toolBar.setMaxHeight(66);
        GridPane.setConstraints(toolBar, 0, 0);
        
        if (true) {
            // add close min max
            final WindowButtons windowButtons = new WindowButtons(stage);
            toolBar.getItems().add(windowButtons);
            // add window header double clicking
            toolBar.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent event) {
                    if (event.getClickCount() == 2) {
                        windowButtons.toogleMaximized();
                    }
                }
            });
            // add window dragging
            toolBar.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent event) {
                    mouseDragOffsetX = event.getSceneX();
                    mouseDragOffsetY = event.getSceneY();
                }
            });
            toolBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent event) {
                    if(!windowButtons.isMaximized()) {
                        stage.setX(event.getScreenX()-mouseDragOffsetX);
                        stage.setY(event.getScreenY()-mouseDragOffsetY);
                    }
                }
            });
        }
        
        ToolBar pageTreeToolBar = new ToolBar();
        pageTreeToolBar.setId("page-tree-toolbar");
        pageTreeToolBar.setMinHeight(29);
        pageTreeToolBar.setMaxWidth(Double.MAX_VALUE);
        
        
        ToggleGroup pageButtonGroup = new ToggleGroup();
        final ToggleButton allButton = new ToggleButton("Profile");
        allButton.setToggleGroup(pageButtonGroup);
        allButton.setSelected(true);
        final ToggleButton samplesButton = new ToggleButton("Practice Exercises");
        samplesButton.setToggleGroup(pageButtonGroup);
        final ToggleButton docsButton = new ToggleButton("Document");
        docsButton.setToggleGroup(pageButtonGroup);
        final ToggleButton helpButton = new ToggleButton("Help");
        helpButton.setToggleGroup(pageButtonGroup);
        final ToggleButton aboutButton = new ToggleButton("About");
        aboutButton.setToggleGroup(pageButtonGroup);
        
        pageTreeToolBar.getItems().addAll(allButton, samplesButton, docsButton, helpButton, aboutButton);
        
        BorderPane leftSplitPane = new BorderPane();
        leftSplitPane.setTop(pageTreeToolBar);
        
        this.root.setTop(toolBar);
        this.root.setCenter(leftSplitPane);
        
        stage.setTitle("Testing Stuff");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
