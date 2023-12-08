package com.westminster.view;

import com.westminster.controller.WestminsterShoppingManagerController;
import java.util.Scanner;

public abstract class WestminsterShoppingManagerView {
    public static void start() throws Exception{
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the management console ");
        boolean exit = false;
        while (!exit) {
            printMenuOptions();
            String input = scanner.nextLine();
            if (input.equals("5")) {
                exit = true;
            }
            WestminsterShoppingManagerController.getInstance().menu(input);
        }
    }

    private static void printMenuOptions(){
        System.out.println("Please select an option");
        System.out.println("1. Add a new Product");
        System.out.println("2. Delete existing Product");
        System.out.println("3. Print all Products");
        System.out.println("4. Save Products to a file/Database");
        System.out.println("5. Exit");
        System.out.println("Enter your choice: ");
    }

    public String callArgument(String prompt){
        Scanner scanner = new Scanner(System.in);
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static void printMessage(String message){
        System.out.println(message);
    }

    public static void printError(String message){
        System.err.println(message);
    }
}
