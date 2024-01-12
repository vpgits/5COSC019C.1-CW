package com.westminster.controller;

import com.westminster.dao.UserDao;
import com.westminster.util.Validator;
import com.westminster.view.UserView;

/**
 * This class is the controller for the User
 */
public class UserController {
    //instance variables
    UserView userView;
    UserDao userDao;

    /**
     * Constructor for the UserController class.
     */
    public UserController() {
        super();
        userView = new UserView();
        userDao = new UserDao();
    }

    /**
     * This method is used to sign up a user.
     * @param username username
     * @param password passwordHash
     * @param firstName fisrtName
     * @param lastName lastName
     * @param email email
     * @return boolean
     */
    public boolean signUp(String username, String password, String firstName, String lastName, String email) {
        try {
            if (userDao.getUserCount() <= 50) {
                userDao.addUser(username, password, firstName, lastName, email);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new UserControllerException("User could not be added: " + e.getMessage());
        }
    }

    /**
     * This method is used to log in a user.
     * @param username username
     * @param passwordHash passwordHash
     * @return boolean
     */
    public boolean logIn(String username, String passwordHash)  {
        try{
            return Validator.checkPasswordHash(username, passwordHash);
        } catch (Exception e) {
            throw new UserControllerException("User could not be logged in: " + e.getMessage());
        }
    }

    /**
     * This method is used to log out a user.
     */
    public static class UserControllerException extends RuntimeException{
        public UserControllerException(String message) {
            super(message);
        }
    }
}