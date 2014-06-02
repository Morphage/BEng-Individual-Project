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
public class KnowledgeDistributionTable {
    
    private static final String TABLE_NAME = "knowledgedistribution";

    private static final String LOGIN_NAME = "login_name";
    private static final String EXERCISE_CATEGORY = "exercise_category";
    private static final String LEVEL_0 = "level0";
    private static final String LEVEL_1 = "level1";
    private static final String LEVEL_2 = "level2";
    private static final String LEVEL_3 = "level3";
    private static final String LEVEL_4 = "level4";
    private static final String LEVEL_5 = "level5";
    private static final String LEVEL_6 = "level6";
    private static final String LEVEL_7 = "level7";
    private static final String LEVEL_8 = "level8";
    private static final String LEVEL_9 = "level9";
    private static final String LEVEL_10 = "level10";
    

    public static double[] getDistribution(String loginName, String exerciseCategory) {
        PreparedStatement ps = null;
        Connection connection = Database.getConnection();
        ResultSet resultSet;

        double[] knowledgeDistribution = new double[11];

        try {
            String query = "SELECT " + LEVEL_0 + ", " + LEVEL_1 + ", " + LEVEL_2 + ", "
                    + LEVEL_3 + ", " + LEVEL_4 + ", " + LEVEL_5 + ", " + LEVEL_6 + ", "
                    + LEVEL_7 + ", " + LEVEL_8 + ", " + LEVEL_9 + ", " + LEVEL_10
                    + " FROM " + TABLE_NAME + " WHERE " + LOGIN_NAME + " = ? AND "
                    + EXERCISE_CATEGORY + " = ?";
                    
            ps = connection.prepareStatement(query);
            ps.setString(1, loginName);
            ps.setString(2, exerciseCategory);
            resultSet = ps.executeQuery();
            
            if (resultSet.next()) {
                for (int i = 0; i < 11; i++) {
                    knowledgeDistribution[i] = resultSet.getDouble("level" + i);
                }
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

        return knowledgeDistribution;
    }
    
}
