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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author achantreau
 */
public class HistoryTable {

    private static final DateFormat df = new SimpleDateFormat("dd MMMM yyyy");

    private static final String TABLE_NAME = "history";

    private static final String LOGIN_NAME = "login_name";
    private static final String DAY = "day";
    private static final String EXERCISE_CATEGORY = "exercise_category";
    private static final String EXERCISES_ANSWERED = "exercises_answered";
    private static final String CORRECT_ANSWERS = "correct_answers";
    private static final String WRONG_ANSWERS = "wrong_answers";

    public static ArrayList<String> getProfileInfo(String loginName) {
        PreparedStatement ps = null;
        Connection connection = Database.getConnection();
        ResultSet resultSet;

        ArrayList<String> profileInfo = new ArrayList<>();

        try {
            /*String query = "SELECT " + FIRST_NAME + "," + LAST_NAME + "," + CLASS + ","
                    + LAST_LOGIN + "," + LAST_EXERCISE_ANSWERED + " FROM " + TABLE_NAME
                    + " WHERE " + LOGIN_NAME + " = ?"; 
            ps = connection.prepareStatement(query);*/
            ps.setString(1, loginName);
            resultSet = ps.executeQuery();

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

    public static void updateLastExerciseAnswered(String loginName) {
        PreparedStatement ps = null;
        Connection connection = Database.getConnection();

        try {
            /*String query = "UPDATE " + TABLE_NAME + " SET " + LAST_EXERCISE_ANSWERED + " = ? WHERE "
                    + LOGIN_NAME + " = ?";
            ps = connection.prepareStatement(query); */
            ps.setDate(1, new Date(new java.util.Date().getTime()));
            ps.setString(2, loginName);
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
