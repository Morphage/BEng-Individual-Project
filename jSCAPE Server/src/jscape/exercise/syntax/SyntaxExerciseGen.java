/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jscape.exercise.syntax;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author achantreau
 */
public class SyntaxExerciseGen {

    private static final String encoding = "UTF-8";
    private static String pathToTemplate = "src\\jscape\\server\\data\\easy1.java.template";

    private static final String xmlExercise = "<?xml version=\"1.0\"?>\n"
            + "<exercise>\n"
            + "    <display>\n"
            + "        <view>CodeEditor</view>\n"
            + "        <value>";

    private List<String> lines = null;

    private String solutionText;
    private int solutionLine;

    private Random random = new Random();

    private List<Integer> linesUsed;

    public SyntaxExerciseGen() {
    }

    private void replaceLine(int line) {
        if (line == 0) {
            lines.set(line, "public class SyntaxExercise");
        } else if (line == 1) {
            int prob = random.nextInt(2);
            if (prob == 0) {
                lines.set(line, "    public static main(String[] args) {");
            } else {
                lines.set(line, "    public static void main(String** args) {");
            }
        } else if (line == 2) {
            int prob = random.nextInt(4);
            if (prob == 0) {
                lines.set(line, "        a = 3;");
                lines.set(6, "        double average = (b + c)/5.0;");
            } else if (prob == 1) {
                lines.set(line, "        int a == 3;");
                lines.set(6, "        double average = (c + b)/3.0;");
            } else if (prob == 2) {
                lines.set(line, "        int a = 3");
            } else if (prob == 3) {
                lines.set(line, "        int *a = 3;");
                lines.set(6, "        double average = (b + c)/4.0;");
            }
        } else if (line == 3) {
            int prob = random.nextInt(4);
            if (prob == 0) {
                lines.set(line, "        b = 4;");
                lines.set(6, "        double average = (a + c)/5.0;");
            } else if (prob == 1) {
                lines.set(line, "        int b == 3;");
                lines.set(6, "        double average = (a + c)/2.0;");
            } else if (prob == 2) {
                lines.set(line, "        int b = 3");
            } else if (prob == 3) {
                lines.set(line, "        int *b = &amp;a;");
                lines.set(6, "        double average = (a + c)/4.0;");
            }
        } else if (line == 4) {
            int prob = random.nextInt(4);
            if (prob == 0) {
                lines.set(line, "        c = 20;");
                lines.set(6, "        double average = (b + a)/5.0;");
            } else if (prob == 1) {
                lines.set(line, "        int c == 20;");
                lines.set(6, "        double average = (b + a)/2.0;");
            } else if (prob == 2) {
                lines.set(line, "        int c = 3");
            } else if (prob == 3) {
                lines.set(line, "        int *c = &amp;b;");
                lines.set(6, "        double average = (a + b)/4.0;");
            }
        } else if (line == 5) {
            int prob = random.nextInt(3);
            if (prob == 0) {
                lines.set(line, "        int a = 2 * c;");
            } else if (prob == 1) {
                lines.set(line, "        int b = 5 * a;");
            } else if (prob == 2) {
                lines.set(line, "        int c = 3 * b;");
            }
        } else if (line == 6) {
            int prob = random.nextInt(3);
            if (prob == 0) {
                lines.set(line, "        int average = (a + b + c)/5.0;");
            } else if (prob == 1) {
                lines.set(line, "        double average = (a + b + c)/5.0");
            } else if (prob == 2) {
                lines.set(line, "        long average = (a + b + c)/5.0;");
            }
        } else if (line == 7) {
            int prob = random.nextInt(7);
            if (prob == 0) {
                lines.set(line, "        System.println(average);");
            } else if (prob == 1) {
                lines.set(line, "        out.println(average);");
            } else if (prob == 2) {
                lines.set(line, "        System.out.println(average)");
            } else if (prob == 3) {
                lines.set(line, "        printf(average)");
            } else if (prob == 4) {
                lines.set(line, "        System.out.println(Average);");
            } else if (prob == 5) {
                lines.set(line, "        cout &lt;&lt; average;");
            } else if (prob == 6) {
                lines.set(line, "        return average;");
            }
        } else if (line == 8) {
            lines.set(line, "");
        } else if (line == 9) {
            lines.set(line, "");
        }

        solutionLine = line + 1;
        solutionText = "Line " + solutionLine;
        linesUsed.add(solutionLine);
    }

    public String makeType2Exercise() {
        try {
            lines = Files.readAllLines(Paths.get(pathToTemplate), Charset.forName(encoding));
        } catch (IOException ie) {
        }

        linesUsed = new ArrayList<>();

        int numberOfErrors = random.nextInt(5) + 1;
        List<Integer> linesToReplace = getUniqueRandoms(numberOfErrors);

        for (Integer i : linesToReplace) {
            System.out.println(i + 1);
            replaceLine(i);
        }

        String exercise = xmlExercise;

        for (String line : lines) {
            exercise += line + "\n";
        }

        String rest = "</value>\n"
                + "    </display>\n"
                + "    <display>\n"
                + "        <view>Exercise</view>\n"
                + "        <value>The compiler is unable to compile this code and execute it. How many lines have syntax errors?</value>\n"
                + "        <choice0>" + (numberOfErrors - 2) + "</choice0>\n"
                + "        <choice1>" + (numberOfErrors - 1) + "</choice1>\n"
                + "        <choice2>" + numberOfErrors + "</choice2>\n"
                + "        <choice3>" + (numberOfErrors + 1) + "</choice3>\n"
                + "        <solution>" + numberOfErrors + "</solution>\n"
                + "    </display>\n"
                + "</exercise>";

        exercise += rest;

        return exercise;
    }

    public String makeType1Exercise() {
        try {
            lines = Files.readAllLines(Paths.get(pathToTemplate), Charset.forName(encoding));
        } catch (IOException ie) {
        }

        linesUsed = new ArrayList<>();

        int randomLine = random.nextInt(lines.size());
        replaceLine(randomLine);

        String exercise = xmlExercise;

        for (String line : lines) {
            exercise += line + "\n";
        }

        for (int i = 0; i < 3; i++) {
            generateDistractions();
        }
        
        Collections.shuffle(linesUsed);

        String rest = "</value>\n"
                + "    </display>\n"
                + "    <display>\n"
                + "        <view>Exercise</view>\n"
                + "        <value>The compiler is unable to compile this code and execute it. Which line has a syntax error?</value>\n"
                + "        <choice0>Line " + linesUsed.get(0) + "</choice0>\n"
                + "        <choice1>Line " + linesUsed.get(1) + "</choice1>\n"
                + "        <choice2>Line " + linesUsed.get(2) + "</choice2>\n"
                + "        <choice3>Line " + linesUsed.get(3) + "</choice3>\n"
                + "        <solution>" + solutionText + "</solution>\n"
                + "    </display>\n"
                + "</exercise>";

        exercise += rest;

        return exercise;
    }

    private List<Integer> getUniqueRandoms(int numberOfErrors) {
        ArrayList<Integer> nums = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            nums.add(i);
        }

        Collections.shuffle(nums);
        return nums.subList(0, numberOfErrors);
    }

    private void generateDistractions() {
        int choice = random.nextInt(lines.size()) + 1;

        while ((lines.get(choice - 1).trim().isEmpty()) || (linesUsed.contains(choice))) {
            choice = random.nextInt(lines.size()) + 1;
        }

        linesUsed.add(choice);
    }
}
