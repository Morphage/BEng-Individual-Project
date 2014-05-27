/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jscape.exercise.strings;

import bsh.EvalError;
import bsh.Interpreter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 *
 * @author achantreau
 */
public class StringExerciseGen {

    private static final String ENCODING = "UTF-8";
    private static final String PATH_TO_STRINGS = "src\\jscape\\server\\data\\strings.data";

    private static String xmlExercise = "<?xml version=\"1.0\"?>\n"
            + "<exercise>\n"
            + "    <display>\n"
            + "        <view>CodeEditor</view>\n"
            + "        <value>";

    // split, charAt, toUpperCase, trim, toLowerCase, substring, isEmpty, indexOf
    // concat
    private static String[] stringMethods
            = {"substring(!int, !int)", "concat(!string)", "toUpperCase()", "charAt(!int)",
                "toLowerCase()", "replace(!string, !string)", "replaceFirst(!string, !string)"};

    private static List<String> possibleStrings;
    private static HashMap<String, String> variableToValueMapping;

    private ArrayList<String> s1Possibilities;
    private ArrayList<String> s2Possibilities;
    private ArrayList<String> s3Possibilities;
    private ArrayList<String> s4Possibilities;

    private static Random random = new Random();

    public StringExerciseGen() {
        try {
            possibleStrings = Files.readAllLines(Paths.get(PATH_TO_STRINGS), Charset.forName(ENCODING));
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public String makeExercise() {
        int difficulty = random.nextInt(2);
        String exercise = "";
        difficulty = 1;

        s1Possibilities = new ArrayList<>();
        s2Possibilities = new ArrayList<>();
        s3Possibilities = new ArrayList<>();
        s4Possibilities = new ArrayList<>();

        if (difficulty
                == 0) {
            exercise = makeEasyExercise();
        } else if (difficulty
                == 1) {
            exercise = makeDifficultExercise();
        }

        return exercise;
    }

    private String makeDifficultExercise() {
        String exercise = xmlExercise;
        String randomCode = createRandomCode();
        String evalCode = convertToEvalCode(randomCode);

        Interpreter i = new Interpreter();
        ArrayList<String> choicesList = new ArrayList<>();
        String solutionValue = "";

        exercise += randomCode;

        try {
            i.eval(evalCode);
            solutionValue = "s1 = " + i.get("s1") + "; " + "s2 = " + i.get("s2") + "; "
                    + "s3 = " + i.get("s3") + "; " + "s4 = " + i.get("s4");
        } catch (EvalError ee) {
            ee.printStackTrace();
        }
        
        Collections.shuffle(s1Possibilities);
        Collections.shuffle(s2Possibilities);
        Collections.shuffle(s3Possibilities);
        Collections.shuffle(s4Possibilities);
        String choice1 = "s1 = " + s1Possibilities.get(0) + "; " + "s2 = " + s2Possibilities.get(0) + "; "
                    + "s3 = " + s3Possibilities.get(0) + "; " + "s4 = " + s4Possibilities.get(0);
        
        Collections.shuffle(s1Possibilities);
        Collections.shuffle(s2Possibilities);
        Collections.shuffle(s3Possibilities);
        Collections.shuffle(s4Possibilities);
        String choice2 = "s1 = " + s1Possibilities.get(0) + "; " + "s2 = " + s2Possibilities.get(0) + "; "
                    + "s3 = " + s3Possibilities.get(0) + "; " + "s4 = " + s4Possibilities.get(0);
        
        Collections.shuffle(s1Possibilities);
        Collections.shuffle(s2Possibilities);
        Collections.shuffle(s3Possibilities);
        Collections.shuffle(s4Possibilities);
        String choice3 = "s1 = " + s1Possibilities.get(0) + "; " + "s2 = " + s2Possibilities.get(0) + "; "
                    + "s3 = " + s3Possibilities.get(0) + "; " + "s4 = " + s4Possibilities.get(0);
        
        choicesList.add(solutionValue);    
        choicesList.add(choice1);
        choicesList.add(choice2);
        choicesList.add(choice3);

        Collections.shuffle(choicesList);

        String rest = "</value>\n"
                + "    </display>\n"
                + "    <display>\n"
                + "        <view>Multiple Choice</view>\n"
                + "        <value>What is the correct combination of final values for each String?</value>\n"
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

        return null;
    }

    public String createRandomCode() {
        variableToValueMapping = new HashMap<>();

        String buildCode = "public class StringExercise {\n"
                + "    public static void main(String[] args) {\n";

        String s1 = getRandomString();
        buildCode += "        String s1 = \"" + s1 + "\";\n";
        buildCode += "        String s2 = s1;\n\n";

        variableToValueMapping.put("s1", s1);
        variableToValueMapping.put("s2", s1);
        s1Possibilities.add(s1);
        s2Possibilities.add(s1);

        buildCode += "        s1 = " + randomStringMethod("s1", "s1") + ";\n";
        s1Possibilities.add(variableToValueMapping.get("s1"));
        buildCode += "        " + randomStringMethod(null, "s2") + ";\n\n";
        String s3 = getRandomString();
        variableToValueMapping.put("s3", s3);
        variableToValueMapping.put("s4", s3);
        s3Possibilities.add(s3);
        s4Possibilities.add(s3);

        buildCode += "        String s3 = \"" + s3 + "\";\n";
        buildCode += "        " + randomStringMethod(null, "s1") + ";\n";
        s3Possibilities.add(variableToValueMapping.get("s3"));
        buildCode += "        String s4 = s3;\n";
        buildCode += "        s3 = " + randomStringMethod("s3", "s1") + ";\n";
        buildCode += "        " + randomStringMethod(null, "s3") + ";\n";
        buildCode += "        " + randomStringMethod(null, "s4") + ";\n";
        buildCode += "    }\n";
        buildCode += "}";

        return buildCode;
    }

    public String convertToEvalCode(String code) {
        String convertedCode = "";
        convertedCode = code.replace("public class StringExercise {\n"
                + "    public static void main(String[] args) {", " ");
        convertedCode = convertedCode.substring(0, convertedCode.length() - 4);
        return convertedCode;
    }

    public void evalCode(String code) {
        Interpreter i = new Interpreter();

        try {
            i.eval(code);
            System.out.println("s1 = " + i.get("s1"));
            System.out.println("s2 = " + i.get("s2"));
            System.out.println("s3 = " + i.get("s3"));
            System.out.println("s4 = " + i.get("s4"));
        } catch (EvalError ee) {
            ee.printStackTrace();
        }
    }

    private String randomStringMethod(String assignedVariable, String variableCallingMethod) {
        int method = random.nextInt(stringMethods.length);
        String randomMethod = stringMethods[method];
        String currentString = variableToValueMapping.get(variableCallingMethod);

        if ("substring(!int, !int)".equals(randomMethod)) {
            if (currentString.length() > 1) {
                int n1, n2;

                do {
                    n1 = random.nextInt(currentString.length());
                    n2 = random.nextInt(currentString.length());
                } while ((n1 > n2) || (n1 == n2));

                randomMethod = randomMethod.replaceFirst("!int", "" + n1);
                randomMethod = randomMethod.replaceFirst("!int", "" + n2);
                randomMethod = variableCallingMethod + "." + randomMethod;

                if (assignedVariable != null) {
                    variableToValueMapping.put(assignedVariable, currentString.substring(n1, n2));
                } else {
                    if ("s1".equals(variableCallingMethod)) {
                        s1Possibilities.add(currentString.substring(n1, n2));
                    } else if ("s2".equals(variableCallingMethod)) {
                        s2Possibilities.add(currentString.substring(n1, n2));
                    } else if ("s3".equals(variableCallingMethod)) {
                        s3Possibilities.add(currentString.substring(n1, n2));
                    } else if ("s4".equals(variableCallingMethod)) {
                        s4Possibilities.add(currentString.substring(n1, n2));
                    }
                }
            } else {
                randomMethod = variableCallingMethod + "." + randomMethod.replaceAll("!int", "0");
                if (assignedVariable != null) {
                    variableToValueMapping.put(assignedVariable, currentString.substring(0, 0));
                }
            }
        } else if ("concat(!string)".equals(randomMethod)) {
            String randomString = getRandomString();
            randomMethod = randomMethod.replaceFirst("!string", "\" " + randomString + "\"");
            randomMethod = variableCallingMethod + "." + randomMethod;

            if (assignedVariable != null) {
                variableToValueMapping.put(assignedVariable, currentString.concat(" " + randomString));
            } else {
                if ("s1".equals(variableCallingMethod)) {
                    s1Possibilities.add(currentString.concat(" " + randomString));
                } else if ("s2".equals(variableCallingMethod)) {
                    s2Possibilities.add(currentString.concat(" " + randomString));
                } else if ("s3".equals(variableCallingMethod)) {
                    s3Possibilities.add(currentString.concat(" " + randomString));
                } else if ("s4".equals(variableCallingMethod)) {
                    s4Possibilities.add(currentString.concat(" " + randomString));
                }
            }
        } else if ("charAt(!int)".equals(randomMethod)) {
            if (currentString.length() >= 1) {
                int loc = random.nextInt(currentString.length());
                randomMethod = randomMethod.replace("!int", "" + loc);

                if (assignedVariable != null) {
                    variableToValueMapping.put(assignedVariable, "" + currentString.charAt(loc));
                    randomMethod = "\"\" + " + variableCallingMethod + "." + randomMethod;
                } else {
                    randomMethod = variableCallingMethod + "." + randomMethod;

                    if ("s1".equals(variableCallingMethod)) {
                        s1Possibilities.add("" + currentString.charAt(loc));
                    } else if ("s2".equals(variableCallingMethod)) {
                        s2Possibilities.add("" + currentString.charAt(loc));
                    } else if ("s3".equals(variableCallingMethod)) {
                        s3Possibilities.add("" + currentString.charAt(loc));
                    } else if ("s4".equals(variableCallingMethod)) {
                        s4Possibilities.add("" + currentString.charAt(loc));
                    }
                }
            } else {
                randomMethod = variableCallingMethod + ".concat(\"" + getRandomString() + "\")";
            }
        } else if ("toLowerCase()".equals(randomMethod)) {
            randomMethod = variableCallingMethod + "." + randomMethod;

            if (assignedVariable != null) {
                variableToValueMapping.put(assignedVariable, currentString.toLowerCase());
            } else {
                if ("s1".equals(variableCallingMethod)) {
                    s1Possibilities.add("" + currentString.toLowerCase());
                } else if ("s2".equals(variableCallingMethod)) {
                    s2Possibilities.add("" + currentString.toLowerCase());
                } else if ("s3".equals(variableCallingMethod)) {
                    s3Possibilities.add("" + currentString.toLowerCase());
                } else if ("s4".equals(variableCallingMethod)) {
                    s4Possibilities.add("" + currentString.toLowerCase());
                }
            }
        } else if ("toUpperCase()".equals(randomMethod)) {
            randomMethod = variableCallingMethod + "." + randomMethod;

            if (assignedVariable != null) {
                variableToValueMapping.put(assignedVariable, currentString.toUpperCase());
            } else {
                if ("s1".equals(variableCallingMethod)) {
                    s1Possibilities.add("" + currentString.toUpperCase());
                } else if ("s2".equals(variableCallingMethod)) {
                    s2Possibilities.add("" + currentString.toUpperCase());
                } else if ("s3".equals(variableCallingMethod)) {
                    s3Possibilities.add("" + currentString.toUpperCase());
                } else if ("s4".equals(variableCallingMethod)) {
                    s4Possibilities.add("" + currentString.toUpperCase());
                }
            }
        } else if ("replace(!string, !string)".equals(randomMethod)) {
            if (currentString.length() >= 1) {
                char replaceWhat = currentString.charAt(random.nextInt(currentString.length()));
                char replaceWith = (char) (random.nextInt(26) + 'a');

                randomMethod = randomMethod.replaceFirst("!string", "\"" + replaceWhat + "\"");
                randomMethod = randomMethod.replaceFirst("!string", "\"" + replaceWith + "\"");
                randomMethod = variableCallingMethod + "." + randomMethod;

                if (assignedVariable != null) {
                    variableToValueMapping.put(assignedVariable, currentString.replace("" + replaceWhat, "" + replaceWith));
                } else {
                    if ("s1".equals(variableCallingMethod)) {
                        s1Possibilities.add(currentString.replace("" + replaceWhat, "" + replaceWith));
                    } else if ("s2".equals(variableCallingMethod)) {
                        s2Possibilities.add(currentString.replace("" + replaceWhat, "" + replaceWith));
                    } else if ("s3".equals(variableCallingMethod)) {
                        s3Possibilities.add(currentString.replace("" + replaceWhat, "" + replaceWith));
                    } else if ("s4".equals(variableCallingMethod)) {
                        s4Possibilities.add(currentString.replace("" + replaceWhat, "" + replaceWith));
                    }
                }
            } else {
                randomMethod = variableCallingMethod + ".toUpperCase()";
            }
        } else if ("replaceFirst(!string, !string)".equals(randomMethod)) {
            if (currentString.length() >= 1) {
                char replaceWhat = currentString.charAt(random.nextInt(currentString.length()));
                char replaceWith = (char) (random.nextInt(26) + 'a');

                randomMethod = randomMethod.replaceFirst("!string", "\"" + replaceWhat + "\"");
                randomMethod = randomMethod.replaceFirst("!string", "\"" + replaceWith + "\"");
                randomMethod = variableCallingMethod + "." + randomMethod;

                if (assignedVariable != null) {
                    variableToValueMapping.put(assignedVariable, currentString.replaceFirst("" + replaceWhat, "" + replaceWith));
                } else {
                    if ("s1".equals(variableCallingMethod)) {
                        s1Possibilities.add(currentString.replaceFirst("" + replaceWhat, "" + replaceWith));
                    } else if ("s2".equals(variableCallingMethod)) {
                        s2Possibilities.add(currentString.replaceFirst("" + replaceWhat, "" + replaceWith));
                    } else if ("s3".equals(variableCallingMethod)) {
                        s3Possibilities.add(currentString.replaceFirst("" + replaceWhat, "" + replaceWith));
                    } else if ("s4".equals(variableCallingMethod)) {
                        s4Possibilities.add(currentString.replaceFirst("" + replaceWhat, "" + replaceWith));
                    }
                }
            } else {
                randomMethod = variableCallingMethod + ".toLowerCase()";
            }
        }

        return randomMethod;
    }

    private String getRandomString() {
        String randomString = possibleStrings.get(random.nextInt(possibleStrings.size()));

        int modify = random.nextInt(2);
        if (modify == 0) {
            randomString = randomString.toUpperCase();
        } else if (modify == 1) {
            randomString = randomString.substring(0, 1).toUpperCase()
                    + randomString.substring(1, randomString.length());
        }

        return randomString;
    }

    private void printMap() {
        System.out.println("******PRINTING MAP******");
        System.out.println("s1: " + variableToValueMapping.get("s1"));
        System.out.println("s2: " + variableToValueMapping.get("s2"));
        System.out.println("s3: " + variableToValueMapping.get("s3"));
        System.out.println("s4: " + variableToValueMapping.get("s4"));
        System.out.println("************************");
    }
}
