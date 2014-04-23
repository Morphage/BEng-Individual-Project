/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jscape;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * @author achantreau
 */
class ProfilePane extends BorderPane {
        
    private Label firstName = new Label("Alexis");
    private Label secondName = new Label("CHANTREAU");
    private Label loginName = new Label("ac6609");
    private Label lastLogin = new Label("Last login: 03/05/14");
    private Label lastQuestionAnswered = new Label("Last question answered: 03/05/14");
    
    public ProfilePane() {
        super();
        
        VBox profileInfo = new VBox(50);
        //profileInfo.setSpacing(0);
        //profileInfo.setStyle("-fx-background-color: #336699;");
        profileInfo.setMinWidth(285);
        profileInfo.setPrefWidth(285);
        profileInfo.setAlignment(Pos.TOP_CENTER);
        
        firstName.getStyleClass().add("page-header");
        secondName.getStyleClass().add("page-header");
        loginName.getStyleClass().add("page-header");
        lastLogin.getStyleClass().add("page-header");
        lastQuestionAnswered.getStyleClass().add("page-header");
        
        //profileInfo.setId("page-tree");
        
        profileInfo.getStyleClass().add("category-page");
        profileInfo.getChildren().addAll(firstName, secondName, loginName, lastLogin, lastQuestionAnswered);
        
        // Create display area for profile statistics
        VBox profileStatistics = new VBox() {
            // stretch to allways fill height of scrollpane
            @Override protected double computePrefHeight(double width) {
                return Math.max(
                        super.computePrefHeight(width),
                        getParent().getBoundsInLocal().getHeight()
                );
            }
        };
        
        // Add STATISTICS title bar
        Label statistics = new Label("      STATISTICS");
        statistics.setMaxWidth(Double.MAX_VALUE);
        statistics.setMinHeight(Control.USE_PREF_SIZE);
        statistics.getStyleClass().add("category-header");        
        profileStatistics.getChildren().add(statistics);
        
        // Create grid
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));        
        
        ObservableList<String> subjects = 
                FXCollections.observableArrayList("Global", "Arrays", "Syntax");
        ComboBox subjectBox = new ComboBox(subjects);
        subjectBox.getSelectionModel().selectFirst();
        GridPane.setConstraints(subjectBox, 0, 0);
        
        Label questionsAnswered = new Label("Questions answered: 432");
        questionsAnswered.setTextFill(Color.web("#0076a3"));
        GridPane.setConstraints(questionsAnswered, 0, 1);
        
        Label correctAnswers = new Label("Correct answers: 269");
        correctAnswers.setTextFill(Color.web("#0076a3"));
        GridPane.setConstraints(correctAnswers, 0, 2);
        
        Label wrongAnswers = new Label("Wrong answers: 163");
        wrongAnswers.setTextFill(Color.web("#0076a3"));
        GridPane.setConstraints(wrongAnswers, 0, 3);
        
        // Create pie chart and set properties
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Correct", 269),
                        new PieChart.Data("Wrong", 163)
                );
        PieChart chart = new PieChart(pieChartData);
        chart.setLegendSide(Side.RIGHT);
        chart.setLabelsVisible(false);
        chart.setClockwise(true);
        chart.setScaleX(0.65);
        chart.setScaleY(0.65);
        
        GridPane.setConstraints(chart, 2, 1, 1, 3);
        
        grid.getStyleClass().add("category-page");        
        grid.setGridLinesVisible(true);
        grid.getChildren().addAll(subjectBox, questionsAnswered, correctAnswers,
                                  wrongAnswers, chart);
        
        profileStatistics.getChildren().add(grid);        
        
        // Add GRAPHS title bar
        Label graphs = new Label("      GRAPHS");
        graphs.setMaxWidth(Double.MAX_VALUE);
        graphs.setMinHeight(Control.USE_PREF_SIZE);
        graphs.getStyleClass().add("category-header");
        profileStatistics.getChildren().add(graphs);
                
        profileStatistics.getStyleClass().add("category-page");
        profileStatistics.setStyle("-fx-border-style: solid;"
                + "-fx-border-color: black;"
                + "-fx-border-width: 1;");

        
        /*
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("noborder-scroll-pane");
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(profileStatistics); */
        
        setLeft(profileInfo);
        setCenter(profileStatistics);
    }
    
}
