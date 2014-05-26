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

    private static final DateFormat df = new SimpleDateFormat("dd/MM/yy");

    private static final String TABLE_NAME = "history";

    private static final String LOGIN_NAME = "login_name";
    private static final String DAY = "day";
    private static final String EXERCISE_CATEGORY = "exercise_category";
    private static final String EXERCISES_ANSWERED = "exercises_answered";
    private static final String CORRECT_ANSWERS = "correct_answers";
    private static final String WRONG_ANSWERS = "wrong_answers";

    private static final String[] monthNames = {"January", "February", "March", "April", "May",
        "June", "July", "August", "September", "October", "November", "December"};

    public static ArrayList<String> getDateList(String loginName) {
        PreparedStatement ps = null;
        Connection connection = Database.getConnection();
        ResultSet resultSet;

        ArrayList<String> monthAndYearList = new ArrayList<>();

        try {
            String query = "SELECT DISTINCT EXTRACT(MONTH FROM " + DAY
                    + ") AS month, EXTRACT(YEAR FROM " + DAY + ") AS year FROM " + TABLE_NAME
                    + " WHERE " + LOGIN_NAME + " = ? ORDER BY month,year";
            ps = connection.prepareStatement(query);
            ps.setString(1, loginName);
            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                int month = resultSet.getInt("month");
                int year = resultSet.getInt("year");
                monthAndYearList.add(monthNames[month - 1] + " " + year);
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

        return monthAndYearList;
    }

    public static ArrayList<String> getHistoryDataForMonth(String loginName, String selectedDate,
            String exerciseCategory) {
        PreparedStatement ps = null;
        Connection connection = Database.getConnection();
        ResultSet resultSet;

        ArrayList<String> historyData = new ArrayList<>();

        String[] words = selectedDate.split(" ");
        int month = monthToInt(words[0]);
        int year = Integer.valueOf(words[1]);

        try {
            String query = "SELECT " + DAY + "," + CORRECT_ANSWERS + ","
                    + WRONG_ANSWERS + " FROM " + TABLE_NAME
                    + " WHERE " + LOGIN_NAME + " = ? AND " + EXERCISE_CATEGORY + " = ?"
                    + " AND EXTRACT(MONTH FROM " + DAY + ") = ? AND EXTRACT(YEAR FROM " + DAY
                    + ") = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1, loginName);
            ps.setString(2, exerciseCategory);
            ps.setInt(3, month);
            ps.setInt(4, year);
            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                historyData.add(df.format(resultSet.getDate(DAY)));
                historyData.add("" + resultSet.getInt(CORRECT_ANSWERS));
                historyData.add("" + resultSet.getInt(WRONG_ANSWERS));
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

        return historyData;
    }

    public static void updateHistoryData(String loginName, String exerciseCategory,
            String isCorrect) {
        PreparedStatement ps = null;
        Connection connection = Database.getConnection();
        ResultSet resultSet;

        try {
            Date currentDate = new Date(new java.util.Date().getTime());
            String testQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + LOGIN_NAME + " = ? AND "
                    + EXERCISE_CATEGORY + " = ? AND " + DAY + " = ?";

            ps = connection.prepareStatement(testQuery);
            ps.setString(1, loginName);
            ps.setString(2, exerciseCategory);
            ps.setDate(3, currentDate);
            resultSet = ps.executeQuery();

            boolean isCorrectAnswer = Boolean.valueOf(isCorrect);

            if (resultSet.next()) {
                String updateColumn = isCorrectAnswer ? CORRECT_ANSWERS : WRONG_ANSWERS;
                String updateQuery = "UPDATE " + TABLE_NAME + " SET (" + EXERCISES_ANSWERED
                        + "," + updateColumn + ") = (" + EXERCISES_ANSWERED + "+?,"
                        + updateColumn + "+?) WHERE " + LOGIN_NAME + " = ?"
                        + " AND " + EXERCISE_CATEGORY + " = ? AND " + DAY + " = ?";
                ps = connection.prepareStatement(updateQuery);
                ps.setInt(1, 1);
                ps.setInt(2, 1);
                ps.setString(3, loginName);
                ps.setString(4, exerciseCategory);
                ps.setDate(5, currentDate);
                ps.executeUpdate();
            } else {
                String insertQuery = "INSERT INTO " + TABLE_NAME + " VALUES (?,?,?,?,?,?)";
                ps = connection.prepareStatement(insertQuery);
                ps.setString(1, loginName);
                ps.setDate(2, currentDate);
                ps.setString(3, exerciseCategory);
                ps.setInt(4, 1);

                if (isCorrectAnswer) {
                    ps.setInt(5, 1);
                    ps.setInt(6, 0);
                } else {
                    ps.setInt(5, 0);
                    ps.setInt(6, 1);
                }

                ps.executeUpdate();
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
    }

    public static ArrayList<String> getYearData(String loginName, String exerciseCategory, int year) {
        PreparedStatement ps = null;
        Connection connection = Database.getConnection();
        ResultSet resultSet;

        ArrayList<String> historyData = new ArrayList<>();

        try {
            String query = "SELECT EXTRACT(MONTH FROM " + DAY + ") AS month ,SUM(" + CORRECT_ANSWERS + ") AS sum_correct,SUM("
                    + WRONG_ANSWERS + ") AS sum_wrong FROM " + TABLE_NAME
                    + " WHERE " + LOGIN_NAME + " = ? AND EXTRACT(YEAR FROM " + DAY
                    + ") = ? AND " + EXERCISE_CATEGORY + "= ?"
                    + " GROUP BY EXTRACT(MONTH FROM " + DAY + ")";
            ps = connection.prepareStatement(query);
            ps.setString(1, loginName);
            ps.setInt(2, year);
            ps.setString(3, exerciseCategory);
            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                historyData.add(monthNames[resultSet.getInt("month") - 1]);
                historyData.add("" + resultSet.getInt("sum_correct"));
                historyData.add("" + resultSet.getInt("sum_wrong"));
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

        return historyData;
    }

    public static ArrayList<String> getTotalForYear(String loginName, int year) {
        PreparedStatement ps = null;
        Connection connection = Database.getConnection();
        ResultSet resultSet;

        ArrayList<String> historyData = new ArrayList<>();

        try {
            String query = "SELECT EXTRACT(MONTH FROM " + DAY + ") AS month, SUM(" + CORRECT_ANSWERS + ") AS sum_correct, SUM("
                    + WRONG_ANSWERS + ") AS sum_wrong FROM " + TABLE_NAME
                    + " WHERE " + LOGIN_NAME + " = ? AND EXTRACT(YEAR FROM " + DAY
                    + ") = ?"
                    + " GROUP BY EXTRACT(MONTH FROM " + DAY + ")";
            ps = connection.prepareStatement(query);
            ps.setString(1, loginName);
            ps.setInt(2, year);
            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                historyData.add(monthNames[resultSet.getInt("month") - 1]);
                historyData.add("" + resultSet.getInt("sum_correct"));
                historyData.add("" + resultSet.getInt("sum_wrong"));
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

        return historyData;
    }

    public static ArrayList<String> getTotalForMonth(String loginName, String selectedDate) {
        PreparedStatement ps = null;
        Connection connection = Database.getConnection();
        ResultSet resultSet;

        ArrayList<String> historyData = new ArrayList<>();

        String[] words = selectedDate.split(" ");
        int month = monthToInt(words[0]);
        int year = Integer.valueOf(words[1]);

        try {
            String query = "SELECT " + DAY + ",SUM(" + CORRECT_ANSWERS + ") AS sum_correct,SUM("
                    + WRONG_ANSWERS + ") AS sum_wrong FROM " + TABLE_NAME
                    + " WHERE " + LOGIN_NAME + " = ? AND EXTRACT(MONTH FROM " + DAY
                    + ") = ? AND EXTRACT(YEAR FROM " + DAY + ") = ?"
                    + " GROUP BY " + DAY;
            ps = connection.prepareStatement(query);
            ps.setString(1, loginName);
            ps.setInt(2, month);
            ps.setInt(3, year);
            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                historyData.add(df.format(resultSet.getDate(DAY)));
                historyData.add("" + resultSet.getInt("sum_correct"));
                historyData.add("" + resultSet.getInt("sum_wrong"));
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

        return historyData;
    }

    public static void addHistoryData(String loginName, String day, String exerciseCategory,
            int exercisesAnswered, int correctAnswers, int wrongAnswers) {
        PreparedStatement ps = null;
        Connection connection = Database.getConnection();

        try {
            String insertQuery = "INSERT INTO " + TABLE_NAME + " VALUES (?,?,?,?,?,?)";
            ps = connection.prepareStatement(insertQuery);
            ps.setString(1, loginName);
            ps.setDate(2, Date.valueOf(day));
            ps.setString(3, exerciseCategory);
            ps.setInt(4, exercisesAnswered);
            ps.setInt(5, correctAnswers);
            ps.setInt(6, wrongAnswers);
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

    private static int monthToInt(String month) {
        int converted = 0;

        if (null != month) {
            switch (month) {
                case "January":
                    converted = 1;
                    break;
                case "February":
                    converted = 2;
                    break;
                case "March":
                    converted = 3;
                    break;
                case "April":
                    converted = 4;
                    break;
                case "May":
                    converted = 5;
                    break;
                case "June":
                    converted = 6;
                    break;
                case "July":
                    converted = 7;
                    break;
                case "August":
                    converted = 8;
                    break;
                case "September":
                    converted = 9;
                    break;
                case "October":
                    converted = 10;
                    break;
                case "November":
                    converted = 11;
                    break;
                case "December":
                    converted = 12;
                    break;
            }
        }

        return converted;
    }

}
