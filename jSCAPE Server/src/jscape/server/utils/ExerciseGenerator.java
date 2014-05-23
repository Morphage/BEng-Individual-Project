/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jscape.server.utils;

import jscape.exercise.syntax.SyntaxExerciseGen;
import jscape.database.ExerciseBankTable;
import jscape.exercise.bst.BSTExerciseGen;
import jscape.exercise.conditionals.ConditionalsExerciseGen;

/**
 *
 * @author achantreau
 */
public class ExerciseGenerator {
    
    public static void main(String[] args) {
        SyntaxExerciseGen seg = new SyntaxExerciseGen();
        BSTExerciseGen beg = new BSTExerciseGen();
        ConditionalsExerciseGen ceg = new ConditionalsExerciseGen();
        
        for (int i = 0; i < 20; i++) {
            //System.out.println(beg.makeExercise());
            //System.out.println(seg.makeType1Exercise());
            System.out.println(ceg.makeExercise());
            //ExerciseBankTable.addExercise("Syntax", seg.makeType2Exercise());
            //ExerciseBankTable.addExercise("Syntax", seg.makeType1Exercise());
            //ExerciseBankTable.addExercise("Binary Trees", beg.makeExercise());
            //ExerciseBankTable.addExercise("Conditionals", ceg.makeExercise());
        }
        
    }
}
