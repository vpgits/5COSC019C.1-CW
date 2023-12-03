package com.westminster.view;

import com.westminster.controller.WestminsterShoppingManagerController;

import java.util.Scanner;

public abstract class WestminsterShoppingManagerView {
    Scanner scanner = new Scanner(System.in);
    public static void start() throws Exception{
        System.out.println("Welcome to the management console ");
        boolean exit = false;
        while (!exit) {
            System.out.println("Please select an option");
            System.out.println("1. Add a new Product");
            System.out.println("2. Delete existing Product");
            System.out.println("3. Print all Products");
            System.out.println("4. Save Products to a file/Database");
            System.out.println("5. Exit");
            String input = UserView.getInstance().callArgument("Enter your choice: ");
            if (input.equals("3")) {
                exit = true;
            }
            WestminsterShoppingManagerController.menu(input);
        }

    }
    public String callArgument(String prompt){
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static void printMessage(String message){
        System.out.println(message);
    }

    public void printError(String message){
        System.err.println(message);
    }
}
