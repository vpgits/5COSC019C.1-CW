
package com.westminster.dao;

import org.junit.jupiter.api.Test;

import com.westminster.util.Validator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

class UserDaoTest {
    private static UserDao userDao = new UserDao();


@BeforeEach
    void init() {
        // Test case for adding a user
        String hashedPassword = Validator.hashPassword("password");
        userDao.addUser("john", hashedPassword, "John", "Doe", "john@example.com");
    }

@AfterEach
    void cleanUp() {
        // Test case for removing a user
        userDao.removeUser("john");
    }

    @Test
    void doesExist() {
        // Test case for checking if a user exists
        assertTrue(userDao.doesExist("john"));
        assertFalse(userDao.doesExist("nonexistent"));
    }

    @Test
    void getUserPasswordHash() {
        // Test case for getting the password hash of a user
        String passwordHash = userDao.getUserPasswordHash("john");
        assertNotNull(passwordHash);
    }

    @Test
    void getUserCount() {
        // Test case for getting the user count
        int userCount = userDao.getUserCount();
        assertTrue(userCount >= 0);
    }
}