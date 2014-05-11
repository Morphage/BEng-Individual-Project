/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jscape.performance;

/**
 *
 * @author achantreau
 */
public class PerformanceStats {
    
    private String exerciseCategory;
    private int    questionsAnswered;
    private int    correctAnswers;
    private int    wrongAnswers;
    
    public PerformanceStats(String exerciseCategory, int questionsAnswered, 
                            int correctAnswers, int wrongAnswers) {
        this.exerciseCategory = exerciseCategory;
        this.questionsAnswered = questionsAnswered;
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = wrongAnswers;
    }
    
    public String getExerciseCategory() {
        return exerciseCategory;
    }

    public int getQuestionsAnswered() {
        return questionsAnswered;
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

    public void setQuestionsAnswered(int questionsAnswered) {
        this.questionsAnswered = questionsAnswered;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public void setWrongAnswers(int wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }
    
    
}
