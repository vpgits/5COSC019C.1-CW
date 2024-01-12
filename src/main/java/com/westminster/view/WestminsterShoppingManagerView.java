package com.westminster.view;

import com.westminster.interfaces.View;

import java.util.Scanner;

/**
 * This class is the view for the Westminster Shopping Manager.
 */
public class WestminsterShoppingManagerView implements View {
    /**
     * Constructor for the WestminsterShoppingManagerView class.
     */
    public WestminsterShoppingManagerView() {
        super();
    }

    /**
     * This method prints the menu options for the Westminster Shopping Manager.
     */
    public static void printMenuOptions() {
        System.out.println("\nPlease select an option");
        System.out.println("1. Add a new Product");
        System.out.println("2. Delete existing Product");
        System.out.println("3. Print all Products");
        System.out.println("4. Save Products to a file/Database");
        System.out.println("5. Go back to main menu");
    }

    /**
     * This method is used to get user input.
     * @param prompt The prompt to be displayed.
     */
    public String callArgument(String prompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(prompt);
        System.out.flush();
        return scanner.nextLine();
    }

    /**
     * This method is used to print messages.
     * @param message The message to be printed.
     */
    public void printMessage(String message) {
        System.out.println(message);
        System.out.flush();
    }

    /**
     * This method is used to print errors.
     * @param message The error message to be printed.
     */
    public void printError(String message) {
        System.err.println(message);
        System.out.flush();
    }
}
