/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jscape;

import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.CategoryAxisBuilder;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.NumberAxisBuilder;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import jscape.performance.PerformancePieChart;
import jscape.performance.PerformanceStatsTable;

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
    
    private ComboBox subjectBox;
    
    public ProfilePane() {
        super();
        
        firstName = new Label();
        lastName = new Label();
        loginName = new Label();
        lastLogin = new Label();
        lastQuestionAnswered = new Label();
        
        BorderPane profileInfo = new BorderPane();
        profileInfo.setMinWidth(285);
        profileInfo.setMaxWidth(285);
        profileInfo.setPrefWidth(285);
        
        /*firstName.getStyleClass().add("page-header");
        lastName.getStyleClass().add("page-header");
        loginName.getStyleClass().add("page-header");
        lastLogin.getStyleClass().add("page-header");
        lastQuestionAnswered.getStyleClass().add("page-header");*/
        
        firstName.setFont(new Font("Arial", 20));
        firstName.setStyle("-fx-text-fill: black;"
                + "-fx-font-weight: bold;");
        
        profileInfo.setId("page-tree");
        
        VBox profileInfoTop = new VBox();
        profileInfoTop.getChildren().addAll(firstName, lastName, loginName);
        profileInfoTop.setAlignment(Pos.TOP_CENTER);
        
        VBox profileInfoBottom = new VBox();
        profileInfoBottom.getChildren().addAll(lastLogin, lastQuestionAnswered);
        
        profileInfo.setTop(profileInfoTop);
        profileInfo.setBottom(profileInfoBottom);
        
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
        
        // Create Performance Summary Label
        Label performanceSummary = new Label("Performance Summary");
        performanceSummary.setFont(new Font("Arial", 20));
        performanceSummary.setStyle("-fx-text-fill: #e1fdff;"
                + "-fx-font-weight: bold;");
        
        // Create Performance Statistics Table        
        ArrayList<String> payload = new ArrayList<String>();
        payload.add("Arrays");
        payload.add("37");
        payload.add("20");
        payload.add("17");
        payload.add("Syntax");
        payload.add("45");
        payload.add("28");
        payload.add("17");
        payload.add("Loops");
        payload.add("50");
        payload.add("45");
        payload.add("5");
        payload.add("Binary Trees");
        payload.add("10");
        payload.add("8");
        payload.add("2");
        
        PerformanceStatsTable performanceTable = new PerformanceStatsTable();        
        performanceTable.setItems(payload);
        
        // Combine performance label and performance table into a VBox
        VBox performanceVBox = new VBox();
        performanceVBox.getChildren().addAll(performanceSummary, performanceTable);
        
        // Create grid
        GridPane grid = new GridPane();
        grid.setHgap(2);
        grid.setVgap(2);
        grid.setPadding(new Insets(0, 10, 0, 10));
        ColumnConstraints col1 = new ColumnConstraints(150);
        ColumnConstraints col2 = new ColumnConstraints(180);
        ColumnConstraints col3 = new ColumnConstraints(180);
        grid.getColumnConstraints().addAll(col1,col2,col3);
                
        // Create choose label
        Label chooseCategoryLabel = new Label("Choose Exercise Category:");
        chooseCategoryLabel.setAlignment(Pos.TOP_CENTER);
        chooseCategoryLabel.setStyle("-fx-text-fill: #e1fdff;"
                + "-fx-font-weight: bold;");
        
        // Create performance pie chart
        final PerformancePieChart performancePieChart = new PerformancePieChart(performanceTable);

        // Create combo box for categories, i.e. arrays, loops, etc...
        ObservableList<String> categories = 
                FXCollections.observableArrayList("Arrays", "Syntax", "Loops", "Binary Trees",
                        "Total", "-- Total answers per category --");
        subjectBox = new ComboBox(categories);
        subjectBox.getSelectionModel().selectFirst();
        subjectBox.setMaxWidth(Double.MAX_VALUE);
        subjectBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                String selected = t1.toString();
                
                if ("-- Total answers per category --".equals(selected)) {
                    performancePieChart.setCategoryChart();
                } else {
                    performancePieChart.setData(t1.toString());
                }
            }
        });
        
        // Add initial data to performance pie chart
        performancePieChart.setData(subjectBox.getSelectionModel().getSelectedItem().toString());
        
        // Add elements to the grid and lay them out
        GridPane.setConstraints(chooseCategoryLabel, 0, 0);
        GridPane.setConstraints(subjectBox, 1, 0, 2, 1);
        GridPane.setConstraints(performancePieChart, 0, 1, 3, 1, HPos.CENTER, VPos.TOP);
        grid.getChildren().addAll(chooseCategoryLabel, subjectBox, performancePieChart);
        //grid.setGridLinesVisible(true);
        
        // Combine all statistics components into a HBox
        HBox statisticsHBox = new HBox(80);
        statisticsHBox.getChildren().addAll(grid, performanceVBox);  
        
        // Complete profile statistics component
        profileStatistics.getChildren().add(statisticsHBox);
        
        // Add GRAPHS title bar
        Label graphs = new Label("      GRAPHS");
        graphs.setMaxWidth(Double.MAX_VALUE);
        graphs.setMinHeight(Control.USE_PREF_SIZE);
        graphs.getStyleClass().add("category-header");
        profileStatistics.getChildren().add(graphs);
        
        // Create choose label
        Label chooseGraphLabel = new Label("Choose Graph Type:");
        chooseGraphLabel.setAlignment(Pos.TOP_CENTER);
        chooseGraphLabel.setStyle("-fx-text-fill: #e1fdff;"
                + "-fx-font-weight: bold;");
        
        // Create combo box for graph types
        ObservableList<String> graphTypes = 
                FXCollections.observableArrayList("This Month's history", "Skill curve", "Monthly average");
        ComboBox graphTypesBox = new ComboBox(graphTypes);
        graphTypesBox.getSelectionModel().selectFirst();
        graphTypesBox.setMaxWidth(Double.MAX_VALUE);
        
        // Combine the two and add to the scene
        HBox graphTypeHBox = new HBox(5);
        graphTypeHBox.getChildren().addAll(chooseGraphLabel, graphTypesBox);
        profileStatistics.getChildren().add(graphTypeHBox);
        
        // Add charts
        String[] days = {"07/05/14", "08/05/14", "09/05/14", "10/05/14", "11/05/14",
                         "12/05/14", "13/05/14", "14/05/14", "15/05/14", "16/05/14"};
        CategoryAxis xAxis = CategoryAxisBuilder.create()
                .categories(FXCollections.<String>observableArrayList(days)).build();
        xAxis.setLabel("Days");
        NumberAxis yAxis = NumberAxisBuilder.create()
                           .label("Questions Answered")
                           .lowerBound(0.0d)
                           .upperBound(100.0d)
                           .tickUnit(10.0d).build();
        ObservableList<StackedBarChart.Series> barChartData = FXCollections.observableArrayList(
            new StackedBarChart.Series("Wrong", FXCollections.observableArrayList(
               new StackedBarChart.Data(days[0], 8d),
               new StackedBarChart.Data(days[1], 5d),
               new StackedBarChart.Data(days[2], 3d),
               new StackedBarChart.Data(days[3], 4d),
               new StackedBarChart.Data(days[4], 5d),
               new StackedBarChart.Data(days[5], 8d),
               new StackedBarChart.Data(days[6], 5d),
               new StackedBarChart.Data(days[7], 3d),
               new StackedBarChart.Data(days[8], 4d),
               new StackedBarChart.Data(days[9], 3d)               
            )),
            new StackedBarChart.Series("Correct", FXCollections.observableArrayList(
               new StackedBarChart.Data(days[0], 12),
               new StackedBarChart.Data(days[1], 15),
               new StackedBarChart.Data(days[2], 14),
               new StackedBarChart.Data(days[3], 12),
               new StackedBarChart.Data(days[4], 29),
               new StackedBarChart.Data(days[5], 12),
               new StackedBarChart.Data(days[6], 15),
               new StackedBarChart.Data(days[7], 14),
               new StackedBarChart.Data(days[8], 12),
               new StackedBarChart.Data(days[9], 17)
            ))
        );
         
        StackedBarChart stackedBar = new StackedBarChart(xAxis, yAxis, barChartData, 100.0d);
        profileStatistics.getChildren().add(stackedBar);
        
        profileStatistics.getStyleClass().add("category-page");
        
        // Add scroll pane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("noborder-scroll-pane");
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(profileStatistics);
        
        setLeft(profileInfo);
        setCenter(scrollPane);
    }
    
    public void updateProfile(ArrayList<String> payload) {
        firstName.setText(payload.get(0));
        lastName.setText(payload.get(1));
        //loginName.setText(payload.get("loginName"));
        loginName.setText("ac6609");
        lastLogin.setText("Last login: " + payload.get(2));
        lastQuestionAnswered.setText("Last question answered: " + payload.get(3));
    }
}
