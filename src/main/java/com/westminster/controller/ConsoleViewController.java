package com.westminster.controller;

public class ConsoleViewController {
    private static ConsoleViewController instance;
    public ConsoleViewController() {
        super();
    }
    public static synchronized ConsoleViewController getInstance() {
        if (instance == null) {
            instance = new ConsoleViewController();
        }
        return instance;
    }
    public void start(){
        UserController.getInstance().addUser();

    }
}