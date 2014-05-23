/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jscape.server.utils;

import jscape.database.CategoryTable;
import jscape.database.StudentTable;

/**
 *
 * @author achantreau
 */
public class InitializeDatabase {

    // ARRAYS
    private static final String ARRAYS_DESCRIPTION = "An array is a data type that is meant to describe"
            + " a collection of elements (values or variables), each selected by one or more indices"
            + " that can be computed at runtime by the program.";
    private static final String ARRAYS_LECTURE_NOTES = "https://cate.doc.ic.ac.uk";
    private static final String ARRAYS_LINKS
            = "http://docs.oracle.com/javase/tutorial/java/nutsandbolts/arrays.html;"
            + "http://www.tutorialspoint.com/java/java_arrays.htm";

    // SYNTAX
    private static final String SYNTAX_DESCRIPTION = "The syntax of the Java programming language is the"
            + " set of rules defining how a Java program is written and interpreted. The syntax is mostly"
            + " derived from C and C++.";
    private static final String SYNTAX_LECTURE_NOTES = "https://cate.doc.ic.ac.uk";
    private static final String SYNTAX_LINKS
            = "http://docs.oracle.com/javase/tutorial/java/nutsandbolts/index.html;"
            + "htpp://intro.cs.princeton.edu/java/11cheatsheet/;"
            + "http://www.tutorialspoint.com/java/java_basic_syntax.htm";

    // BINARY TREES
    private static final String BINARYTREES_DESCRIPTION = "A binary tree is a tree data structure in which each"
            + " node has at most two children (referred to as the left child and the right child). In a binary"
            + " tree, the degree of each node can be at most two. Binary trees are used to implement binary search"
            + " trees and binary heaps, and are used for efficient searching and sorting.";
    private static final String BINARYTREES_LECTURE_NOTES = "https://cate.doc.ic.ac.uk";
    private static final String BINARYTREES_LINKS
            = "http://cslibrary.stanford.edu/110/BinaryTrees.html;"
            + "http://www.cs.cmu.edu/~adamchik/15-121/lectures/Trees/trees.html;"
            + "http://math.hws.edu/javanotes/c9/s4.html";

    // CONDITIONALS
    private static final String CONDITIONALS_DESCRIPTION = "The statements inside your source files are generally"
            + " executed from top to bottom, in the order that they appear. Control flow statements, however, break up"
            + " the flow of execution by employing decision making, looping, and branching, enabling your program to"
            + " conditionally execute particular blocks of code.";
    private static final String CONDITIONALS_LECTURE_NOTES = "https://cate.doc.ic.ac.uk";
    private static final String CONDITIONALS_LINKS
            = "http://docs.oracle.com/javase/tutorial/java/nutsandbolts/if.html;"
            + "http://www.tutorialspoint.com/java/java_decision_making.htm;"
            + "http://www.functionx.com/java/Lesson07.htm";
    
    // LOOPS
    private static final String LOOPS_DESCRIPTION = "A loop is a sequence of statements which is specified once but which"
            + " may be carried out several times in succession. The code \"inside\" the loop (the body of the loop) is obeyed"
            + " a specified number of times, or once for each of a collection of items, or until some condition is met, or indefinitely.";
    private static final String LOOPS_LECTURE_NOTES = "https://cate.doc.ic.ac.uk";
    private static final String LOOPS_LINKS
            = "http://docs.oracle.com/javase/tutorial/java/nutsandbolts/while.html;"
            + "http://docs.oracle.com/javase/tutorial/java/nutsandbolts/for.html;"
            + "http://www.tutorialspoint.com/java/java_loop_control.htm";
    
    // STRINGS
    private static final String STRINGS_DESCRIPTION = "Strings, which are widely used in Java programming, are a sequence of characters."
            + " In the Java programming language, strings are objects. The Java platform provides the String class, which includes methods"
            + " for examining individual characters of the sequence, for comparing strings, for searching strings, for extracting substrings,"
            + " and for creating a copy of a string with all characters translated to uppercase or to lowercase, etc...";
    private static final String STRINGS_LECTURE_NOTES = "https://cate.doc.ic.ac.uk";
    private static final String STRINGS_LINKS
            = "http://docs.oracle.com/javase/tutorial/java/data/strings.html;"
            + "http://docs.oracle.com/javase/7/docs/api/java/lang/String.html;"
            + "http://www.tutorialspoint.com/java/java_strings.htm";

    public static void main(String[] args) {
        // Add exercise categories
        CategoryTable.addCategory("Arrays", ARRAYS_DESCRIPTION, ARRAYS_LECTURE_NOTES, ARRAYS_LINKS, true);
        CategoryTable.addCategory("Syntax", SYNTAX_DESCRIPTION, SYNTAX_LECTURE_NOTES, SYNTAX_LINKS, true);
        CategoryTable.addCategory("Binary Trees", BINARYTREES_DESCRIPTION, BINARYTREES_LECTURE_NOTES, BINARYTREES_LINKS, true);
        CategoryTable.addCategory("Conditionals", CONDITIONALS_DESCRIPTION, CONDITIONALS_LECTURE_NOTES, CONDITIONALS_LINKS, true);
        CategoryTable.addCategory("Loops", LOOPS_DESCRIPTION, LOOPS_LECTURE_NOTES, LOOPS_LINKS, true);
        CategoryTable.addCategory("Objects", "objects", "objects", "objects", true);
        CategoryTable.addCategory("Strings", STRINGS_DESCRIPTION, STRINGS_LECTURE_NOTES, STRINGS_LINKS, true);

        // Add students
        StudentTable.addStudent("ac6609", "Alexis", "Chantreau", "cs101", "ac6609");
        StudentTable.addStudent("user001", "user001", "user001", "cs101", "user001");
        StudentTable.addStudent("user002", "user002", "user002", "cs101", "user002");
        StudentTable.addStudent("user003", "user003", "user003", "cs101", "user003");
        StudentTable.addStudent("user004", "user004", "user004", "cs101", "user004");
    }

}
