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
public class GlobalViewStats {

    private String studentName;
    private int    exercisesAnswered;
    private int    correctAnswers;
    private double correctPercentage;
    private int    wrongAnswers;
    private double wrongPercentage;

    public GlobalViewStats(String studentName, int exercisesAnswered, int correctAnswers, 
            double correctPercentage, int wrongAnswers, double wrongPercentage) {
        this.studentName = studentName;
        this.exercisesAnswered = exercisesAnswered;
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = wrongAnswers;
        this.correctPercentage = correctPercentage;
        this.wrongPercentage = wrongPercentage;
    }

    public String getStudentName() {
        return studentName;
    }

    public int getExercisesAnswered() {
        return exercisesAnswered;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public double getCorrectPercentage() {
        return correctPercentage;
    }

    public int getWrongAnswers() {
        return wrongAnswers;
    }

    public double getWrongPercentage() {
        return wrongPercentage;
    }
}
