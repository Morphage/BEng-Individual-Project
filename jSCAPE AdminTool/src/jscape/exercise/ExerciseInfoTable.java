/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jscape.exercise;

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
public class ExerciseInfoTable extends TableView<ExerciseInfo> {

    private static final Font FONT = new Font("Arial", 16);

    private static final String COL1_HEADER = "Exercise ID";
    private static final String COL2_HEADER = "Correct Answers";
    private static final String COL3_HEADER = "Wrong Answers";
    private static final String COL4_HEADER = "Exercise Details";
    private static final String COL5_HEADER = "Difficulty Category";

    private static final double COL1_WIDTH = TextBuilder.create().text(COL1_HEADER)
            .font(FONT).build().getLayoutBounds().getWidth();
    private static final double COL2_WIDTH = TextBuilder.create().text(COL2_HEADER)
            .font(FONT).build().getLayoutBounds().getWidth();
    private static final double COL3_WIDTH = TextBuilder.create().text(COL3_HEADER)
            .font(FONT).build().getLayoutBounds().getWidth();
    private static final double COL4_WIDTH = TextBuilder.create().text(COL4_HEADER)
            .font(FONT).build().getLayoutBounds().getWidth();
    private static final double COL5_WIDTH = TextBuilder.create().text(COL5_HEADER)
            .font(FONT).build().getLayoutBounds().getWidth() + 50;

    public ExerciseInfoTable() {
        TableColumn exerciseIDCol = new TableColumn(COL1_HEADER);
        exerciseIDCol.setResizable(false);
        exerciseIDCol.setMinWidth(COL1_WIDTH);
        exerciseIDCol.setCellValueFactory(
                new PropertyValueFactory<ExerciseInfo, Integer>("exerciseID"));

        TableColumn correctAnswersCol = new TableColumn(COL2_HEADER);
        correctAnswersCol.setResizable(false);
        correctAnswersCol.setMinWidth(COL2_WIDTH);
        correctAnswersCol.setCellValueFactory(
                new PropertyValueFactory<ExerciseInfo, Integer>("correctAnswers"));

        TableColumn wrongAnswersCol = new TableColumn(COL3_HEADER);
        wrongAnswersCol.setResizable(false);
        wrongAnswersCol.setMinWidth(COL3_WIDTH);
        wrongAnswersCol.setCellValueFactory(
                new PropertyValueFactory<ExerciseInfo, Integer>("wrongAnswers"));
        
        TableColumn exerciseDetailsCol = new TableColumn(COL4_HEADER);
        exerciseDetailsCol.setResizable(false);
        exerciseDetailsCol.setMinWidth(COL4_WIDTH);
        exerciseDetailsCol.setCellValueFactory(
                new PropertyValueFactory<ExerciseInfo, String>("exerciseText"));
        
        TableColumn difficultyCol = new TableColumn(COL5_HEADER);
        difficultyCol.setResizable(false);
        difficultyCol.setMinWidth(COL5_WIDTH);
        difficultyCol.setCellValueFactory(
                new PropertyValueFactory<ExerciseInfo, String>("difficultyCategory"));

        getColumns().addAll(exerciseIDCol, correctAnswersCol, wrongAnswersCol, difficultyCol);
    }

    public void setItems(ArrayList<String> payload) {
        ArrayList<ExerciseInfo> exerciseData = new ArrayList<>();

        for (int i = 0; i < payload.size(); i += 5) {
            int exerciseID = Integer.valueOf(payload.get(i));
            int correct = Integer.valueOf(payload.get(i + 1));
            int wrong = Integer.valueOf(payload.get(i + 2));
            String exerciseText = payload.get(i+3);
            String difficulty = payload.get(i+4);
            
            exerciseData.add(new ExerciseInfo(exerciseID, correct, wrong, exerciseText, difficulty));
        }

        final ObservableList<ExerciseInfo> data = FXCollections.observableArrayList(exerciseData);

        setItems(data);
    }

}
