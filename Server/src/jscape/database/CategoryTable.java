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
import java.util.Date;

/**
 *
 * @author achantreau
 */
public class CategoryTable {

    private static final String TABLE_NAME = "category";

    private static final String EXERCISE_CATEGORY = "exercise_category";

    public static ArrayList<String> getExerciseCategories() {
        PreparedStatement ps = null;
        Connection connection = Database.getConnection();
        ResultSet resultSet;

        ArrayList<String> profileInfo = new ArrayList<>();

        try {
            String query = "SELECT " + EXERCISE_CATEGORY + " FROM " + TABLE_NAME;
            ps = connection.prepareStatement(query);
            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                profileInfo.add(resultSet.getString(EXERCISE_CATEGORY));
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

        return profileInfo;
    }

}
