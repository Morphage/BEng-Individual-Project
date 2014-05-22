/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jscape.server.utils;

import bsh.EvalError;
import bsh.Interpreter;

/**
 *
 * @author achantreau
 */
public class BeanshellTest {

    public static void main(String[] args) {
        Interpreter i = new Interpreter();  // Construct an interpreter

        try {
            i.set("foo", 5);                    // Set variables
            i.set("b", 10);

            int b = (int) i.get("b");    // retrieve a variable
            
            //i.set("a", 6);
            //i.set("b", 4);
           // i.set("c", 20);
            //i.set("average", "(a+b+c)/5.0");

            i.eval("int a = 4;\n" +
"int b = 6;\n" +
"boolean c = true;\n" +
"\n" +
"if ((a==4) && c) {\n" +
"    b = 10;\n" +
"	a = 15;\n" +
"}\n" +
"\n" +
"if (a < b) {\n" +
"    c = false;\n" +
"} else {\n" +
"    a = 1337;\n" +
"}");
            System.out.println(i.get("a"));
            System.out.println(i.get("b"));
            System.out.println(i.get("c"));
            
        } catch (EvalError ee) {
        }
    }
}
