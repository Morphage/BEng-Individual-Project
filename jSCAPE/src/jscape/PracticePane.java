/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jscape;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author achantreau
 */
class PracticePane extends BorderPane {
    
    public PracticePane() {
        super();
        
        VBox main = new VBox(80);
        
        // Add JAVA PROGRAMMING EXERCISES title bar
        Label javaHeader = new Label("      JAVA PROGRAMMING EXERCISES");
        javaHeader.setMaxWidth(Double.MAX_VALUE);
        javaHeader.setMinHeight(Control.USE_PREF_SIZE);
        javaHeader.getStyleClass().add("category-header");
        main.getChildren().add(javaHeader);
        
        /*
        Label emptyHeader = new Label("");
        emptyHeader.setMaxWidth(Double.MAX_VALUE);
        emptyHeader.setMinHeight(Control.USE_PREF_SIZE);
        main.getChildren().add(emptyHeader);*/
  
        // Add grid of exercise categories, e.g. arrays, syntax, data structures...
        GridPane exerciseCategories = new GridPane();
        exerciseCategories.setHgap(50);
        exerciseCategories.setVgap(50);
        exerciseCategories.setPadding(new Insets(0, 10, 1000, 10));
        exerciseCategories.getStyleClass().add("category-page-flow");
        for (int i = 0; i < 10; i++) {
            Node tile = createTile();
            GridPane.setConstraints(tile, i%5, i/5);
            exerciseCategories.getChildren().add(tile);
        }
        exerciseCategories.setAlignment(Pos.CENTER);
        main.getChildren().add(exerciseCategories);
        
        main.getStyleClass().add("category-page");
        //exerciseCategories.setGridLinesVisible(true);
        
        setCenter(main);
    }
    
    public Node createTile() {
        String[] categories = {"Arrays", "Syntax", "Strings", "Loops", "Conditionals",
            "Binary Trees", "Objects"
        };
        
        int i = (int)(Math.random() * 7);
        
        ImageView buttonLogo = new ImageView(new Image(JScape.class.getResourceAsStream("images/StackedAreaChartSample.png")));
        Button tile = new Button(categories[i], buttonLogo);
        //Button tile = new Button("Arrays", getIcon());
        tile.setMinSize(140,145);
        tile.setPrefSize(140,145);
        tile.setMaxSize(140,145);
        tile.setContentDisplay(ContentDisplay.TOP);
        tile.getStyleClass().clear();
        tile.getStyleClass().add("sample-tile");
        tile.setOnAction(new EventHandler() {
            public void handle(Event event) {
                //Ensemble2.getEnsemble2().goToPage(SamplePage.this);
            }
        });
        return tile;
    }
    
    private Node getIcon() {
            ImageView imageView = new ImageView(new Image(JScape.class.getResource("images/icon-overlay.png").toString()));
            imageView.setMouseTransparent(true);
            Rectangle overlayHighlight = new Rectangle(-8,-8,130,130);
            overlayHighlight.setFill(new LinearGradient(0,0.5,0,1,true, CycleMethod.NO_CYCLE, new Stop[]{ new Stop(0,Color.BLACK), new Stop(1,Color.web("#444444"))}));
            overlayHighlight.setOpacity(0.8);
            overlayHighlight.setMouseTransparent(true);
            overlayHighlight.setBlendMode(BlendMode.ADD);
            Rectangle background = new Rectangle(-8,-8,130,130);
            background.setFill(Color.web("#b9c0c5"));
            Group group = new Group(background);
            Rectangle clipRect = new Rectangle(114,114);
            clipRect.setArcWidth(38);
            clipRect.setArcHeight(38);
            group.setClip(clipRect);
            group.getChildren().addAll(overlayHighlight,imageView);
            // Wrap in extra group as clip dosn't effect layout without it
            return new Group(group);
    }
}
