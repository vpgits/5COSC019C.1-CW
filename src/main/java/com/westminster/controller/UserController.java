package com.westminster.controller;

import com.westminster.dao.UserDao;
import com.westminster.util.Validator;
import com.westminster.view.UserView;


public class UserController {
    UserView userView;

    public UserController() {
        super();
        userView = new UserView();
    }


    public void signUp() throws Exception {
        String username = Validator.validateUsername(userView);
        String email = Validator.validateEmail(userView);
        String password = Validator.validatePassword(userView);
        String fname = Validator.validateNonEmpty(userView, "Enter your first name");
        String lname = Validator.validateNonEmpty(userView, "Enter your last name");
        if(UserDao.getUserCount()<=50){
            UserDao.addUser(username, password, fname, lname, email);
            userView.callArgument(UserView.USERCONFIRM);
        } else {
            userView.printError(UserView.MAXUSER);
        }
    }

    public void logIn() throws Exception {
        String email = Validator.validateEmail(userView);
        String password = Validator.validatePassword(userView);
           String passwordHash = UserDao.getUserPasswordHash(email);
           if (Validator.checkPasswordHash(password, passwordHash)) {
               userView.callArgument(UserView.USERCONFIRM);
           } else {
               userView.printError(UserView.USERNOTFOUND);
           }
    }
}


