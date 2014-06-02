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
import jscape.irt.Item;
import jscape.server.Server;

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
    
    private static final String DIFFICULTY_CATEGORY = "difficulty_category";
    
    private static final String A = "a";
    private static final String B = "b";
    private static final String C = "c";
    
    public static ArrayList<String> getExercise(String loginName, String exerciseCategory) {
        String serverMode = Server.getServer().getServerMode();
        ArrayList<String> exerciseInfo = new ArrayList<>();
        
        if ("DIFFICULTY_CATEGORY".equals(serverMode)) {
            exerciseInfo = getExerciseWithDifficulty(loginName, exerciseCategory);
        } else if ("RANDOM".equals(serverMode)) {
            exerciseInfo = getExerciseRandom(loginName, exerciseCategory);
        } else if ("IRT".equals(serverMode)) {
            // irt stuff
        }   
        
        return exerciseInfo;
    }

    public static void addExercise(String exerciseCategory, String xmlExercise) {
        PreparedStatement ps = null;
        Connection connection = Database.getConnection();

        try {
            String query = "INSERT INTO " + TABLE_NAME + "(" + EXERCISE_CATEGORY + ", "
                    + EXERCISE_TEXT + ") VALUES (?,?)";
            ps = connection.prepareStatement(query);
            ps.setString(1, exerciseCategory);
            ps.setString(2, xmlExercise);
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

    public static void updateExerciseStats(String exerciseId, String isCorrectAnswer) {
        PreparedStatement ps = null;
        Connection connection = Database.getConnection();
        
        boolean isCorrect = Boolean.valueOf(isCorrectAnswer);
        String updateColumn = isCorrect ? CORRECT_ANSWERS : WRONG_ANSWERS;

        try {
            String query = "UPDATE " + TABLE_NAME + " SET (" + updateColumn + ") = ("
                    + updateColumn + "+?) WHERE " + EXERCISE_ID + " = ?";
            ps = connection.prepareStatement(query);
            ps.setInt(1, 1);
            ps.setInt(2, Integer.valueOf(exerciseId));
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
    
    /*********************  RANDOM EXERCISE SELECTION  ****************************/
    public static ArrayList<String> getExerciseRandom(String loginName, String exerciseCategory) {
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
    
    /*********************  ITEM RESPONSE THEORY METHODS  ****************************/
    
    public static ArrayList<Item> getItems(String loginName, String exerciseCategory) {
        PreparedStatement ps = null;
        Connection connection = Database.getConnection();
        ResultSet resultSet;

        ArrayList<Item> itemList = new ArrayList<>();

        try {
            String subquery = "SELECT " + EXERCISE_ID + " FROM " + TABLE_NAME2
                    + " WHERE " + LOGIN_NAME + " = ?";
            String query = "SELECT " + EXERCISE_ID + "," + A
                    + ", " + B + ", " + C + " FROM " + TABLE_NAME
                    + " WHERE " + EXERCISE_CATEGORY + " = ? AND " + EXERCISE_ID + " NOT IN ("
                    + subquery + ")";
            ps = connection.prepareStatement(query);
            ps.setString(1, exerciseCategory);
            ps.setString(2, loginName);
            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                int exercise_id = Integer.valueOf(resultSet.getString(EXERCISE_ID));
                double a = Double.valueOf(resultSet.getString(A));
                double b = Double.valueOf(resultSet.getString(B));
                double c = Double.valueOf(resultSet.getString(C));
                
                itemList.add(new Item(exercise_id, a, b, c));
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

        return itemList;
    }
    
    /*************************  DIFFICULTY METHODS *********************************/
    
    public static void addExerciseWithDifficulty(String exerciseCategory, String xmlExercise) {
        PreparedStatement ps = null;
        Connection connection = Database.getConnection();
        String difficulty = ExerciseParser.getDifficulty(xmlExercise);
        
        try {
            String query = "INSERT INTO " + TABLE_NAME + "(" + EXERCISE_CATEGORY + ", "
                    + EXERCISE_TEXT + ", " + DIFFICULTY_CATEGORY + ") VALUES (?,?,?)";
            ps = connection.prepareStatement(query);
            ps.setString(1, exerciseCategory);
            ps.setString(2, xmlExercise);
            ps.setString(3, difficulty);
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
    
    public static ArrayList<String> getExerciseWithDifficulty(String loginName, String exerciseCategory) {
        PreparedStatement ps = null;
        Connection connection = Database.getConnection();
        ResultSet resultSet;

        ArrayList<String> exerciseInfo = new ArrayList<>();
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
                difficulty = "" + difficulty.charAt(0);
            }
            
            String subquery = "SELECT " + EXERCISE_ID + " FROM " + TABLE_NAME2
                    + " WHERE " + LOGIN_NAME + " = ?";
            String query = "SELECT " + EXERCISE_ID + "," + EXERCISE_TEXT + " FROM " + TABLE_NAME
                    + " WHERE " + EXERCISE_CATEGORY + " = ? AND " + DIFFICULTY_CATEGORY + " = ? AND " 
                    + EXERCISE_ID + " NOT IN ("
                    + subquery + ") ORDER BY RANDOM() LIMIT 1"; // Random exercise selection for now
            ps = connection.prepareStatement(query);
            ps.setString(1, exerciseCategory);
            ps.setString(2, difficulty);
            ps.setString(3, loginName);
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
}
