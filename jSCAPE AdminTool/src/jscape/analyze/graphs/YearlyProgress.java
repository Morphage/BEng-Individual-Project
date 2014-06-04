/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jscape.analyze.graphs;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.collections.FXCollections;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;

/**
 *
 * @author achantreau
 */
public class YearlyProgress extends StackedBarChart {

    private CategoryAxis xAxis;
    private NumberAxis yAxis;

    private int[] numberOfDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public YearlyProgress(CategoryAxis xAxis, NumberAxis yAxis) {
        super(xAxis, yAxis);

        this.xAxis = xAxis;
        this.yAxis = yAxis;

        setCategoryGap(70d);
        setLegendVisible(true);
        setAnimated(false);
    }

    public void setData(ArrayList<String> payload) {
        XYChart.Series<String, Number> correctSeries = new XYChart.Series<String, Number>();
        XYChart.Series<String, Number> wrongSeries = new XYChart.Series<String, Number>();
        correctSeries.setName("Correct");
        wrongSeries.setName("Wrong");

        if (!payload.isEmpty()) {
            for (int i = 0; i < payload.size(); i += 3) {
                String month = payload.get(i);
                int correct = Integer.valueOf(payload.get(i + 1));
                int wrong = Integer.valueOf(payload.get(i + 2));

                correctSeries.getData().add(new XYChart.Data<String, Number>(month, correct));
                wrongSeries.getData().add(new XYChart.Data<String, Number>(month, wrong));
            }

            yAxis.setAutoRanging(true);
            getData().setAll(correctSeries, wrongSeries);
            setLegendVisible(true);
        } else {
            yAxis.setAutoRanging(false);
            XYChart.Series<String, Number> zeroSeries = new XYChart.Series<String, Number>();
            XYChart.Series<String, Number> zeroSeries2 = new XYChart.Series<String, Number>();
            zeroSeries.getData().add(new XYChart.Data<String, Number>("May", 0));
            zeroSeries2.getData().add(new XYChart.Data<String, Number>("May", 0));
            setLegendVisible(false);
            getData().setAll(zeroSeries, zeroSeries2);
        }
    }
    
}
