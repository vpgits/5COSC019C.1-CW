package com.westminster.dao;


import com.westminster.util.SQLiteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Data Access Object for User
 */
public class UserDao {
    /**
     * Private constructor to prevent instantiation
     */
    public UserDao() {
        super();
    }

    /**
     * Adds a user to the database
     *
     * @param username username
     * @param password password
     * @throws UserDaoException User DAO error
     */
    public void addUser(String username, String password, String fname, String lname, String email) throws UserDaoException {
        if (getUserCount() >= 50) {
            throw new UserDaoException("User limit reached");
        }
        String sql = "INSERT INTO user(username, password, name, email) VALUES(?,?,?,?)";
        try (
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, fname + " " + lname);
            pstmt.setString(4, email);
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new UserDaoException("Error adding user: " + e.getMessage());
        }
    }

    /**
     * Removes a user from the database
     *
     * @param username username
     * @throws UserDaoException User DAO error
     */
    public void removeUser(String username) throws UserDaoException {
        String sql = "DELETE FROM user WHERE username = ?";
        try (
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new UserDaoException("Error removing user: " + e.getMessage());
        }
    }

    /**
     * Checks if a user exists
     *
     * @param username username
     * @return true if the user exists, false otherwise
     */
    public boolean doesExist(String username) {
        String sql = "SELECT username FROM user WHERE username = ?";
        try (
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getString("username").equals(username)) {
                return true;
            } else return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the password hash of a user
     *
     * @param username username
     * @return password has of the user
     */
    public String getUserPasswordHash(String username) {
        String sql = "SELECT password  FROM user WHERE username = ?";
        try (
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("password");
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Gets the user's name
     *
     * @return user's name
     */
    public int getUserCount() {
        String sql = "SELECT COUNT(*) FROM user";
        try (
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Custom Exception class for User DAO
     */
    public static class UserDaoException extends RuntimeException {
        public UserDaoException(String message) {
            super(message);
        }
    }
}
