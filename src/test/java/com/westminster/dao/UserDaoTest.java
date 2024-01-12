
package com.westminster.dao;

import org.junit.jupiter.api.Test;

import com.westminster.util.Validator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 * This class contains unit tests for the UserDao class.
 */
class UserDaoTest {
    // instance variables
    private static final UserDao userDao = new UserDao();

    /**
     * This method is run before each test. Adds a user to the database.
     */
    @BeforeEach
    void init() {
        // Test case for adding a user
        String hashedPassword = Validator.hashPassword("password");
        userDao.addUser("john", hashedPassword, "John", "Doe", "john@example.com");
    }

    /**
     * This method is run after each test. Removes the user from the database.
     */
    @AfterEach
    void cleanUp() {
        // Test case for removing a user
        userDao.removeUser("john");
    }

    /**
     * This method tests the addUser method.
     */
    @Test
    void doesExist() {
        // Test case for checking if a user exists
        assertTrue(userDao.doesExist("john"));
        assertFalse(userDao.doesExist("nonexistent"));
    }

    /**
     * This method tests the getUser method.
     */
    @Test
    void getUserPasswordHash() {
        // Test case for getting the password hash of a user
        String passwordHash = userDao.getUserPasswordHash("john");
        assertNotNull(passwordHash);
    }

    /**
     * This method tests the getUser method.
     */
    @Test
    void getUserCount() {
        // Test case for getting the user count
        int userCount = userDao.getUserCount();
        assertTrue(userCount >= 0);
    }
}