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
public class StringTest {

    public static void main(String[] args) {
        String s1 = "Mississippi";
        String s2 = s1;
        s1 = s1.replace('Q', '!');
        System.out.println(s1); // Prints "M!ss!ss!pp!"
        System.out.println(s2); // Prints "Mississippi"
        System.out.println(s1 == s2);
    }
}
