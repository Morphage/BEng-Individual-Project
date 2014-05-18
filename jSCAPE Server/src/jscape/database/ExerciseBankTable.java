/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jscape.database;

import java.sql.Connection;
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

        ArrayList<String> exerciseInfo = new ArrayList<>();

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
            
            if (resultSet.next()) {
                exerciseInfo.add(resultSet.getString(EXERCISE_ID));

                Exercise exercise = ExerciseParser.parseXMLExercise(resultSet.getString(EXERCISE_TEXT));
                exerciseInfo.add(exercise.getLeftDisplayView());
                exerciseInfo.add(exercise.getLeftDisplayValue());
                exerciseInfo.add(exercise.getRightDisplayView());
                exerciseInfo.add(exercise.getRightDisplayValue());
                exerciseInfo.add(exercise.getChoice1());
                exerciseInfo.add(exercise.getChoice2());
                exerciseInfo.add(exercise.getChoice3());
                exerciseInfo.add(exercise.getChoice4());
                exerciseInfo.add(exercise.getSolution());
            } else {
                exerciseInfo = null;
            }
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

        return exerciseInfo;
    }

    public static void addExercise(int exerciseID) {
        PreparedStatement ps = null;
        Connection connection = Database.getConnection();

        String xmlExercise = "<?xml version=\"1.0\"?>\n"
                + "<exercise>\n"
                + "    <display>\n"
                + "        <view>CodeEditor</view>\n"
                + "        <value>public class SyntaxExercise {\n"
                + "            public static void main(String[] args) {\n"
                + "            int x = 4;\n"
                + "            y = 6;\n"
                + "            int z = 6;\n"
                + "		\n"
                + "            x++;\n"
                + "            }\n"
                + "            }</value>\n"
                + "    </display>\n"
                + "    <display>\n"
                + "        <view>Exercise</view>\n"
                + "        <value>The compiler is unable to compile this code and execute it. Which line has a syntax error?</value>\n"
                + "        <choice0>line 2</choice0>\n"
                + "        <choice1>line 7</choice1>\n"
                + "        <choice2>line 5</choice2>\n"
                + "        <choice3>line 4</choice3>\n"
                + "        <solution>3</solution>\n"
                + "    </display>\n"
                + "</exercise>";

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
