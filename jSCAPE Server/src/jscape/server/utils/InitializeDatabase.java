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
    private static final String CONDITIONALS_DESCRIPTION = "A binary tree is a tree data structure in which each"
            + " node has at most two children (referred to as the left child and the right child). In a binary"
            + " tree, the degree of each node can be at most two. Binary trees are used to implement binary search"
            + " trees and binary heaps, and are used for efficient searching and sorting.";
    private static final String CONDITIONALS_LECTURE_NOTES = "https://cate.doc.ic.ac.uk";
    private static final String CONDITIONALS_LINKS
            = "http://cslibrary.stanford.edu/110/BinaryTrees.html;"
            + "http://www.cs.cmu.edu/~adamchik/15-121/lectures/Trees/trees.html;"
            + "http://math.hws.edu/javanotes/c9/s4.html";

    public static void main(String[] args) {
        // Add exercise categories
        CategoryTable.addCategory("Arrays", ARRAYS_DESCRIPTION, ARRAYS_LECTURE_NOTES, ARRAYS_LINKS, true);
        CategoryTable.addCategory("Syntax", SYNTAX_DESCRIPTION, SYNTAX_LECTURE_NOTES, SYNTAX_LINKS, true);
        CategoryTable.addCategory("Binary Trees", BINARYTREES_DESCRIPTION, BINARYTREES_LECTURE_NOTES, BINARYTREES_LINKS, true);
        CategoryTable.addCategory("Conditionals", CONDITIONALS_DESCRIPTION, CONDITIONALS_LECTURE_NOTES, CONDITIONALS_LINKS, true);
        CategoryTable.addCategory("Loops", "loops", "loops", "loops", true);
        CategoryTable.addCategory("Objects", "objects", "objects", "objects", true);
        CategoryTable.addCategory("Strings", "strings", "strings", "strings", true);

        // Add students
        StudentTable.addStudent("ac6609", "Alexis", "Chantreau", "cs101", "ac6609");
        StudentTable.addStudent("user001", "user001", "user001", "cs101", "user001");
        StudentTable.addStudent("user002", "user002", "user002", "cs101", "user002");
        StudentTable.addStudent("user003", "user003", "user003", "cs101", "user003");
        StudentTable.addStudent("user004", "user004", "user004", "cs101", "user004");
    }

}
