package com.westminster.controller;

import com.westminster.model.User;
import com.westminster.util.Validator;
import com.westminster.view.UserView;
import java.util.ArrayList;


public class UserController {
    private final ArrayList<User> users = new ArrayList<>();
    private static UserController instance;

    private UserController() {
        super();
    }

    public static synchronized UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    public void addUser() {
        String email = Validator.validateEmail();
        String password = Validator.validatePassword();
        if(users.size()<51){
            users.add(new User());
            users.getLast().setEmail(email);
            users.getLast().setPassword(password);
            UserView.getInstance().callArgument(UserView.USERCONFIRM);
        } else {
            UserView.getInstance().printError(UserView.MAXUSER);
        }
    }
}


