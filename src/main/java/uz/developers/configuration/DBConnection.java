package uz.developers.configuration;

import java.sql.Connection;
import java.sql.DriverManager;

import static uz.developers.configuration.Properties.*;

public class DBConnection {
    public static Connection connection = null;

    public static Connection getInstance() {
        try {
            if (connection == null || connection.isClosed()){
                connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return connection;
    }
}
