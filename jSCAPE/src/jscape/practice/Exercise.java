/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jscape.practice;

/**
 *
 * @author achantreau
 */
public class Exercise {
    
    private int exerciseId;
    
    private String leftDisplayView;
    private String leftDisplayValue;
    
    private String rightDisplayView;
    private String rightDisplayValue;
    private String choice1;
    private String choice2;
    private String choice3;
    private String choice4;
    private String solution;
    
    public Exercise(int exerciseId, String ldv, String ldv2, String rdv, String rdv2, String c1,
            String c2, String c3, String c4, String s) {
        this.exerciseId = exerciseId;
        this.leftDisplayView = ldv;
        this.leftDisplayValue = ldv2;
        this.rightDisplayView = rdv;
        this.rightDisplayValue = rdv2;
        this.choice1 = c1;
        this.choice2 = c2;
        this.choice3 = c3;
        this.choice4 = c4;
        this.solution = s;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public String getLeftDisplayView() {
        return leftDisplayView;
    }

    public String getLeftDisplayValue() {
        return leftDisplayValue;
    }

    public String getRightDisplayView() {
        return rightDisplayView;
    }

    public String getRightDisplayValue() {
        return rightDisplayValue;
    }

    public String getChoice1() {
        return choice1;
    }

    public String getChoice2() {
        return choice2;
    }

    public String getChoice3() {
        return choice3;
    }

    public String getChoice4() {
        return choice4;
    }

    public String getSolution() {
        return solution;
    }        
}
