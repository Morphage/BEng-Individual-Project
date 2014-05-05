/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jscape;

import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * @author achantreau
 */
class ProfilePane extends BorderPane {
        
    private Label firstName;
    private Label lastName;
    private Label loginName;
    private Label lastLogin;
    private Label lastQuestionAnswered;
    
    ComboBox subjectBox;
    
    public ProfilePane() {
        super();
        
        firstName = new Label();
        lastName = new Label();
        loginName = new Label();
        lastLogin = new Label();
        lastQuestionAnswered = new Label();
        
        VBox profileInfo = new VBox(50);
        //profileInfo.setSpacing(0);
        //profileInfo.setStyle("-fx-background-color: #336699;");
        profileInfo.setMinWidth(285);
        profileInfo.setPrefWidth(285);
        profileInfo.setAlignment(Pos.TOP_CENTER);
        
        firstName.getStyleClass().add("page-header");
        lastName.getStyleClass().add("page-header");
        loginName.getStyleClass().add("page-header");
        lastLogin.getStyleClass().add("page-header");
        lastQuestionAnswered.getStyleClass().add("page-header");
        
        //profileInfo.setId("page-tree");
        
        //profileInfo.getStyleClass().add("category-page");
        profileInfo.setStyle("fx-background-color: black");
        profileInfo.getChildren().addAll(firstName, lastName, loginName, lastLogin, lastQuestionAnswered);
        
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
        ColumnConstraints col1 = new ColumnConstraints(180);
        ColumnConstraints col2 = new ColumnConstraints(180);
        ColumnConstraints col3 = new ColumnConstraints(180);
        grid.getColumnConstraints().addAll(col1,col2,col3);
        
        // Create combo box for subjects, i.e. arrays, loops, etc...
        ObservableList<String> subjects = 
                FXCollections.observableArrayList("Global", "Arrays", "Syntax");
        subjectBox = new ComboBox(subjects);
        subjectBox.getSelectionModel().selectFirst();
        subjectBox.setMaxWidth(Double.MAX_VALUE);
        GridPane.setConstraints(subjectBox, 0, 0, 3, 1);
        
        // Questions answered label
        Label questionsAnswered = new Label("Questions answered: 432");
        questionsAnswered.setTextFill(Color.web("#0076a3"));
        GridPane.setConstraints(questionsAnswered, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
        
        Label correctAnswers = new Label("Correct answers: 269");
        correctAnswers.setTextFill(Color.web("#0076a3"));
        GridPane.setConstraints(correctAnswers, 1, 1, 1, 1, HPos.CENTER, VPos.CENTER);
        
        Label wrongAnswers = new Label("Wrong answers: 163");
        wrongAnswers.setTextFill(Color.web("#0076a3"));
        GridPane.setConstraints(wrongAnswers, 2, 1, 1, 1, HPos.CENTER, VPos.CENTER);
        
        // Create pie chart and set properties
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Correct", 269),
                        new PieChart.Data("Wrong", 163)
                );
        PieChart chart = new PieChart(pieChartData);
        chart.setLegendSide(Side.BOTTOM);
        chart.setLabelsVisible(false);
        chart.setClockwise(true);
        chart.setScaleX(0.8);
        chart.setScaleY(0.8);
        chart.setMaxWidth(Double.MAX_VALUE);
        GridPane.setConstraints(chart, 0, 2, 3, 1, HPos.CENTER, VPos.TOP);
        
        grid.getStyleClass().add("category-page");
        //grid.setGridLinesVisible(true);
        grid.getChildren().addAll(subjectBox, questionsAnswered, correctAnswers,
                                  wrongAnswers, chart);
        
        profileStatistics.getChildren().add(grid);
        
        // Add GRAPHS title bar
        Label graphs = new Label("      GRAPHS");
        graphs.setMaxWidth(Double.MAX_VALUE);
        graphs.setMinHeight(Control.USE_PREF_SIZE);
        graphs.getStyleClass().add("category-header");
        profileStatistics.getChildren().add(graphs);
        
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Month");
        //creating the chart
        final LineChart<Number,Number> lineChart = 
                new LineChart<Number,Number>(xAxis,yAxis);
                
        lineChart.setTitle("Stock Monitoring, 2010");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("My portfolio");
        //populating the series with data
        series.getData().add(new XYChart.Data(1, 23));
        series.getData().add(new XYChart.Data(2, 14));
        series.getData().add(new XYChart.Data(3, 15));
        series.getData().add(new XYChart.Data(4, 24));
        series.getData().add(new XYChart.Data(5, 34));
        series.getData().add(new XYChart.Data(6, 36));
        series.getData().add(new XYChart.Data(7, 22));
        series.getData().add(new XYChart.Data(8, 45));
        series.getData().add(new XYChart.Data(9, 43));
        series.getData().add(new XYChart.Data(10, 17));
        series.getData().add(new XYChart.Data(11, 29));
        series.getData().add(new XYChart.Data(12, 25));
        
        lineChart.getData().add(series);
        
        profileStatistics.getChildren().add(lineChart);
        
        profileStatistics.getStyleClass().add("category-page");
        /*profileStatistics.setStyle("-fx-border-style: solid;"
                + "-fx-border-color: black;"
                + "-fx-border-width: 1;");*/

        
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("noborder-scroll-pane");
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(profileStatistics);
        
        setLeft(profileInfo);
        setCenter(scrollPane);
    }
    
    public void updateProfile(HashMap<String,String> payload) {
        firstName.setText(payload.get("firstName"));
        lastName.setText(payload.get("lastName"));
        //loginName.setText(payload.get("loginName"));
        loginName.setText("ac6609");
        lastLogin.setText(payload.get("lastLogin"));
        lastQuestionAnswered.setText(payload.get("lastQuestionAnswered"));
    }
}
