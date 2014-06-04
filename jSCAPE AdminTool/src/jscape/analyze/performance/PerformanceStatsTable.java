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
public class PerformanceStatsTable extends TableView<PerformanceStats> {

    private static final Font FONT = new Font("Arial", 16);

    private static final String COL1_HEADER = "Exercise Category";
    private static final String COL2_HEADER = "Exercises Answered";
    private static final String COL3_HEADER = "Correct Answers";
    private static final String COL4_HEADER = "Wrong Answers";

    private static final double COL1_WIDTH = TextBuilder.create().text(COL1_HEADER)
            .font(FONT).build().getLayoutBounds().getWidth();
    private static final double COL2_WIDTH = TextBuilder.create().text(COL2_HEADER)
            .font(FONT).build().getLayoutBounds().getWidth();
    private static final double COL3_WIDTH = TextBuilder.create().text(COL3_HEADER)
            .font(FONT).build().getLayoutBounds().getWidth();
    private static final double COL4_WIDTH = TextBuilder.create().text(COL4_HEADER)
            .font(FONT).build().getLayoutBounds().getWidth();

    public PerformanceStatsTable() {
        TableColumn exerciseCategoryCol = new TableColumn(COL1_HEADER);
        exerciseCategoryCol.setResizable(false);
        exerciseCategoryCol.setMinWidth(COL1_WIDTH);
        exerciseCategoryCol.setCellValueFactory(
                new PropertyValueFactory<PerformanceStats, String>("exerciseCategory"));

        TableColumn questionsAnsweredCol = new TableColumn(COL2_HEADER);
        questionsAnsweredCol.setResizable(false);
        questionsAnsweredCol.setMinWidth(COL2_WIDTH);
        questionsAnsweredCol.setCellValueFactory(
                new PropertyValueFactory<PerformanceStats, Integer>("exercisesAnswered"));

        TableColumn correctAnswersCol = new TableColumn(COL3_HEADER);
        correctAnswersCol.setResizable(false);
        correctAnswersCol.setMinWidth(COL3_WIDTH);
        correctAnswersCol.setCellValueFactory(
                new PropertyValueFactory<PerformanceStats, Integer>("correctAnswers"));

        TableColumn wrongAnswersCol = new TableColumn(COL4_HEADER);
        wrongAnswersCol.setResizable(false);
        wrongAnswersCol.setMinWidth(COL4_WIDTH);
        wrongAnswersCol.setCellValueFactory(
                new PropertyValueFactory<PerformanceStats, Integer>("wrongAnswers"));

        getColumns().addAll(exerciseCategoryCol, questionsAnsweredCol,
                correctAnswersCol, wrongAnswersCol);
    }

    public void setItems(ArrayList<String> payload) {
        ArrayList<PerformanceStats> performanceStatsData = new ArrayList<PerformanceStats>();
        int sumAnswers = 0;
        int sumCorrect = 0;
        int sumWrong = 0;

        for (int i = 0; i < payload.size(); i += 4) {
            int answers = Integer.valueOf(payload.get(i + 1));
            int correct = Integer.valueOf(payload.get(i + 2));
            int wrong = Integer.valueOf(payload.get(i + 3));

            performanceStatsData.add(new PerformanceStats(payload.get(i), answers, correct, wrong));

            sumAnswers += answers;
            sumCorrect += correct;
            sumWrong += wrong;
        }
        
        performanceStatsData.add(new PerformanceStats("Total", sumAnswers, sumCorrect, sumWrong));
        final ObservableList<PerformanceStats> data = FXCollections.observableArrayList(performanceStatsData);

        setItems(data);
    }

    public PerformanceStats search(String exerciseCategory) {
        PerformanceStats found = null;

        for (PerformanceStats ps : getItems()) {
            if (exerciseCategory.equals(ps.getExerciseCategory())) {
                found = ps;
                break;
            }
        }

        return found;
    }

    public ObservableList<String> getExerciseCategories() {
        ArrayList<String> exerciseCategories = new ArrayList<String>();

        for (PerformanceStats ps : getItems()) {
            exerciseCategories.add(ps.getExerciseCategory());
        }

        return FXCollections.observableArrayList(exerciseCategories);
    }

}
