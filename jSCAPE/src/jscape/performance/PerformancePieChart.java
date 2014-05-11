/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jscape.performance;

import java.text.DecimalFormat;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

/**
 *
 * @author achantreau
 */
public class PerformancePieChart extends PieChart {
    
    private static final DecimalFormat df = new DecimalFormat("#.0"); 
    
    private PerformanceStatsTable performanceStatsTable;
    
    public PerformancePieChart(PerformanceStatsTable performanceStatsTable) {
        this.performanceStatsTable = performanceStatsTable;
        
        setLegendVisible(false);
        setLabelsVisible(true);
        setClockwise(false);
        setAnimated(true);
        setMaxWidth(Double.MAX_VALUE);
    }
    
    public void setData(String exerciseCategory) {
        PerformanceStats performanceStats = performanceStatsTable.search(exerciseCategory);
        
        int correct = performanceStats.getCorrectAnswers();
        int wrong = performanceStats.getWrongAnswers();
        int total = performanceStats.getQuestionsAnswered();
        
        double correctPercentage = 0;
        double wrongPercentage = 0;
        
        if (total != 0) {
            correctPercentage = ((double) correct) / total * 100;
            wrongPercentage = ((double) wrong) / total * 100;
        }
        
        ObservableList<PieChart.Data> pieChartData
                = FXCollections.observableArrayList(
                        new PieChart.Data("Correct (" + df.format(correctPercentage) + "%)", correct),
                        new PieChart.Data("Wrong (" + df.format(wrongPercentage) + "%)", wrong)
                );
        setData(pieChartData);
    }
    
    public void setCategoryChart() {
        ObservableList<PerformanceStats> tableData = performanceStatsTable.getItems();
        ArrayList<PieChart.Data> pieChartData = new ArrayList<PieChart.Data>();
        
        int totalAnswers = performanceStatsTable.search("Total").getQuestionsAnswered();
        
        for (PerformanceStats ps : tableData) {
            if (!"Total".equals(ps.getExerciseCategory())) {
                double percentage = ((double) ps.getQuestionsAnswered()) / totalAnswers * 100;
                String label = ps.getExerciseCategory() + "(" + df.format(percentage) + "%)";
                pieChartData.add(new PieChart.Data(label, ps.getQuestionsAnswered()));
            }
        }
        
        final ObservableList<PieChart.Data> data = 
                FXCollections.observableArrayList(pieChartData);
        setData(data);
    }
}
