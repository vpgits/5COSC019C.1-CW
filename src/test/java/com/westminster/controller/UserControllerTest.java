package com.westminster.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Test
    void signUp() throws Exception {
        UserController userController = new UserController();
        userController.signUp();
    }

    @Test
    void logIn() {
    }
}