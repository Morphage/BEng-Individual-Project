public static ArrayList<String> getPerformanceStats(String loginName) {
    PreparedStatement ps;
    Connection connection = Database.getConnection();
    ResultSet resultSet;

    ArrayList<String> performanceData = new ArrayList<>();

    String query 
        = "SELECT " + EXERCISE_CATEGORY + "," 
                    + EXERCISES_ANSWERED + ","
                    + CORRECT_ANSWERS + "," 
                    + WRONG_ANSWERS + 
          " FROM "  + TABLE_NAME + 
          " WHERE " + LOGIN_NAME + " = ?";

    ps = connection.prepareStatement(query);
    ps.setString(1, loginName);
    resultSet = ps.executeQuery();

    while (resultSet.next()) {
        performanceData.add(resultSet.getString(EXERCISE_CATEGORY));
        performanceData.add("" + resultSet.getInt(EXERCISES_ANSWERED));
        performanceData.add("" + resultSet.getInt(CORRECT_ANSWERS));
        performanceData.add("" + resultSet.getInt(WRONG_ANSWERS));
    }

    return performanceData;
}
