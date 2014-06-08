/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jscape.server.utils;

import jscape.database.CategoryTable;
import jscape.database.HistoryTable;
import jscape.database.PerformanceTable;
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
            + " In the Java programming language, strings are objects.\nThe Java platform provides the String class, which includes methods"
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
        StudentTable.addStudent("user1", "user1", "user1", "cs101", "user1");
        StudentTable.addStudent("user2", "user2", "user2", "cs101", "user2");
        StudentTable.addStudent("user3", "user3", "user3", "cs101", "user3");
        StudentTable.addStudent("user4", "user4", "user4", "cs101", "user4");
        
        // Add history data
        
        /* CREATE DEMO STUDENT WITH LOTS OF DATA */
        initializeDemoStudent("demo001");
        initializeDemoStudent("demo002");
        initializeDemoStudent("user5");
        initializeDemoStudent("user6");
        initializeDemoStudent("user7");
        initializeDemoStudent("user8");
        
    }
    
    private static void initializeDemoStudent(String loginName) {
        StudentTable.addStudent(loginName, "Michael", "Hunt", "cs101", loginName);
        
        PerformanceTable.addPerformanceData(loginName, "Arrays", 250, 150, 100);
        PerformanceTable.addPerformanceData(loginName, "Conditionals", 285, 175, 110);
        PerformanceTable.addPerformanceData(loginName, "Loops", 100, 65, 35);
        PerformanceTable.addPerformanceData(loginName, "Strings", 280, 160, 120);
        PerformanceTable.addPerformanceData(loginName, "Binary Trees", 334, 212, 122);
        PerformanceTable.addPerformanceData(loginName, "Syntax", 334, 212, 122);
        PerformanceTable.addPerformanceData(loginName, "Objects", 85, 32, 53);
        
        HistoryTable.addHistoryData(loginName, "2013-11-01", "Binary Trees", 10, 3, 7);
        HistoryTable.addHistoryData(loginName, "2013-11-02", "Binary Trees", 10, 2, 8);
        HistoryTable.addHistoryData(loginName, "2013-11-03", "Binary Trees", 8, 4, 4);
        HistoryTable.addHistoryData(loginName, "2013-11-06", "Binary Trees", 7, 3, 4);
        HistoryTable.addHistoryData(loginName, "2013-11-08", "Binary Trees", 12, 7, 5);
        HistoryTable.addHistoryData(loginName, "2013-11-11", "Binary Trees", 15, 7, 8);
        HistoryTable.addHistoryData(loginName, "2013-11-17", "Binary Trees", 13, 5, 8);
        HistoryTable.addHistoryData(loginName, "2013-11-19", "Binary Trees", 10, 6, 4);
        HistoryTable.addHistoryData(loginName, "2013-11-25", "Binary Trees", 8, 6, 2);
        
        HistoryTable.addHistoryData(loginName, "2014-04-01", "Binary Trees", 10, 2, 8);
        HistoryTable.addHistoryData(loginName, "2014-04-02", "Binary Trees", 14, 4, 10);
        HistoryTable.addHistoryData(loginName, "2014-04-05", "Binary Trees", 15, 8, 7);
        HistoryTable.addHistoryData(loginName, "2014-04-06", "Binary Trees", 13, 6, 7);
        HistoryTable.addHistoryData(loginName, "2014-04-07", "Binary Trees", 18, 10, 8);
        HistoryTable.addHistoryData(loginName, "2014-04-11", "Binary Trees", 11, 9, 2);
        HistoryTable.addHistoryData(loginName, "2014-04-12", "Binary Trees", 20, 12, 8);
        HistoryTable.addHistoryData(loginName, "2014-04-13", "Binary Trees", 13, 7, 6);
        HistoryTable.addHistoryData(loginName, "2014-04-14", "Binary Trees", 5, 4, 1);
        HistoryTable.addHistoryData(loginName, "2014-04-15", "Binary Trees", 10, 6, 4);
        HistoryTable.addHistoryData(loginName, "2014-04-16", "Binary Trees", 4, 3, 1);
        HistoryTable.addHistoryData(loginName, "2014-04-19", "Binary Trees", 6, 3, 3);
        HistoryTable.addHistoryData(loginName, "2014-04-20", "Binary Trees", 7, 4, 3);
        HistoryTable.addHistoryData(loginName, "2014-04-21", "Binary Trees", 3, 3, 0);
        HistoryTable.addHistoryData(loginName, "2014-04-22", "Binary Trees", 8, 6, 2);
        HistoryTable.addHistoryData(loginName, "2014-04-23", "Binary Trees", 13, 7, 6);
        HistoryTable.addHistoryData(loginName, "2014-04-24", "Binary Trees", 10, 5, 5);
        HistoryTable.addHistoryData(loginName, "2014-04-25", "Binary Trees", 15, 9, 6);
        HistoryTable.addHistoryData(loginName, "2014-04-26", "Binary Trees", 14, 11, 3);
        HistoryTable.addHistoryData(loginName, "2014-04-27", "Binary Trees", 13, 10, 3);
        HistoryTable.addHistoryData(loginName, "2014-04-28", "Binary Trees", 9, 8, 1);
        HistoryTable.addHistoryData(loginName, "2014-04-29", "Binary Trees", 7, 6, 1);
        HistoryTable.addHistoryData(loginName, "2014-04-30", "Binary Trees", 10, 10, 0);
        
        HistoryTable.addHistoryData(loginName, "2014-05-01", "Binary Trees", 10, 3, 7);
        HistoryTable.addHistoryData(loginName, "2014-05-02", "Binary Trees", 10, 2, 8);
        HistoryTable.addHistoryData(loginName, "2014-05-03", "Binary Trees", 8, 4, 4);
        HistoryTable.addHistoryData(loginName, "2014-05-04", "Binary Trees", 12, 5, 7);
        HistoryTable.addHistoryData(loginName, "2014-05-05", "Binary Trees", 15, 8, 7);
        HistoryTable.addHistoryData(loginName, "2014-05-06", "Binary Trees", 13, 6, 7);
        HistoryTable.addHistoryData(loginName, "2014-05-07", "Binary Trees", 18, 10, 8);
        HistoryTable.addHistoryData(loginName, "2014-05-08", "Binary Trees", 12, 7, 5);
        HistoryTable.addHistoryData(loginName, "2014-05-09", "Binary Trees", 14, 8, 6);
        HistoryTable.addHistoryData(loginName, "2014-05-10", "Binary Trees", 9, 6, 3);
        HistoryTable.addHistoryData(loginName, "2014-05-11", "Binary Trees", 11, 9, 2);
        HistoryTable.addHistoryData(loginName, "2014-05-12", "Binary Trees", 20, 12, 8);
        HistoryTable.addHistoryData(loginName, "2014-05-13", "Binary Trees", 13, 7, 6);
        HistoryTable.addHistoryData(loginName, "2014-05-14", "Binary Trees", 5, 4, 1);
        HistoryTable.addHistoryData(loginName, "2014-05-15", "Binary Trees", 10, 6, 4);
        HistoryTable.addHistoryData(loginName, "2014-05-16", "Binary Trees", 4, 3, 1);
        HistoryTable.addHistoryData(loginName, "2014-05-17", "Binary Trees", 12, 10, 2);
        HistoryTable.addHistoryData(loginName, "2014-05-18", "Binary Trees", 15, 13, 2);
        HistoryTable.addHistoryData(loginName, "2014-05-19", "Binary Trees", 6, 3, 3);
        HistoryTable.addHistoryData(loginName, "2014-05-20", "Binary Trees", 7, 4, 3);
        HistoryTable.addHistoryData(loginName, "2014-05-21", "Binary Trees", 3, 3, 0);
        HistoryTable.addHistoryData(loginName, "2014-05-22", "Binary Trees", 8, 6, 2);
        HistoryTable.addHistoryData(loginName, "2014-05-23", "Binary Trees", 13, 7, 6);
        HistoryTable.addHistoryData(loginName, "2014-05-24", "Binary Trees", 10, 5, 5);
        HistoryTable.addHistoryData(loginName, "2014-05-25", "Binary Trees", 15, 9, 6);
        HistoryTable.addHistoryData(loginName, "2014-05-26", "Binary Trees", 14, 11, 3);
        HistoryTable.addHistoryData(loginName, "2014-05-27", "Binary Trees", 13, 10, 3);
        HistoryTable.addHistoryData(loginName, "2014-05-28", "Binary Trees", 9, 8, 1);
        HistoryTable.addHistoryData(loginName, "2014-05-29", "Binary Trees", 7, 6, 1);
        HistoryTable.addHistoryData(loginName, "2014-05-30", "Binary Trees", 10, 10, 0);
        HistoryTable.addHistoryData(loginName, "2014-05-31", "Binary Trees", 8, 7, 1);
        
        HistoryTable.addHistoryData(loginName, "2014-05-01", "Syntax", 10, 3, 7);
        HistoryTable.addHistoryData(loginName, "2014-05-02", "Syntax", 10, 2, 8);
        HistoryTable.addHistoryData(loginName, "2014-05-03", "Syntax", 8, 4, 4);
        HistoryTable.addHistoryData(loginName, "2014-05-04", "Syntax", 12, 5, 7);
        HistoryTable.addHistoryData(loginName, "2014-05-05", "Syntax", 15, 8, 7);
        HistoryTable.addHistoryData(loginName, "2014-05-06", "Syntax", 13, 6, 7);
        HistoryTable.addHistoryData(loginName, "2014-05-07", "Syntax", 18, 10, 8);
        HistoryTable.addHistoryData(loginName, "2014-05-08", "Syntax", 12, 7, 5);
        HistoryTable.addHistoryData(loginName, "2014-05-09", "Syntax", 14, 8, 6);
        HistoryTable.addHistoryData(loginName, "2014-05-10", "Syntax", 9, 6, 3);
        HistoryTable.addHistoryData(loginName, "2014-05-11", "Syntax", 11, 9, 2);
        HistoryTable.addHistoryData(loginName, "2014-05-12", "Syntax", 20, 12, 8);
        HistoryTable.addHistoryData(loginName, "2014-05-13", "Syntax", 13, 7, 6);
        HistoryTable.addHistoryData(loginName, "2014-05-14", "Syntax", 5, 4, 1);
        HistoryTable.addHistoryData(loginName, "2014-05-15", "Syntax", 10, 6, 4);
        HistoryTable.addHistoryData(loginName, "2014-05-16", "Syntax", 4, 3, 1);
        HistoryTable.addHistoryData(loginName, "2014-05-17", "Syntax", 12, 10, 2);
        HistoryTable.addHistoryData(loginName, "2014-05-18", "Syntax", 15, 13, 2);
        HistoryTable.addHistoryData(loginName, "2014-05-19", "Syntax", 6, 3, 3);
        HistoryTable.addHistoryData(loginName, "2014-05-20", "Syntax", 7, 4, 3);
        HistoryTable.addHistoryData(loginName, "2014-05-21", "Syntax", 3, 3, 0);
        HistoryTable.addHistoryData(loginName, "2014-05-22", "Syntax", 8, 6, 2);
        HistoryTable.addHistoryData(loginName, "2014-05-23", "Syntax", 13, 7, 6);
        HistoryTable.addHistoryData(loginName, "2014-05-24", "Syntax", 10, 5, 5);
        HistoryTable.addHistoryData(loginName, "2014-05-25", "Syntax", 15, 9, 6);
        HistoryTable.addHistoryData(loginName, "2014-05-26", "Syntax", 14, 11, 3);
        HistoryTable.addHistoryData(loginName, "2014-05-27", "Syntax", 13, 10, 3);
        HistoryTable.addHistoryData(loginName, "2014-05-28", "Syntax", 9, 8, 1);
        HistoryTable.addHistoryData(loginName, "2014-05-29", "Syntax", 7, 6, 1);
        HistoryTable.addHistoryData(loginName, "2014-05-30", "Syntax", 10, 10, 0);
        HistoryTable.addHistoryData(loginName, "2014-05-31", "Syntax", 8, 7, 1);
    }

}
