/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jscape.analyze.performance;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.scene.text.TextBuilder;

/**
 *
 * @author achantreau
 */
public class GlobalViewStatsTable extends TableView<GlobalViewStats> {

    private static final Font FONT = new Font("Arial", 16);

    private static final String COL1_HEADER = "Student Name";
    private static final String COL2_HEADER = "Exercises Answered";
    private static final String COL3_HEADER = "Correct Answers";
    private static final String COL4_HEADER = "Correct Percentage";
    private static final String COL5_HEADER = "Wrong Answers";
    private static final String COL6_HEADER = "Wrong Percentage";

    private static final double COL1_WIDTH = TextBuilder.create().text(COL1_HEADER)
            .font(FONT).build().getLayoutBounds().getWidth();
    private static final double COL2_WIDTH = TextBuilder.create().text(COL2_HEADER)
            .font(FONT).build().getLayoutBounds().getWidth();
    private static final double COL3_WIDTH = TextBuilder.create().text(COL3_HEADER)
            .font(FONT).build().getLayoutBounds().getWidth();
    private static final double COL4_WIDTH = TextBuilder.create().text(COL4_HEADER)
            .font(FONT).build().getLayoutBounds().getWidth();
    private static final double COL5_WIDTH = TextBuilder.create().text(COL5_HEADER)
            .font(FONT).build().getLayoutBounds().getWidth();
    private static final double COL6_WIDTH = TextBuilder.create().text(COL6_HEADER)
            .font(FONT).build().getLayoutBounds().getWidth();

    public GlobalViewStatsTable() {
        TableColumn studentNameCol = new TableColumn(COL1_HEADER);
        studentNameCol.setResizable(false);
        studentNameCol.setMinWidth(COL1_WIDTH);
        studentNameCol.setCellValueFactory(
                new PropertyValueFactory<GlobalViewStats, String>("studentName"));

        TableColumn questionsAnsweredCol = new TableColumn(COL2_HEADER);
        questionsAnsweredCol.setResizable(false);
        questionsAnsweredCol.setMinWidth(COL2_WIDTH);
        questionsAnsweredCol.setCellValueFactory(
                new PropertyValueFactory<GlobalViewStats, Integer>("exercisesAnswered"));

        TableColumn correctAnswersCol = new TableColumn(COL3_HEADER);
        correctAnswersCol.setResizable(false);
        correctAnswersCol.setMinWidth(COL3_WIDTH);
        correctAnswersCol.setCellValueFactory(
                new PropertyValueFactory<GlobalViewStats, Integer>("correctAnswers"));

        TableColumn correctPercentageCol = new TableColumn(COL4_HEADER);
        correctPercentageCol.setResizable(false);
        correctPercentageCol.setMinWidth(COL4_WIDTH);
        correctPercentageCol.setCellValueFactory(
                new PropertyValueFactory<GlobalViewStats, Double>("correctPercentage"));

        TableColumn wrongAnswersCol = new TableColumn(COL5_HEADER);
        wrongAnswersCol.setResizable(false);
        wrongAnswersCol.setMinWidth(COL5_WIDTH);
        wrongAnswersCol.setCellValueFactory(
                new PropertyValueFactory<GlobalViewStats, Integer>("wrongAnswers"));

        TableColumn wrongPercentageCol = new TableColumn(COL6_HEADER);
        wrongPercentageCol.setResizable(false);
        wrongPercentageCol.setMinWidth(COL6_WIDTH);
        wrongPercentageCol.setCellValueFactory(
                new PropertyValueFactory<GlobalViewStats, Double>("wrongPercentage"));

        getColumns().addAll(studentNameCol, questionsAnsweredCol, correctAnswersCol,
                correctPercentageCol, wrongAnswersCol, wrongPercentageCol);
    }

    public void setItems(ArrayList<String> payload) {
        ArrayList<GlobalViewStats> globalStatsData = new ArrayList<>();

        for (int i = 0; i < payload.size(); i += 6) {
            String loginName = payload.get(i);
            int answers = Integer.valueOf(payload.get(i + 1));
            int correct = Integer.valueOf(payload.get(i + 2));
            double correctPercentage = Double.valueOf(payload.get(i + 3));
            int wrong = Integer.valueOf(payload.get(i + 4));
            double wrongPercentage = Double.valueOf(payload.get(i + 5));

            globalStatsData.add(new GlobalViewStats(loginName, answers, correct,
                    correctPercentage, wrong, wrongPercentage));
        }

        final ObservableList<GlobalViewStats> data = FXCollections.observableArrayList(globalStatsData);

        setItems(data);
    }
}
