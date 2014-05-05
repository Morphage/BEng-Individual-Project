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
import java.util.HashMap;

/**
 *
 * @author achantreau
 */
public class StudentTable {
    
    private static final String TABLE_NAME = "student";
    
    private static final String LOGIN_NAME = "login_name";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String LAST_LOGIN = "last_login";
    private static final String LAST_QUESTION_ANSWERED = "last_question_answered";
       
    public static HashMap<String,String> getProfileInfo(String loginName) {
        PreparedStatement ps = null;
        Connection connection = Database.getConnection();
        ResultSet resultSet;

        HashMap<String,String> profileInfo = new HashMap<>();

        try {
            ps = connection.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE " + LOGIN_NAME + " = ?");
            ps.setString(1, loginName);
            resultSet = ps.executeQuery();
            
            resultSet.next();

            String firstName = resultSet.getString(FIRST_NAME);
            profileInfo.put("firstName", firstName);

            String lastName = resultSet.getString(LAST_NAME);
            profileInfo.put("lastName", lastName);
            
            String lastLogin = resultSet.getString(LAST_LOGIN);
            profileInfo.put("lastLogin", lastLogin);
            
            String lastQuestionAnswered = resultSet.getString(LAST_QUESTION_ANSWERED);
            profileInfo.put("lastQuestionAnswered", lastQuestionAnswered);
        }  
        catch(SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(ps != null) {
                    ps.close();
                }
            }
            catch(SQLException e) {
                e.printStackTrace();
            }
         }

        return profileInfo;
    }
}
