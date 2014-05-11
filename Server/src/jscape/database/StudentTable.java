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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author achantreau
 */
public class StudentTable {

    private static final DateFormat df = new SimpleDateFormat("dd MMMM yyyy");

    private static final String TABLE_NAME = "student";

    private static final String LOGIN_NAME = "login_name";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String LAST_LOGIN = "last_login";
    private static final String LAST_QUESTION_ANSWERED = "last_question_answered";

    public static ArrayList<String> getProfileInfo(String loginName) {
        PreparedStatement ps = null;
        Connection connection = Database.getConnection();
        ResultSet resultSet;

        ArrayList<String> profileInfo = new ArrayList<>();

        try {
            String query = "SELECT " + FIRST_NAME + "," + LAST_NAME + "," + LAST_LOGIN + ","
                    + LAST_QUESTION_ANSWERED + " FROM " + TABLE_NAME + " WHERE " + LOGIN_NAME + " = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1, loginName);
            resultSet = ps.executeQuery();

            resultSet.next();

            profileInfo.add(resultSet.getString(FIRST_NAME));
            profileInfo.add(resultSet.getString(LAST_NAME));

            Date date = resultSet.getDate(LAST_LOGIN);
            profileInfo.add(df.format(date));

            date = resultSet.getDate(LAST_QUESTION_ANSWERED);
            profileInfo.add(df.format(date));         
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
}
