package com.westminster.controller;

import com.westminster.dao.UserDao;
import com.westminster.model.User;
import com.westminster.util.SQLiteConnection;
import com.westminster.util.Validator;
import com.westminster.view.UserView;
import java.util.ArrayList;


public class UserController {


    public UserController() {
        super();
    }


    public void addUser() throws Exception {
        String email = Validator.validateEmail();
        String password = Validator.validatePassword();
        if(!UserDao.isFull()){
            UserDao.addUser(email, password);
            UserView.getInstance().callArgument(UserView.USERCONFIRM);
        } else {
            UserView.getInstance().printError(UserView.MAXUSER);
        }
    }

    public void logIn() throws Exception {
        String email = Validator.validateEmail();
        String password = Validator.validatePassword();
        if (!UserDao.isEmpty()) {
           String passwordHash = UserDao.getUserPasswordHash(email);
           if (Validator.checkPasswordHash(password, passwordHash)) {
               UserView.getInstance().callArgument(UserView.USERCONFIRM);
           } else {
               UserView.getInstance().printError(UserView.USERNOTFOUND);
           }
        } else {
            UserView.getInstance().printError(UserView.USERNOTFOUND);
        }
    }
}


