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
import java.util.HashMap;

/**
 *
 * @author achantreau
 */
public class PerformanceTable {

    private static final String TABLE_NAME = "performance";

    private static final String LOGIN_NAME = "login_name";
    private static final String EXERCISE_CATEGORY = "exercise_category";
    private static final String EXERCISES_ANSWERED = "exercises_answered";
    private static final String CORRECT_ANSWERS = "correct_answers";
    private static final String WRONG_ANSWERS = "wrong_answers";
    
    private static final String DIFFICULTY_CATEGORY = "difficulty_category";
    
    private static final HashMap<String,String> nextDifficultyIfCorrect = new HashMap<>();
    private static final HashMap<String,String> nextDifficultyIfWrong = new HashMap<>();
    static {
        nextDifficultyIfCorrect.put("A1", "A2");
        nextDifficultyIfCorrect.put("A2", "A3");
        nextDifficultyIfCorrect.put("A3", "B1");
        nextDifficultyIfCorrect.put("B1", "B2");
        nextDifficultyIfCorrect.put("B2", "B3");
        nextDifficultyIfCorrect.put("B3", "C1");
        nextDifficultyIfCorrect.put("C1", "C2");
        nextDifficultyIfCorrect.put("C2", "C3");
        nextDifficultyIfCorrect.put("C3", "C3");
        
        nextDifficultyIfWrong.put("A1", "A1");
        nextDifficultyIfWrong.put("A2", "A1");
        nextDifficultyIfWrong.put("A3", "A2");
        nextDifficultyIfWrong.put("B1", "A3");
        nextDifficultyIfWrong.put("B2", "B1");
        nextDifficultyIfWrong.put("B3", "B2");
        nextDifficultyIfWrong.put("C1", "B3");
        nextDifficultyIfWrong.put("C2", "C1");
        nextDifficultyIfWrong.put("C3", "C2");
    }

    public static ArrayList<String> getPerformanceStats(String loginName) {
        PreparedStatement ps = null;
        Connection connection = Database.getConnection();
        ResultSet resultSet;

        ArrayList<String> performanceData = new ArrayList<>();

        try {
            String query = "SELECT " + EXERCISE_CATEGORY + "," + EXERCISES_ANSWERED + ","
                    + CORRECT_ANSWERS + "," + WRONG_ANSWERS + " FROM " + TABLE_NAME + " WHERE "
                    + LOGIN_NAME + " = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1, loginName);
            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                performanceData.add(resultSet.getString(EXERCISE_CATEGORY));
                performanceData.add("" + resultSet.getInt(EXERCISES_ANSWERED));
                performanceData.add("" + resultSet.getInt(CORRECT_ANSWERS));
                performanceData.add("" + resultSet.getInt(WRONG_ANSWERS));
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

        return performanceData;
    }

    public static void updatePerformanceStats(String loginName, String exerciseCategory,
            String isCorrect) {
        PreparedStatement ps = null;
        Connection connection = Database.getConnection();

        try {
            boolean isCorrectAnswer = Boolean.valueOf(isCorrect);
            String updateColumn = isCorrectAnswer ? CORRECT_ANSWERS : WRONG_ANSWERS;

            String query = "UPDATE " + TABLE_NAME + " SET (" + EXERCISES_ANSWERED
                    + "," + updateColumn + ") = (" + EXERCISES_ANSWERED + "+?,"
                    + updateColumn + "+?) WHERE " + LOGIN_NAME + " = ?"
                    + " AND " + EXERCISE_CATEGORY + " = ?";

            ps = connection.prepareStatement(query);
            ps.setInt(1, 1);
            ps.setInt(2, 1);
            ps.setString(3, loginName);
            ps.setString(4, exerciseCategory);
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

    public static void addPerformanceData(String loginName, String exerciseCategory,
            int exercisesAnswered, int correctAnswers, int wrongAnswers) {
        PreparedStatement ps = null;
        Connection connection = Database.getConnection();

        try {
            String query = "UPDATE " + TABLE_NAME + " SET (" + EXERCISES_ANSWERED
                    + "," + CORRECT_ANSWERS + "," + WRONG_ANSWERS + ") = (?,?,?) WHERE "
                    + LOGIN_NAME + " = ?" + " AND " + EXERCISE_CATEGORY + " = ?";

            ps = connection.prepareStatement(query);
            ps.setInt(1, exercisesAnswered);
            ps.setInt(2, correctAnswers);
            ps.setInt(3, wrongAnswers);
            ps.setString(4, loginName);
            ps.setString(5, exerciseCategory);
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

    public static void updateDifficultyCategory(String loginName, String exerciseCategory,
            String isCorrect) {
        PreparedStatement ps = null;
        Connection connection = Database.getConnection();
        ResultSet resultSet;
        
        String difficulty = "";

        try {
            String firstQuery = "SELECT " + DIFFICULTY_CATEGORY + " FROM performance"
                    + " WHERE " + LOGIN_NAME + " = ? AND " + EXERCISE_CATEGORY + " = ?";
            ps = connection.prepareStatement(firstQuery);
            ps.setString(1, loginName);
            ps.setString(2, exerciseCategory);
            resultSet = ps.executeQuery();
            
            if (resultSet.next()) {
                difficulty = resultSet.getString(DIFFICULTY_CATEGORY);
            }
            
            boolean isCorrectAnswer = Boolean.valueOf(isCorrect);
            if (isCorrectAnswer) {
                difficulty = nextDifficultyIfCorrect.get(difficulty);
            } else {
                difficulty = nextDifficultyIfWrong.get(difficulty);
            }
            
            String query = "UPDATE " + TABLE_NAME + " SET (" + DIFFICULTY_CATEGORY +") = (?) WHERE "
                    + LOGIN_NAME + " = ?" + " AND " + EXERCISE_CATEGORY + " = ?";

            ps = connection.prepareStatement(query);
            ps.setString(1, difficulty);
            ps.setString(2, loginName);
            ps.setString(3, exerciseCategory);
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
