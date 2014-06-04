/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jscape.analyze.performance;

/**
 *
 * @author achantreau
 */
public class PerformanceStats {
    
    private String exerciseCategory;
    private int    exercisesAnswered;
    private int    correctAnswers;
    private int    wrongAnswers;
    
    public PerformanceStats(String exerciseCategory, int questionsAnswered, 
                            int correctAnswers, int wrongAnswers) {
        this.exerciseCategory = exerciseCategory;
        this.exercisesAnswered = questionsAnswered;
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = wrongAnswers;
    }
    
    public String getExerciseCategory() {
        return exerciseCategory;
    }

    public int getExercisesAnswered() {
        return exercisesAnswered;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public int getWrongAnswers() {
        return wrongAnswers;
    }

    public void setExerciseCategory(String exerciseCategory) {
        this.exerciseCategory = exerciseCategory;
    }

    public void setExercisesAnswered(int exercisesAnswered) {
        this.exercisesAnswered = exercisesAnswered;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public void setWrongAnswers(int wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }
    
    
}
