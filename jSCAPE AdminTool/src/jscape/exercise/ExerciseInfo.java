/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jscape.exercise;

/**
 *
 * @author achantreau
 */
public class ExerciseInfo {
    private int exerciseID;
    private String exerciseText;
    private int correctAnswers;
    private int wrongAnswers;
    private String difficultyCategory;
    
    public ExerciseInfo(int exerciseID, int correctAnswers, int wrongAnswers, 
            String exerciseText, String difficultyCategory) {
        this.exerciseID = exerciseID;
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = wrongAnswers;
        this.exerciseText = exerciseText;
        this.difficultyCategory = difficultyCategory;
    }

    public int getExerciseID() {
        return exerciseID;
    }

    public String getExerciseText() {
        return exerciseText;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public int getWrongAnswers() {
        return wrongAnswers;
    }

    public String getDifficultyCategory() {
        return difficultyCategory;
    }
}
