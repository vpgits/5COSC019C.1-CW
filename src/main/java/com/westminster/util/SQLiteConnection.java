package com.westminster.util;

/**
 * Connector class for SQLite database
 */
public class SQLiteConnection {
    private static SQLiteConnection instance;

    private SQLiteConnection() {
        super();
    }

    public static synchronized SQLiteConnection getInstance() {
        if (instance == null) {
            instance = new SQLiteConnection();
        }
        return instance;
    }

}
