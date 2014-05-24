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

    public ConditionalsExerciseGen() {
        try {
            template = new String(Files.readAllBytes(Paths.get(PATH_TO_TEMPLATE)), ENCODING);
            possibleStrings = Files.readAllLines(Paths.get(PATH_TO_STRINGS), Charset.forName(ENCODING));
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public String makeExercise() {
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

        //System.out.println(randomCode);
        String randomVar = "var" + (random.nextInt(numberOfVariables) + randomOffset);
        String solutionValue = "";

        Interpreter i = new Interpreter();  // Construct an interpreter

        try {
            i.eval(randomCode);
            solutionValue = "" + i.get(randomVar);
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
        numberOfVariables = random.nextInt(5) + 1;
        randomOffset = random.nextInt(7) + 1;

        String buildCode = "";
        for (int i = 0; i < numberOfVariables; i++) {
            buildCode += declareVariable(i + randomOffset) + "\n" + "        ";
        }
        String javaCode = template.replace("%VARIABLES_DECLARATIONS%", buildCode);

        buildCode = "";
        for (int i = 0; i < numberOfVariables; i++) {
            buildCode += assignValue(i + randomOffset) + "\n" + "        ";
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
            int createIfStatement = random.nextInt(10);

            if (createIfStatement < 4) {
                switch (type) {
                    case "int":
                        String intComparisonOp = intComparisonOps[random.nextInt(intComparisonOps.length)];

                        ifStatements += "if (var" + varId + " " + intComparisonOp + " " + getRandomVariableOrValue("int", "var" + varId, false) + ") {\n";

                        int insideStatements = random.nextInt(3) + 1;
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
                                    } else if ("String".equals(complexVariable)) {
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

                        int createElseStatement = random.nextInt(2);
                        if (createElseStatement != 1) {
                            ifStatements += "        } else {\n";

                            int elseStatements = random.nextInt(3) + 1;
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
                                        } else if ("String".equals(complexVariable)) {
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

                        break;
                    case "boolean":

                        break;
                    case "String":
                        break;
                }
            }
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
}
