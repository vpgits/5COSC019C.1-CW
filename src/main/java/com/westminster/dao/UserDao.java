package com.westminster.dao;

import com.westminster.model.User;
import com.westminster.util.SQLiteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

/**
 * Data Access Object for User
 */
public class UserDao {
    private static ArrayList<User> users = new ArrayList<User>(50);
    /**
     * Private constructor to prevent instantiation
     */
    private UserDao() {
        super();
    }

    /**
     * Adds a user to the database
     * @param username username
     * @param password password
     * @throws UserDaoException User DAO error
     */
    public static void addUser(String username, String password) throws UserDaoException {
            users.add(new User());
            users.getLast().setEmail(username);
            users.getLast().setPassword(password);
            String sql = "INSERT INTO user(username, password) VALUES(?,?)";// sample sql string template
            try (
                    Connection conn = SQLiteConnection.connect();
                    PreparedStatement pstmt = conn.prepareStatement(sql)
            ) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.executeUpdate();
            } catch (Exception e) {
                throw new UserDaoException("Error adding user: " + e.getMessage());
            }
    }

    /**
     * Gets the password hash of a user
     * @param username username
     * @return password has of the user
     * @throws UserDaoException User DAO error
     */
    public static String getUserPasswordHash(String username) throws UserDaoException {
        String sql = "SELECT password  FROM user WHERE username = ?";
        try(
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)
                ) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
            if (pstmt.getResultSet().next()) {
                return pstmt.getResultSet().getString("password");
            } else {
                throw new UserDoesNotExistException("User does not exist");
            }
        } catch (Exception e) {
            throw new UserDaoException("Error getting user: " + e.getMessage());
        }
    }

    public static boolean isFull(){
        return users.size() >= 50;
    }
    public static boolean isEmpty(){
        return users.isEmpty();
    }


    public static class UserDaoException extends Exception {
        public UserDaoException(String message) {
            super(message);
        }
    }
    public static class UserDoesNotExistException extends Exception {
        public UserDoesNotExistException(String message) {
            super(message);
        }
    }
}
