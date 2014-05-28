package jscape;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.DepthTest;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jscape.profile.AnalyzePane;

/**
 *
 * @author achantreau
 */
public class JSCAPEAdminTool extends Application {

    private BorderPane rootPane;

    private StackPane modalDimmer;

    private TabPane tabPane;

    private AnalyzePane analyzePane;

    public boolean runPerformanceStatsService = false;

    @Override
    public void start(Stage primaryStage) {
        StackPane layerPane = new StackPane();

        rootPane = new BorderPane();
        rootPane.setId("Window");
        layerPane.setDepthTest(DepthTest.DISABLE);
        layerPane.getChildren().add(rootPane);

        final Scene scene = new Scene(layerPane, 1020, 700);
        scene.getStylesheets().add(JSCAPEAdminTool.class.getResource("jscape.css").toExternalForm());

        // create modal dimmer, to dim screen when showing modal dialogs
        modalDimmer = new StackPane();
        modalDimmer.setId("ModalDimmer");
        modalDimmer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                t.consume();
            }
        });
        modalDimmer.setVisible(false);
        layerPane.getChildren().add(modalDimmer);

        analyzePane = new AnalyzePane();

        tabPane = new TabPane();
        tabPane.setId("MainTabs");

        Tab analyzeTab = new Tab("ANALYZE");
        analyzeTab.setContent(analyzePane);

        Tab exercisesTab = new Tab("EXERCISES");
        exercisesTab.setContent(null);

        tabPane.getTabs().addAll(analyzeTab, exercisesTab);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        this.rootPane.setCenter(tabPane);

        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
