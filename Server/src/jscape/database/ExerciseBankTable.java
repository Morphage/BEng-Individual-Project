/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jscape.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import jscape.exercise.Exercise;
import jscape.exercise.ExerciseParser;

/**
 *
 * @author achantreau
 */
public class ExerciseBankTable {
    
    private static final String TABLE_NAME = "exercisebank";
    private static final String TABLE_NAME2 = "studentexerciserecord";

    private static final String EXERCISE_ID = "exercise_id";
    private static final String EXERCISE_CATEGORY = "exercise_category";
    private static final String EXERCISE_TEXT = "exercise_text";
    private static final String CORRECT_ANSWERS = "correct_answers";
    private static final String WRONG_ANSWERS = "wrong_answers";
    private static final String LOGIN_NAME = "login_name";
    
    public static ArrayList<String> getExercise(String loginName, String exerciseCategory) {
        PreparedStatement ps = null;
        Connection connection = Database.getConnection();
        ResultSet resultSet;

        ArrayList<String> profileInfo = new ArrayList<>();

        try {
            String subquery = "SELECT " + EXERCISE_ID + " FROM " + TABLE_NAME2
                    + " WHERE " + LOGIN_NAME + " = ?";
            String query = "SELECT " + EXERCISE_ID + "," + EXERCISE_TEXT + " FROM " + TABLE_NAME 
                    + " WHERE " + EXERCISE_CATEGORY + " = ? AND " + EXERCISE_ID + " NOT IN ("
                    + subquery + ") ORDER BY RANDOM() LIMIT 1"; // Random exercise selection for now
            ps = connection.prepareStatement(query);
            ps.setString(1, exerciseCategory);
            ps.setString(2, loginName);
            resultSet = ps.executeQuery();

            resultSet.next();

            profileInfo.add(resultSet.getString(EXERCISE_ID));
            
            Exercise exercise = ExerciseParser.parseXMLExercise(resultSet.getString(EXERCISE_TEXT));
            profileInfo.add(exercise.getLeftDisplayView());
            profileInfo.add(exercise.getLeftDisplayValue());
            profileInfo.add(exercise.getRightDisplayView());
            profileInfo.add(exercise.getRightDisplayValue());
            profileInfo.add(exercise.getChoice1());
            profileInfo.add(exercise.getChoice2());
            profileInfo.add(exercise.getChoice3());
            profileInfo.add(exercise.getChoice4());
            profileInfo.add(exercise.getSolution());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return profileInfo;
    }
    
    public static void addExercise(int exerciseID) {
        PreparedStatement ps = null;
        Connection connection = Database.getConnection();
        
        String xmlExercise = "<?xml version=\"1.0\"?>\n" +
"<exercise>\n" +
"    <display>\n" +
"        <view>CodeEditor</view>\n" +
"        <value>public class SyntaxExercise {\n" +
"            public static void main(String[] args) {\n" +
"            int x = 4;\n" +
"            int y = 6;\n" +
"		\n" +
"            x*y;\n" +
"            }\n" +
"            }</value>\n" +
"    </display>\n" +
"    <display>\n" +
"        <view>Exercise</view>\n" +
"        <value>The compiler is unable to compile this code and execute it. Which line has a syntax error?</value>\n" +
"        <choice1>line 2</choice1>\n" +
"        <choice2>line 6</choice2>\n" +
"        <choice3>line 3</choice3>\n" +
"        <choice4>line 4</choice4>\n" +
"        <solution>2</solution>\n" +
"    </display>\n" +
"</exercise>";

        try {
            String query = "INSERT INTO " + TABLE_NAME + " VALUES (?,?,?)";
            ps = connection.prepareStatement(query);
            ps.setInt(1, exerciseID);
            ps.setString(2, "Syntax");
            ps.setString(3, xmlExercise);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
}
