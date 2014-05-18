/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jscape.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author achantreau
 */
public class StudentExerciseRecordTable {

    private static final String TABLE_NAME = "studentexerciserecord";

    private static final String LOGIN_NAME = "login_name";
    private static final String EXERCISE_ID = "exercise_id";
    private static final String ANSWER_ID = "answer_id";

    public static void answerExercise(String loginName, String exerciseId, String answerId) {
        PreparedStatement ps = null;
        Connection connection = Database.getConnection();
        
        int exerciseID = Integer.valueOf(exerciseId);
        int answerID = Integer.valueOf(answerId);

        try {
            String query = "INSERT INTO " + TABLE_NAME + " VALUES (?,?,?)";
            ps = connection.prepareStatement(query);
            ps.setString(1, loginName);
            ps.setInt(2, exerciseID);
            ps.setInt(3, answerID);
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
