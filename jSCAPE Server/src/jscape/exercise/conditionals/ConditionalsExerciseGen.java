/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jscape.exercise.conditionals;

import bsh.EvalError;
import bsh.Interpreter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 *
 * @author achantreau
 */
public class ConditionalsExerciseGen {

    private static final String ENCODING = "UTF-8";
    private static final String PATH_TO_TEMPLATE = "src\\jscape\\server\\data\\conditionals1.template";
    private static final String PATH_TO_STRINGS = "src\\jscape\\server\\data\\strings.data";

    private static String xmlExercise = "<?xml version=\"1.0\"?>\n"
            + "<exercise>\n"
            + "    <display>\n"
            + "        <view>CodeEditor</view>\n"
            + "        <value>";

    private static String template = null;

    private static HashMap<String, String> variablesType = new HashMap<>();
    private static HashMap<String, String> assignedValues = new HashMap<>();

    private static List<String> possibleStrings;
    private static String[] possibleBooleans = {"true", "false"};

    private static Random random = new Random();

    private int numberOfVariables;
    private int randomOffset;

    public ConditionalsExerciseGen() {
        try {
            template = new String(Files.readAllBytes(Paths.get(PATH_TO_TEMPLATE)), ENCODING);
            possibleStrings = Files.readAllLines(Paths.get(PATH_TO_STRINGS), Charset.forName(ENCODING));
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public String makeExercise() {
        String exercise = xmlExercise;

        String randomCode = createRandomCode();
        exercise += randomCode;

        randomCode = randomCode.replace("public class ConditionalsExercise {\r\n"
                + "    public static void main(String[] args) {", " ");
        randomCode = randomCode.substring(0, randomCode.length() - 4);

        System.out.println(randomCode);

        String randomVar = "var" + (random.nextInt(numberOfVariables) + randomOffset);
        String solutionValue = "";
        
        Interpreter i = new Interpreter();  // Construct an interpreter

        try {
            i.eval(randomCode);
            solutionValue = "" + i.get(randomVar);
        } catch (EvalError ee) {
        }
                    
        System.out.println("Final value of " + randomVar + " = " + solutionValue);
        
        String rest = "</value>\n"
         + "    </display>\n"
         + "    <display>\n"
         + "        <view>Exercise</view>\n"
         + "        <value>What is the final value of variable " + randomVar + "?</value>\n"
         + "        <choice0>" + "40" + "</choice0>\n"
         + "        <choice1>" + "34" + "</choice1>\n"
         + "        <choice2>" + solutionValue + "</choice2>\n"
         + "        <choice3>" + "50" + "</choice3>\n"
         + "        <solution>" + solutionValue + "</solution>\n"
         + "    </display>\n"
         + "</exercise>";
        
        exercise += rest;

        return exercise;
    }

    private String createRandomCode() {
        numberOfVariables = random.nextInt(7) + 1;
        randomOffset = random.nextInt(7) + 1;

        String buildCode = "";
        for (int i = 0; i < numberOfVariables; i++) {
            buildCode += declareVariable(i + randomOffset) + System.lineSeparator() + "        ";
        }
        String javaCode = template.replace("%VARIABLES_DECLARATIONS%", buildCode);

        buildCode = "";
        for (int i = 0; i < numberOfVariables; i++) {
            buildCode += assignValue(i + randomOffset) + System.lineSeparator() + "        ";
        }
        javaCode = javaCode.replace("%VARIABLES_ASSIGNMENTS%", buildCode);

        buildCode = ifStatements(numberOfVariables, randomOffset);
        javaCode = javaCode.replace("%IF_STATEMENTS%", buildCode);

        return javaCode;
    }

    private String ifStatements(int numberOfVariables, int randomOffset) {
        String ifStatements = "";

        for (int i = 0; i < numberOfVariables; i++) {
            String varId = "" + (i + randomOffset);
            String type = variablesType.get("var" + varId);

            if (type.equals("int")) {
                ifStatements += "if (var" + varId + " != " + random.nextInt(500) + ") {\n";
                ifStatements += "            var" + varId + " = " + random.nextInt(500) + ";\n";
                ifStatements += "        }\n        ";
            } else if (type.equals("boolean")) {

            } else if (type.equals("String")) {

            }
        }

        return ifStatements;
    }

    private String assignValue(int id) {
        String assignment = "";

        if ("String".equals(variablesType.get("var" + id))) {
            String word = "";
            int complexity = random.nextInt(3) + 1;

            for (int i = 0; i < complexity; i++) {
                word += possibleStrings.get(random.nextInt(possibleStrings.size())) + " ";
            }

            assignment = "var" + id + " = \"" + word.trim() + "\";";
            assignedValues.put("var" + id, word.trim());
        } else if ("boolean".equals(variablesType.get("var" + id))) {
            String value = possibleBooleans[random.nextInt(possibleBooleans.length)];
            assignment = "var" + id + " = " + value + ";";
            assignedValues.put("var" + id, value);
        } else if ("int".equals(variablesType.get("var" + id))) {
            int value = random.nextInt(500);
            assignment = "var" + id + " = " + value + ";";
            assignedValues.put("var" + id, "" + value);
        }

        return assignment;
    }

    private String declareVariable(int id) {
        String variable = null;

        int type = random.nextInt(3);

        if (type == 0) {
            variable = "int var" + id + ";";
            variablesType.put("var" + id, "int");
        } else if (type == 1) {
            variable = "String var" + id + ";";
            variablesType.put("var" + id, "String");
        } else if (type == 2) {
            variable = "boolean var" + id + ";";
            variablesType.put("var" + id, "boolean");
        }

        return variable;
    }
}
