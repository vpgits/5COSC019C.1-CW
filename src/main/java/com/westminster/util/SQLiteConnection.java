package com.westminster.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Connector class for SQLite database
 */
public class SQLiteConnection {

    private SQLiteConnection() {
        super();
    }

    /**
     * Connects to the sqlite database
     * Connect to the database
     * @return Connection object
     * @throws DatabaseConnectionException if connection fails
     */
    public static Connection connect() throws DatabaseConnectionException, DatabaseQueryException{
        //sqlite connection string
        String url = "jdbc:sqlite:src/main/resources/database/w1985483.sqlite";
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error connecting to database: "+ e.getMessage());
        } catch (Exception e){
            throw new DatabaseQueryException("Database query error: "+ e.getMessage());
        }
        return conn;
    }
    public static class DatabaseConnectionException extends Exception {
        public DatabaseConnectionException(String message) {
            super(message);
        }
    }
    public static class DatabaseQueryException extends Exception {
        public DatabaseQueryException(String message) {
            super(message);
        }
    }
}


