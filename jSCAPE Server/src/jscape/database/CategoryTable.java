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
public class CategoryTable {

    private static final String TABLE_NAME = "category";

    private static final String EXERCISE_CATEGORY = "exercise_category";
    private static final String DESCRIPTION = "description";
    private static final String LECTURE_NOTES = "lecture_notes";
    private static final String HELPFUL_LINKS = "helpful_links";
    private static final String VISIBLE = "visible";

    public static ArrayList<String> getExerciseCategories() {
        PreparedStatement ps = null;
        Connection connection = Database.getConnection();
        ResultSet resultSet;

        ArrayList<String> categoryInfo = new ArrayList<>();

        try {
            String query = "SELECT " + EXERCISE_CATEGORY + "," + DESCRIPTION
                    + "," + LECTURE_NOTES + "," + HELPFUL_LINKS + " FROM " + TABLE_NAME
                    + " WHERE " + VISIBLE + " = true";
            ps = connection.prepareStatement(query);
            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                categoryInfo.add(resultSet.getString(EXERCISE_CATEGORY));
                categoryInfo.add(resultSet.getString(DESCRIPTION));
                categoryInfo.add(resultSet.getString(LECTURE_NOTES));
                categoryInfo.add(resultSet.getString(HELPFUL_LINKS));
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

        return categoryInfo;
    }

    public static void addCategory(String exerciseCategory, String description,
            String lectureNotes, String helpfulLinks, boolean visible) {
        PreparedStatement ps = null;
        Connection connection = Database.getConnection();

        try {
            String query = "INSERT INTO " + TABLE_NAME + "(" + EXERCISE_CATEGORY + ","
                    + DESCRIPTION + "," + LECTURE_NOTES + "," + HELPFUL_LINKS + ","
                    + VISIBLE + ") VALUES (?,?,?,?,?)";
            ps = connection.prepareStatement(query);
            ps.setString(1, exerciseCategory);
            ps.setString(2, description);
            ps.setString(3, lectureNotes);
            ps.setString(4, helpfulLinks);
            ps.setBoolean(5, visible);
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
