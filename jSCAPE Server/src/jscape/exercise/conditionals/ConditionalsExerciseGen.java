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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author achantreau
 */
public class ConditionalsExerciseGen {

    private static final String ENCODING = "UTF-8";
    private static final String PATH_TO_TEMPLATE = "src\\jscape\\server\\data\\conditionals.template";
    private static final String PATH_TO_STRINGS = "src\\jscape\\server\\data\\strings.data";

    private static String xmlExercise = "<?xml version=\"1.0\"?>\n"
            + "<exercise>\n"
            + "    <display>\n"
            + "        <view>CodeEditor</view>\n"
            + "        <value>";

    private static String template = null;

    private static HashMap<String, String> variablesType;
    private static HashMap<String, String> assignedValues;
    private static ArrayList<String> intVariables;
    private static ArrayList<String> stringVariables;
    private static ArrayList<String> booleanVariables;

    private static List<String> possibleStrings;
    private static String[] possibleBooleans = {"true", "false"};
    private static String[] intComparisonOps = {"==", "!=", "<", "<=", ">", ">="};
    private static String[] booleanComparisonOps = {"==", "!=", "&&", "||"};
    private static String[] arithmeticOps = {"+", "-"};

    private static Random random = new Random();

    private int numberOfVariables;
    private int randomOffset;

    private String justVariables = "";

    public ConditionalsExerciseGen() {
        try {
            template = new String(Files.readAllBytes(Paths.get(PATH_TO_TEMPLATE)), ENCODING);
            possibleStrings = Files.readAllLines(Paths.get(PATH_TO_STRINGS), Charset.forName(ENCODING));
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public String makeExercise() {
        int difficulty = random.nextInt(3);
        String exercise = "";

        if (difficulty == 0) {
            exercise = makeEasyExercise();
        } else if (difficulty == 1) {
            exercise = makeMediumExercise();
        } else if (difficulty == 2) {
            exercise = makeDifficultExercise();
        }
        return exercise;
    }

    private String makeDifficultExercise() {
        variablesType = new HashMap<>();
        assignedValues = new HashMap<>();
        intVariables = new ArrayList<>();
        stringVariables = new ArrayList<>();
        booleanVariables = new ArrayList<>();

        String exercise = xmlExercise;

        String randomCode = createRandomCode();
        randomCode = randomCode.replace("public class ConditionalsExercise {\r\n"
                + "    public static void main(String[] args) {", " ");
        randomCode = randomCode.substring(0, randomCode.length() - 4);

        int id1 = random.nextInt(numberOfVariables) + randomOffset;
        int id2 = random.nextInt(numberOfVariables) + randomOffset;
        while (id1 == id2) {
            id2 = random.nextInt(numberOfVariables) + randomOffset;
        }

        int id3 = random.nextInt(numberOfVariables) + randomOffset;
        while ((id1 == id3) || (id2 == id3)) {
            id3 = random.nextInt(numberOfVariables) + randomOffset;
        }

        String randomVar1 = "var" + id1;
        String randomVar2 = "var" + id2;
        String randomVar3 = "var" + id3;
        String randomVar1Type = variablesType.get(randomVar1);
        String randomVar2Type = variablesType.get(randomVar2);
        String randomVar3Type = variablesType.get(randomVar3);

        Interpreter i = new Interpreter();  // Construct an interpreter
        String oldValue1 = "";
        String newValue1 = "";
        String oldValue2 = "";
        String newValue2 = "";
        String oldValue3 = "";
        String newValue3 = "";

        try {
            i.eval(justVariables);
            oldValue1 = "" + i.get(randomVar1);
            oldValue2 = "" + i.get(randomVar2);
            oldValue3 = "" + i.get(randomVar3);
            i.eval(randomCode);
            newValue1 = "" + i.get(randomVar1);
            newValue2 = "" + i.get(randomVar2);
            newValue3 = "" + i.get(randomVar3);
        } catch (EvalError ee) {
        }

        randomCode = randomCode.replace("&&", "&amp;&amp;");
        randomCode = randomCode.replace("<", "&lt;");
        randomCode = randomCode.replace(">", "&gt;");
        randomCode = "public class ConditionalsExercise {\r\n"
                + "    public static void main(String[] args) {"
                + randomCode;
        randomCode += "}\n";
        randomCode += "}";

        exercise += randomCode;

        ArrayList<String> choicesList = new ArrayList<>();

        String solutionValue = randomVar1 + " = " + newValue1 + "; " + randomVar2 + " = " + newValue2 + "; "
                + randomVar3 + " = " + newValue3;
        choicesList.add(solutionValue);

        if (newValue1.equals(oldValue1) && newValue2.equals(oldValue2) && newValue3.equals(oldValue3)) {
            choicesList.add(randomVar1 + " = " + getRandomValue(randomVar1Type) + "; " + randomVar2 + " = " + getRandomValue(randomVar2Type)
            + "; " + randomVar3 + " = " + getRandomValue(randomVar3Type));
        } else {
            choicesList.add(randomVar1 + " = " + newValue1 + "; " + randomVar2 + " = " + oldValue2 + "; " + randomVar3 + " = " + getRandomValue(randomVar3Type));
        }

        if (newValue1.equals(oldValue1)) {
            choicesList.add(randomVar1 + " = " + getRandomValue(randomVar1Type) + "; " + randomVar2 + " = " + newValue2 + "; " + randomVar3 + " = " + newValue3);
        } else {
            choicesList.add(randomVar1 + " = " + oldValue1 + "; " + randomVar2 + " = " + newValue2 + "; " + randomVar3 + " = " + newValue3);
        }

        if (newValue2.equals(oldValue2)) {
            choicesList.add(randomVar1 + " = " + newValue1 + "; " + randomVar2 + " = " + getRandomValue(randomVar2Type) + "; " + randomVar3 + " = " + newValue3);
        } else {
            choicesList.add(randomVar1 + " = " + newValue1 + "; " + randomVar2 + " = " + oldValue2 + "; " + randomVar3 + " = " + newValue3);
        }
        Collections.shuffle(choicesList);

        String rest = "</value>\n"
                + "    </display>\n"
                + "    <display>\n"
                + "        <view>Exercise</view>\n"
                + "        <value>What is the correct combination of final values?</value>\n"
                + "        <choice0>" + choicesList.get(0) + "</choice0>\n"
                + "        <choice1>" + choicesList.get(1) + "</choice1>\n"
                + "        <choice2>" + choicesList.get(2) + "</choice2>\n"
                + "        <choice3>" + choicesList.get(3) + "</choice3>\n"
                + "        <solution>" + solutionValue + "</solution>\n"
                + "    </display>\n"
                + "</exercise>";

        exercise += rest;

        return exercise;
    }

    private String makeMediumExercise() {
        variablesType = new HashMap<>();
        assignedValues = new HashMap<>();
        intVariables = new ArrayList<>();
        stringVariables = new ArrayList<>();
        booleanVariables = new ArrayList<>();

        String exercise = xmlExercise;

        String randomCode = createRandomCode();
        randomCode = randomCode.replace("public class ConditionalsExercise {\r\n"
                + "    public static void main(String[] args) {", " ");
        randomCode = randomCode.substring(0, randomCode.length() - 4);

        int id1 = random.nextInt(numberOfVariables) + randomOffset;
        int id2 = random.nextInt(numberOfVariables) + randomOffset;
        while (id1 == id2) {
            id2 = random.nextInt(numberOfVariables) + randomOffset;
        }

        String randomVar1 = "var" + id1;
        String randomVar2 = "var" + id2;
        String randomVar1Type = variablesType.get(randomVar1);
        String randomVar2Type = variablesType.get(randomVar2);

        Interpreter i = new Interpreter();  // Construct an interpreter
        String oldValue1 = "";
        String newValue1 = "";
        String oldValue2 = "";
        String newValue2 = "";

        try {
            i.eval(justVariables);
            oldValue1 = "" + i.get(randomVar1);
            oldValue2 = "" + i.get(randomVar2);
            i.eval(randomCode);
            newValue1 = "" + i.get(randomVar1);
            newValue2 = "" + i.get(randomVar2);
        } catch (EvalError ee) {
        }

        randomCode = randomCode.replace("&&", "&amp;&amp;");
        randomCode = randomCode.replace("<", "&lt;");
        randomCode = randomCode.replace(">", "&gt;");
        randomCode = "public class ConditionalsExercise {\r\n"
                + "    public static void main(String[] args) {"
                + randomCode;
        randomCode += "}\n";
        randomCode += "}";

        exercise += randomCode;

        ArrayList<String> choicesList = new ArrayList<>();

        String solutionValue = randomVar1 + " = " + newValue1 + "; " + randomVar2 + " = " + newValue2;
        choicesList.add(solutionValue);

        if (newValue1.equals(oldValue1) && newValue2.equals(oldValue2)) {
            choicesList.add(randomVar1 + " = " + getRandomValue(randomVar1Type) + "; " + randomVar2 + " = " + getRandomValue(randomVar2Type));
        } else {
            choicesList.add(randomVar1 + " = " + oldValue1 + "; " + randomVar2 + " = " + getRandomValue(randomVar2Type));
        }

        if (newValue1.equals(oldValue1)) {
            choicesList.add(randomVar1 + " = " + getRandomValue(randomVar1Type) + "; " + randomVar2 + " = " + newValue2);
        } else {
            choicesList.add(randomVar1 + " = " + oldValue1 + "; " + randomVar2 + " = " + newValue2);
        }

        if (newValue2.equals(oldValue2)) {
            choicesList.add(randomVar1 + " = " + newValue1 + "; " + randomVar2 + " = " + getRandomValue(randomVar2Type));
        } else {
            choicesList.add(randomVar1 + " = " + newValue1 + "; " + randomVar2 + " = " + oldValue2);
        }
        Collections.shuffle(choicesList);

        String rest = "</value>\n"
                + "    </display>\n"
                + "    <display>\n"
                + "        <view>Exercise</view>\n"
                + "        <value>What is the correct combination of final values?</value>\n"
                + "        <choice0>" + choicesList.get(0) + "</choice0>\n"
                + "        <choice1>" + choicesList.get(1) + "</choice1>\n"
                + "        <choice2>" + choicesList.get(2) + "</choice2>\n"
                + "        <choice3>" + choicesList.get(3) + "</choice3>\n"
                + "        <solution>" + solutionValue + "</solution>\n"
                + "    </display>\n"
                + "</exercise>";

        exercise += rest;

        return exercise;
    }

    private String makeEasyExercise() {
        variablesType = new HashMap<>();
        assignedValues = new HashMap<>();
        intVariables = new ArrayList<>();
        stringVariables = new ArrayList<>();
        booleanVariables = new ArrayList<>();

        String exercise = xmlExercise;

        String randomCode = createRandomCode();
        randomCode = randomCode.replace("public class ConditionalsExercise {\r\n"
                + "    public static void main(String[] args) {", " ");
        randomCode = randomCode.substring(0, randomCode.length() - 4);

        String solutionValue = "";
        String randomVar = "var" + (random.nextInt(numberOfVariables) + randomOffset);

        Interpreter i = new Interpreter();  // Construct an interpreter
        String oldValue = "";
        String newValue = "";

        try {
            int counter = 0;
            i.eval(justVariables);
            oldValue = "" + i.get(randomVar);
            i.eval(randomCode);
            newValue = "" + i.get(randomVar);
            solutionValue = "" + i.get(randomVar);
            while (newValue.equals(oldValue) && (counter < 20)) {
                randomVar = "var" + (random.nextInt(numberOfVariables) + randomOffset);
                i.eval(justVariables);
                oldValue = "" + i.get(randomVar);
                i.eval(randomCode);
                newValue = "" + i.get(randomVar);
                solutionValue = "" + i.get(randomVar);
                counter++;
            }
        } catch (EvalError ee) {
        }

        randomCode = randomCode.replace("&&", "&amp;&amp;");
        randomCode = randomCode.replace("<", "&lt;");
        randomCode = randomCode.replace(">", "&gt;");
        randomCode = "public class ConditionalsExercise {\r\n"
                + "    public static void main(String[] args) {"
                + randomCode;
        randomCode += "}\n";
        randomCode += "}";

        exercise += randomCode;

        String rest = "";

        Set<String> choicesSet = new HashSet<>();
        ArrayList<String> choicesList = new ArrayList<>();

        if ("boolean".equals(variablesType.get(randomVar))) {
            rest = "</value>\n"
                    + "    </display>\n"
                    + "    <display>\n"
                    + "        <view>Exercise</view>\n"
                    + "        <value>What is the final value of variable " + randomVar + "?</value>\n"
                    + "        <choice0>true</choice0>\n"
                    + "        <choice1>false</choice1>\n"
                    + "        <choice2>true</choice2>\n"
                    + "        <choice3>false</choice3>\n"
                    + "        <solution>" + solutionValue + "</solution>\n"
                    + "    </display>\n"
                    + "</exercise>";
        } else if ("int".equals(variablesType.get(randomVar))) {
            choicesSet.add(newValue);

            if (!newValue.equals(oldValue)) {
                choicesSet.add(oldValue);
            }

            while (choicesSet.size() != 4) {
                int randomNumber = random.nextInt(500);
                choicesSet.add("" + randomNumber);
            }

            for (String s : choicesSet) {
                choicesList.add(s);
            }

            Collections.shuffle(choicesList);

            rest = "</value>\n"
                    + "    </display>\n"
                    + "    <display>\n"
                    + "        <view>Exercise</view>\n"
                    + "        <value>What is the final value of variable " + randomVar + "?</value>\n"
                    + "        <choice0>" + choicesList.get(0) + "</choice0>\n"
                    + "        <choice1>" + choicesList.get(1) + "</choice1>\n"
                    + "        <choice2>" + choicesList.get(2) + "</choice2>\n"
                    + "        <choice3>" + choicesList.get(3) + "</choice3>\n"
                    + "        <solution>" + solutionValue + "</solution>\n"
                    + "    </display>\n"
                    + "</exercise>";

        } else if ("String".equals(variablesType.get(randomVar))) {
            choicesSet.add(newValue);

            if (!newValue.equals(oldValue)) {
                choicesSet.add(oldValue);
            }

            while (choicesSet.size() != 4) {
                String possibleChoice = possibleStrings.get(random.nextInt(possibleStrings.size()));
                choicesSet.add(possibleChoice);
            }

            for (String s : choicesSet) {
                choicesList.add(s);
            }

            Collections.shuffle(choicesList);

            rest = "</value>\n"
                    + "    </display>\n"
                    + "    <display>\n"
                    + "        <view>Exercise</view>\n"
                    + "        <value>What is the final value of variable " + randomVar + "?</value>\n"
                    + "        <choice0>" + choicesList.get(0) + "</choice0>\n"
                    + "        <choice1>" + choicesList.get(1) + "</choice1>\n"
                    + "        <choice2>" + choicesList.get(2) + "</choice2>\n"
                    + "        <choice3>" + choicesList.get(3) + "</choice3>\n"
                    + "        <solution>" + solutionValue + "</solution>\n"
                    + "    </display>\n"
                    + "</exercise>";
        }

        exercise += rest;

        return exercise;
    }

    private String createRandomCode() {
        numberOfVariables = random.nextInt(3) + 3;
        randomOffset = random.nextInt(7) + 1;

        String buildCode = "";
        for (int i = 0; i < numberOfVariables; i++) {
            buildCode += declareVariable(i + randomOffset) + "\n" + "        ";
        }
        justVariables = buildCode;
        String javaCode = template.replace("%VARIABLES_DECLARATIONS%", buildCode);

        buildCode = "";
        for (int i = 0; i < numberOfVariables; i++) {
            buildCode += assignValue(i + randomOffset) + "\n" + "        ";
        }
        justVariables += "\n" + buildCode;
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
            int createIfStatement = random.nextInt(10);

            if (createIfStatement < 4) {
                switch (type) {
                    case "int":
                        String intComparisonOp = intComparisonOps[random.nextInt(intComparisonOps.length)];

                        ifStatements += "if (var" + varId + " " + intComparisonOp + " " + getRandomVariableOrValue("int", "var" + varId, false) + ") {\n";
                        ifStatements += fillIfStatement();
                        break;
                    case "boolean":
                        ifStatements += "if (var" + varId + ") {\n";
                        ifStatements += fillIfStatement();
                        break;
                    case "String":
                        String randomVariableOrValue = getRandomVariableOrValue("String", "var" + varId, false);
                        if (stringVariables.contains(randomVariableOrValue)) {
                            ifStatements += "if (var" + varId + ".equals(" + randomVariableOrValue + ")) {\n";
                        } else {
                            ifStatements += "if (var" + varId + ".equals(\"" + randomVariableOrValue + "\")) {\n";
                        }

                        ifStatements += fillIfStatement();
                        break;
                }
            }
        }

        return ifStatements;
    }

    private String fillIfStatement() {
        String ifStatements = "";
        int insideStatements = random.nextInt(5) + 1;

        for (int j = 0; j < insideStatements; j++) {
            String newVar = getRandomVariable();
            int simpleOrComplex = random.nextInt(4);

            if (simpleOrComplex != 3) {
                String randomVariableOrValue = getRandomVariableOrValue(variablesType.get(newVar), newVar, true);
                if ("String".equals(variablesType.get(newVar))) {
                    if (stringVariables.contains(randomVariableOrValue)) {
                        ifStatements += "            " + newVar + " = " + randomVariableOrValue;
                    } else {
                        ifStatements += "            " + newVar + " = \"" + randomVariableOrValue + "\"";
                    }
                } else {
                    ifStatements += "            " + newVar + " = " + randomVariableOrValue;
                }
            } else {
                String newVarType = variablesType.get(newVar);
                if ("int".equals(newVarType)) {
                    ifStatements += "            " + newVar + " = " + getRandomVariableOrValue(newVarType, "", true);
                    ifStatements += " " + arithmeticOps[random.nextInt(arithmeticOps.length)] + " " + getRandomVariableOrValue(newVarType, "", true);
                } else if ("boolean".equals(newVarType)) {
                    String complexVariable = getRandomVariable();
                    String complexVariableType = variablesType.get(complexVariable);

                    if ("int".equals(complexVariableType)) {
                        ifStatements += "            " + newVar + " = (" + getRandomVariableOrValue("int", "", true);
                        ifStatements += " " + intComparisonOps[random.nextInt(intComparisonOps.length)] + " " + getRandomVariableOrValue("int", "", true) + ")";
                    } else if ("boolean".equals(complexVariableType)) {
                        ifStatements += "            " + newVar + " = (" + getRandomVariableOrValue("boolean", "", true);
                        ifStatements += " " + booleanComparisonOps[random.nextInt(booleanComparisonOps.length)] + " " + getRandomVariableOrValue("boolean", "", true) + ")";
                    } else if ("String".equals(complexVariableType)) {
                        String s1 = getRandomVariableOrValue("String", "", true);
                        String s2 = getRandomVariableOrValue("String", "", true);

                        if (!stringVariables.contains(s1)) {
                            s1 = "\"" + s1 + "\"";
                        }
                        if (!stringVariables.contains(s2)) {
                            s2 = "\"" + s2 + "\"";
                        }

                        ifStatements += "            " + newVar + " = (" + s1;
                        ifStatements += ".equals(" + s2 + "))";
                    }
                } else if ("String".equals(newVarType)) {
                    String randomVariableOrValue = getRandomVariableOrValue(newVarType, "", true);
                    String randomVariableOrValue2 = getRandomVariableOrValue(newVarType, "", true);
                    if (stringVariables.contains(randomVariableOrValue)) {
                        ifStatements += "            " + newVar + " = " + randomVariableOrValue;
                    } else {
                        ifStatements += "            " + newVar + " = \" " + randomVariableOrValue + "\"";
                    }

                    if (stringVariables.contains(randomVariableOrValue2)) {
                        ifStatements += " + " + randomVariableOrValue2;
                    } else {
                        ifStatements += " + \" " + randomVariableOrValue2 + "\"";
                    }
                }
            }

            ifStatements += ";\n";
        }

        int createElseStatement = random.nextInt(10);
        if (createElseStatement < 7) {
            ifStatements += "        } else {\n";

            int elseStatements = random.nextInt(5) + 1;
            for (int j = 0; j < elseStatements; j++) {
                String newVar = getRandomVariable();
                int simpleOrComplex = random.nextInt(4);

                if (simpleOrComplex != 3) {
                    String randomVariableOrValue = getRandomVariableOrValue(variablesType.get(newVar), newVar, true);
                    if ("String".equals(variablesType.get(newVar))) {
                        if (stringVariables.contains(randomVariableOrValue)) {
                            ifStatements += "            " + newVar + " = " + randomVariableOrValue;
                        } else {
                            ifStatements += "            " + newVar + " = \"" + randomVariableOrValue + "\"";
                        }
                    } else {
                        ifStatements += "            " + newVar + " = " + randomVariableOrValue;
                    }
                } else {
                    String newVarType = variablesType.get(newVar);
                    if ("int".equals(newVarType)) {
                        ifStatements += "            " + newVar + " = " + getRandomVariableOrValue(newVarType, "", true);
                        ifStatements += " " + arithmeticOps[random.nextInt(arithmeticOps.length)] + " " + getRandomVariableOrValue(newVarType, "", true);
                    } else if ("boolean".equals(newVarType)) {
                        String complexVariable = getRandomVariable();
                        String complexVariableType = variablesType.get(complexVariable);

                        if ("int".equals(complexVariableType)) {
                            ifStatements += "            " + newVar + " = (" + getRandomVariableOrValue("int", "", true);
                            ifStatements += " " + intComparisonOps[random.nextInt(intComparisonOps.length)] + " " + getRandomVariableOrValue("int", "", true) + ")";
                        } else if ("boolean".equals(complexVariableType)) {
                            ifStatements += "            " + newVar + " = (" + getRandomVariableOrValue("boolean", "", true);
                            ifStatements += " " + booleanComparisonOps[random.nextInt(booleanComparisonOps.length)] + " " + getRandomVariableOrValue("boolean", "", true) + ")";
                        } else if ("String".equals(complexVariableType)) {
                            ifStatements += "            " + newVar + " = (" + getRandomVariableOrValue("String", "", true);
                            ifStatements += ".equals(" + getRandomVariableOrValue("String", "", true) + "))";
                        }

                    } else if ("String".equals(newVarType)) {
                        String randomVariableOrValue = getRandomVariableOrValue(newVarType, "", true);
                        String randomVariableOrValue2 = getRandomVariableOrValue(newVarType, "", true);
                        if (stringVariables.contains(randomVariableOrValue)) {
                            ifStatements += "            " + newVar + " = " + randomVariableOrValue;
                        } else {
                            ifStatements += "            " + newVar + " = \" " + randomVariableOrValue + "\"";
                        }

                        if (stringVariables.contains(randomVariableOrValue2)) {
                            ifStatements += " + " + randomVariableOrValue2;
                        } else {
                            ifStatements += " + \" " + randomVariableOrValue2 + "\"";
                        }
                    }
                }

                ifStatements += ";\n";
            }

            ifStatements += "        }\n        ";
        } else {
            ifStatements += "        }\n        ";
        }

        return ifStatements;
    }

    private String assignValue(int id) {
        String assignment = "";

        if (null != variablesType.get("var" + id)) {
            switch (variablesType.get("var" + id)) {
                case "String":
                    String word = "";
                    int complexity = random.nextInt(2) + 1;
                    for (int i = 0; i < complexity; i++) {
                        word += possibleStrings.get(random.nextInt(possibleStrings.size())) + " ";
                    }
                    assignment = "var" + id + " = \"" + word.trim() + "\";";
                    assignedValues.put("var" + id, word.trim());
                    break;
                case "boolean": {
                    String value = possibleBooleans[random.nextInt(possibleBooleans.length)];
                    assignment = "var" + id + " = " + value + ";";
                    assignedValues.put("var" + id, value);
                    break;
                }
                case "int": {
                    int value = random.nextInt(500);
                    assignment = "var" + id + " = " + value + ";";
                    assignedValues.put("var" + id, "" + value);
                    break;
                }
            }
        }

        return assignment;
    }

    private String declareVariable(int id) {
        String variable = null;

        int type = random.nextInt(3);

        if (type == 0) {
            variable = "int var" + id + ";";
            variablesType.put("var" + id, "int");
            intVariables.add("var" + id);
        } else if (type == 1) {
            variable = "String var" + id + ";";
            variablesType.put("var" + id, "String");
            stringVariables.add("var" + id);
        } else if (type == 2) {
            variable = "boolean var" + id + ";";
            variablesType.put("var" + id, "boolean");
            booleanVariables.add("var" + id);
        }

        return variable;
    }

    private String getRandomVariableOrValue(String type, String exclude, boolean isAssignment) {
        String variable = "";
        int variableOrValue = random.nextInt(4);

        if ("int".equals(type)) {
            if ((intVariables.size() == 1) || (variableOrValue == 3)) {
                int randomValueOrVariableValue = random.nextInt(4);
                if (isAssignment || (randomValueOrVariableValue == 3)) {
                    variable = "" + random.nextInt(500);
                } else {
                    variable = assignedValues.get(exclude);
                }
            } else {
                variable = intVariables.get(random.nextInt(intVariables.size()));
                while (variable.equals(exclude)) {
                    variable = intVariables.get(random.nextInt(intVariables.size()));
                }
            }
        } else if ("boolean".equals(type)) {
            if ((booleanVariables.size() == 1) || (variableOrValue == 3)) {
                variable = possibleBooleans[random.nextInt(possibleBooleans.length)];
            } else {
                variable = booleanVariables.get(random.nextInt(booleanVariables.size()));
                while (variable.equals(exclude)) {
                    variable = booleanVariables.get(random.nextInt(booleanVariables.size()));
                }
            }
        } else if ("String".equals(type)) {
            if ((stringVariables.size() == 1) || (variableOrValue == 3)) {
                variable = possibleStrings.get(random.nextInt(possibleStrings.size()));
            } else {
                variable = stringVariables.get(random.nextInt(stringVariables.size()));
                while (variable.equals(exclude)) {
                    variable = stringVariables.get(random.nextInt(stringVariables.size()));
                }
            }
        }

        return variable;
    }

    private String getRandomVariable() {
        String variable = "";
        int type = random.nextInt(3);

        while (true) {
            if ((type == 0) && (!intVariables.isEmpty())) {
                break;
            }

            if ((type == 1) && (!stringVariables.isEmpty())) {
                break;
            }

            if ((type == 2) && (!booleanVariables.isEmpty())) {
                break;
            }

            type = random.nextInt(3);
        }

        if (type == 0) {
            variable = intVariables.get(random.nextInt(intVariables.size()));
        } else if (type == 1) {
            variable = stringVariables.get(random.nextInt(stringVariables.size()));
        } else if (type == 2) {
            variable = booleanVariables.get(random.nextInt(booleanVariables.size()));
        }

        return variable;
    }

    private String getRandomValue(String variableType) {
        String randomValue = "";

        if ("int".equals(variableType)) {
            randomValue = "" + random.nextInt(500);
        } else if ("boolean".equals(variableType)) {
            randomValue = possibleBooleans[random.nextInt(possibleBooleans.length)];
        } else if ("String".equals(variableType)) {
            randomValue = possibleStrings.get(random.nextInt(possibleStrings.size()));
        }

        return randomValue;
    }
}
