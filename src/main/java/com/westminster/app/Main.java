package com.westminster.app;

import com.westminster.controller.WestminsterShoppingManagerController;
import com.westminster.view.SignInUpView;

import javax.swing.*;
import java.util.Scanner;



public class Main {
    public static void main(String[] args)   {
        System.out.println("Welcome to the Westminster Shopping Manager");
        menu();
    }

    private static void menu(){
        WestminsterShoppingManagerController controller = new WestminsterShoppingManagerController();
        try(Scanner scanner = new Scanner(System.in)){
            boolean exit = false;
            while (!exit) {
                printMenuOptions();
                String input = scanner.nextLine();
                switch (input){
                    case "1":{
                        SwingUtilities.invokeLater(() -> {
                            try {
                                SignInUpView signInUpView = new SignInUpView();
                                signInUpView.setVisible(true);
                            } catch (Exception e) {
                                throw new RuntimeException("Error occurred while loading the GUI");
                            }
                        });
                        break;
                    }
                    case "2":{
                        controller.menu();
                        break;
                    }
                    case "3":{
                        exit = true;
                        break;
                    }
                    default:{
                        System.out.println("Invalid input");
                        break;
                    }
                }
            }
        }
    }

    private static void printMenuOptions(){
        System.out.println("Please select an option");
        System.out.println("1. Log in as user");
        System.out.println("2. Log in as admin");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }
}