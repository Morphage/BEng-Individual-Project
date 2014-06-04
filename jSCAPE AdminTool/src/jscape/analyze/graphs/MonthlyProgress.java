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
public class MonthlyProgress extends StackedBarChart {

    private CategoryAxis xAxis;
    private NumberAxis yAxis;

    private int[] numberOfDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public MonthlyProgress(CategoryAxis xAxis, NumberAxis yAxis) {
        super(xAxis, yAxis);

        this.xAxis = xAxis;
        this.yAxis = yAxis;

        setCategoryGap(10d);
        setLegendVisible(true);
        setAnimated(false);
    }

    public void setData(ArrayList<String> payload, String monthAndYear) {
        String[] days = getDaysArray(monthAndYear);
        xAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(days)));

        XYChart.Series<String, Number> correctSeries = new XYChart.Series<String, Number>();
        XYChart.Series<String, Number> wrongSeries = new XYChart.Series<String, Number>();
        correctSeries.setName("Correct");
        wrongSeries.setName("Wrong");

        if (!payload.isEmpty()) {
            for (int i = 0; i < payload.size(); i += 3) {
                String date = payload.get(i);
                int correct = Integer.valueOf(payload.get(i + 1));
                int wrong = Integer.valueOf(payload.get(i + 2));

                correctSeries.getData().add(new XYChart.Data<String, Number>(date, correct));
                wrongSeries.getData().add(new XYChart.Data<String, Number>(date, wrong));
            }

            yAxis.setAutoRanging(true);
            getData().setAll(correctSeries, wrongSeries);
            setLegendVisible(true);
        } else {
            yAxis.setAutoRanging(false);
            XYChart.Series<String, Number> zeroSeries = new XYChart.Series<String, Number>();
            XYChart.Series<String, Number> zeroSeries2 = new XYChart.Series<String, Number>();
            zeroSeries.getData().add(new XYChart.Data<String, Number>(days[15], 0));
            zeroSeries2.getData().add(new XYChart.Data<String, Number>(days[15], 0));
            setLegendVisible(false);
            getData().setAll(zeroSeries, zeroSeries2);
        }
    }

    private String[] getDaysArray(String monthAndYear) {
        String[] words = monthAndYear.split(" ");
        int month = monthToInt(words[0]);
        int numberOfDaysInMonth = numberOfDays[month - 1];
        int year = Integer.valueOf(words[1]) - 2000;

        String[] daysArray = new String[numberOfDaysInMonth];

        for (int i = 1; i <= numberOfDaysInMonth; i++) {
            if (month < 10) {
                if (i < 10) {
                    daysArray[i - 1] = "0" + i + "/0" + month + "/" + year;
                } else {
                    daysArray[i - 1] = i + "/0" + month + "/" + year;
                }
            } else {
                if (i < 10) {
                    daysArray[i - 1] = "0" + i + "/" + month + "/" + year;
                } else {
                    daysArray[i - 1] = i + "/" + month + "/" + year;
                }
            }
        }

        return daysArray;
    }
    
    private int monthToInt(String month) {
        int converted = 0;

        if ("January".equals(month)) {
            converted = 1;
        } else if ("February".equals(month)) {
            converted = 2;
        } else if ("March".equals(month)) {
            converted = 3;
        } else if ("April".equals(month)) {
            converted = 4;
        } else if ("May".equals(month)) {
            converted = 5;
        } else if ("June".equals(month)) {
            converted = 6;
        } else if ("July".equals(month)) {
            converted = 7;
        } else if ("August".equals(month)) {
            converted = 8;
        } else if ("September".equals(month)) {
            converted = 9;
        } else if ("October".equals(month)) {
            converted = 10;
        } else if ("November".equals(month)) {
            converted = 11;
        } else if ("December".equals(month)) {
            converted = 12;
        }

        return converted;
    }
}
