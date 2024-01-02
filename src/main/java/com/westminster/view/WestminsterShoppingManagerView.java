package com.westminster.view;


import com.westminster.interfaces.View;

import java.util.Scanner;

public class WestminsterShoppingManagerView implements View {
    public WestminsterShoppingManagerView() {
        super();
    }

    public static void printMenuOptions() {
        System.out.println("Please select an option");
        System.out.println("1. Add a new Product");
        System.out.println("2. Delete existing Product");
        System.out.println("3. Print all Products");
        System.out.println("4. Save Products to a file/Database");
        System.out.println("5. Go back to previous menu");
        System.out.print("Enter your choice: ");
    }

    public String callArgument(String prompt) {
        String input;
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print(prompt);
            System.out.flush();
            input = scanner.nextLine();
        }
        return input;

    }

    public void printMessage(String message) {
        System.out.println(message);
        System.out.flush();
    }

    public void printError(String message) {
        System.err.println(message);
        System.out.flush();
    }
}
