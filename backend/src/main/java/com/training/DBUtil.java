package com.training;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver could not be loaded.", e);
        }

        return DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/login_db",
            "root",
            "Njhermia@15"
        );
    }
}