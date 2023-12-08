package com.westminster.view;


import com.westminster.interfaces.View;

import java.util.Scanner;

public class UserView implements View {
    public static final String USERNOTFOUND = "User not found";
    private final Scanner scanner = new Scanner(System.in);
    public static final String EMAILPROMPT = "Enter the user email (username): ";
    public static final String PASSWORDHELPER = "Password should be 8-16 characters long and contain at least one " +
            "uppercase letter, one lowercase letter, one number and one special character";
    public static final String PASSWORDPROMPT = "Enter the user password." + PASSWORDHELPER + "\nPassword: ";
    public static final String USERCONFIRM = "User added successfully";
    public static final String MAXUSER = "Maximum number of users reached";


    public UserView() {
        super();
    }

    public String callArgument(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    @Override
    public void printMessage(String message) {
        System.out.println(message);
    }

    public void printError(String message) {
        System.err.println(message);
    }
}
