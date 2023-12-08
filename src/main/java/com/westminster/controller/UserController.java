package com.westminster.controller;

import com.westminster.dao.UserDao;
import com.westminster.model.User;
import com.westminster.util.SQLiteConnection;
import com.westminster.util.Validator;
import com.westminster.view.UserView;
import java.util.ArrayList;


public class UserController {
    UserView userView;

    public UserController() {
        super();
        userView = new UserView();
    }


    public void addUser() throws Exception {
        String email = Validator.validateEmail(userView);
        String password = Validator.validatePassword(userView);
        if(!UserDao.isFull()){
            UserDao.addUser(email, password);
            userView.callArgument(UserView.USERCONFIRM);
        } else {
            userView.printError(UserView.MAXUSER);
        }
    }

    public void logIn() throws Exception {
        String email = Validator.validateEmail(userView);
        String password = Validator.validatePassword(userView);
        if (!UserDao.isEmpty()) {
           String passwordHash = UserDao.getUserPasswordHash(email);
           if (Validator.checkPasswordHash(password, passwordHash)) {
               userView.callArgument(UserView.USERCONFIRM);
           } else {
               userView.printError(UserView.USERNOTFOUND);
           }
        } else {
            userView.printError(UserView.USERNOTFOUND);
        }
    }
}


