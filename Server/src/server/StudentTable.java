/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author achantreau
 */
public class StudentTable {
    
    private static final String TABLE_NAME = "student";
    
    private static final String LOGIN_NAME_COLUMN = "login_name";
    private static final String FIRST_NAME_COLUMN = "first_name";
    private static final String LAST_NAME_COLUMN = "last_name";
        
    public static String[] getFirstNames() {
    
        PreparedStatement ps = null;
        Connection connection = Database.getConnection();
        ResultSet resultSet;

        ArrayList<String> firstNames = new ArrayList<String>();

        try {
            ps = connection.prepareStatement("SELECT * FROM " + TABLE_NAME);
            resultSet = ps.executeQuery();
            
            while(resultSet.next()) {
                String name = resultSet.getString(FIRST_NAME_COLUMN);
                firstNames.add(name); 
            }
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

        String[] list = new String[firstNames.size()];
        firstNames.toArray(list);

        return list;
    }
}
