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
/**
 *
 * @author achantreau
 */
public class ExerciseBankTable {

    private static final String TABLE_NAME = "exercisebank";

    private static final String EXERCISE_ID = "exercise_id";
    private static final String EXERCISE_CATEGORY = "exercise_category";
    private static final String EXERCISE_TEXT = "exercise_text";
    private static final String CORRECT_ANSWERS = "correct_answers";
    private static final String WRONG_ANSWERS = "wrong_answers";
    private static final String LOGIN_NAME = "login_name";
    
    private static final String DIFFICULTY_CATEGORY = "difficulty_category";
    
    private static final String A = "a";
    private static final String B = "b";
    private static final String C = "c";
    
    public static ArrayList<String> getExerciseInfo(String exerciseCategory) {
        PreparedStatement ps = null;
        Connection connection = Database.getConnection();
        ResultSet resultSet;

        ArrayList<String> exerciseInfo = new ArrayList<>();

        try {
            String query = "SELECT * FROM " + TABLE_NAME
                    + " WHERE " + EXERCISE_CATEGORY + " = ? AND ("
                    + CORRECT_ANSWERS + " > 0 OR " + WRONG_ANSWERS
                    + " > 0)";
            ps = connection.prepareStatement(query);
            ps.setString(1, exerciseCategory);
            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                exerciseInfo.add("" + resultSet.getInt(EXERCISE_ID));
                exerciseInfo.add("" + resultSet.getInt(CORRECT_ANSWERS));
                exerciseInfo.add("" + resultSet.getInt(WRONG_ANSWERS));
                exerciseInfo.add(resultSet.getString(EXERCISE_TEXT));
                exerciseInfo.add(resultSet.getString(DIFFICULTY_CATEGORY));
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
}
