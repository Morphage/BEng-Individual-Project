/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jscape.exercise.conditionals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 *
 * @author achantreau
 */
public class ConditionalsExerciseGen {

    private static final String encoding = "UTF-8";
    private static String pathToTemplate = "src\\jscape\\server\\data\\conditionals1.template";

    private static String xmlExercise = "<?xml version=\"1.0\"?>\n"
            + "<exercise>\n"
            + "    <display>\n"
            + "        <view>CodeEditor</view>\n"
            + "        <value>";

    private static String template = null;
    
    private static HashMap<String,String> variablesType = new HashMap<>();

    public static void main(String[] args) {
        try {
            template = new String(Files.readAllBytes(Paths.get(pathToTemplate)), encoding);
        } catch (IOException ie) {
            ie.printStackTrace();
        }

        int variablesDeclarations = (int) (Math.random() * 7 + 1);
        int randomOffset = (int) (Math.random() * 7);
        
        String buildCode = "";        
        for (int i = 0; i < variablesDeclarations; i++) {
            buildCode += declareVariable(i+randomOffset) + System.lineSeparator() + "        ";
        }        
        String javaCode = template.replace("%VARIABLES_DECLARATIONS%", buildCode);
        
        buildCode = "";
        for (int i = 0; i < variablesDeclarations; i++) {
            buildCode += assignValue(i+randomOffset) + System.lineSeparator() + "        ";
        }
        javaCode = javaCode.replace("%VARIABLES_ASSIGNMENTS%", buildCode);
        
        System.out.println(javaCode);
    }
    
    private static String assignValue(int id) {
        String assignment = "";
        
        if ("String".equals(variablesType.get("var" + id))) {
            assignment = "var" + id + " = \"hello world\";";
        } else if ("boolean".equals(variablesType.get("var" + id))) {
            assignment = "var" + id + " = true;";
        } else if ("int".equals(variablesType.get("var" + id))) {
            assignment = "var" + id + " = 10;";
        } else if ("double".equals(variablesType.get("var" + id))) {
            assignment = "var" + id + " = 5.0;";
        }
        
        return assignment;
    }
    
    private static String declareVariable(int id) {
        String variable = null;
        
        int type = (int) (Math.random() * 4);
        
        if (type == 0) {
            variable = "int var" + id + ";";
            variablesType.put("var" + id, "int");
        } else if (type == 1) {
            variable = "String var" + id + ";";
            variablesType.put("var" + id, "String");
        } else if (type == 2) {
            variable = "boolean var" + id + ";";
            variablesType.put("var" + id, "boolean");
        } else if (type == 3) {
            variable = "double var" + id + ";";
            variablesType.put("var" + id, "double");
        }
        
        return variable;
    }
}

