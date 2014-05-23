/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jscape.server.data;

/**
 *
 * @author achantreau
 */
public class ConditionalsExercise {

    public static void main(String[] args) {
        int var1 = 45;
        int var2 = 50;
        String var3 = "hello";
        boolean var4 = true;

        if (var4 && (var1 == 45)) {
            var1 = 75;
            var3 += " world";
        }
        
        System.out.println(var1);
        System.out.println(var3);
    }
}
