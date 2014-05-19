/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jscape.server.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author achantreau
 */
public class CodeGenTest2 {

    private static final String encoding = "UTF-8";
    private static String pathToTemplate = "src\\jscape\\server\\data\\easy2.java.template";

    private static String xmlExercise = "<?xml version=\"1.0\"?>\n"
            + "<exercise>\n"
            + "    <display>\n"
            + "        <view>CodeEditor</view>\n"
            + "        <value>";

    //private static List<String> lines = null;
    private static String template = null;
    
    private static HashMap<String,String> variables = new HashMap<>();

    public static void main(String[] args) {
        try {
            //lines = Files.readAllLines(Paths.get(pathToTemplate), Charset.forName(encoding));
            template = new String(Files.readAllBytes(Paths.get(pathToTemplate)), encoding);

            /*for (String line : lines) {
             System.out.println(line);
             }*/
            //byte[] encoded = Files.readAllBytes(Paths.get("src\\jscape\\server\\data\\easy1.java.template"));
            //System.out.println(xmlExercise + new String(encoded, "UTF-8"));
        } catch (IOException ie) {
            ie.printStackTrace();
        }
        
        int numberOfErrors = (int) (Math.random() * 6 + 1);

        int variablesDeclarations = (int) (Math.random() * 7 + 1);
        int randomOffset = (int) (Math.random() * 7);
        String variablesString = "";
        
        for (int i = 0; i < variablesDeclarations; i++) {
            variablesString += declareVariable(i+randomOffset) + System.lineSeparator() + "        ";
        }
        
        String javaCode = template.replace("%VARIABLES_DECLARATIONS%", variablesString);
        
        for (int i = 0; i < variablesDeclarations; i++) {
            System.out.println(variables.get("var" + (i+randomOffset)));
        }
        
        System.out.println(javaCode);

        //int random = (int) (Math.random() * lines.size());
        //String replacement = replaceLine(random);
    }
    
    public static String declareVariable(int id) {
        String variable = null;
        
        int type = (int) (Math.random() * 4);
        
        if (type == 0) {
            variable = "int var" + id + ";";
            variables.put("var"+id, "int");
        } else if (type == 1) {
            variable = "String var" + id + ";";
            variables.put("var"+id, "String");
        } else if (type == 2) {
            variable = "char var" + id + ";";
            variables.put("var"+id, "char");
        } else if (type == 3) {
            variable = "double var" + id + ";";
            variables.put("var"+id, "double");
        }
        
        return variable;
    }

    public static String replaceLine(int lineNumber) {

        return null;
    }
}
