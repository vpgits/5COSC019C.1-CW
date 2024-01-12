package com.westminster.view;


import com.westminster.interfaces.View;

import java.util.Scanner;

/**
 * This class is the view for the User
 */
public class UserView implements View {
    //utility prompts
    private final Scanner scanner = new Scanner(System.in);
    public static final String EMAILPROMPT = "Enter the your email: ";
    public static final String PASSWORDHELPER = "Password should be 8-16 characters long and contain at least one " +
            "uppercase letter, one lowercase letter, one number and one special character";
    public static final String PASSWORDPROMPT = "Enter the user password." + PASSWORDHELPER + "\nPassword: ";
    public static final String USERNAMEPROMPT = "Please enter your username: ";

    /**
     * Constructor for the UserView class.
     */
    public UserView() {
        super();
    }
    /**
     * This method is used to get user input.
     * @param prompt The prompt to be displayed.
     */
    public String callArgument(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
    /**
     * This method is used to print messages.
     * @param message The message to be printed.
     */
    @Override
    public void printMessage(String message) {
        System.out.println(message);
    }

    /**
     * This method is used to print errors.
     * @param message The error message to be printed.
     */
    public void printError(String message) {
        System.err.println(message);
    }
}
