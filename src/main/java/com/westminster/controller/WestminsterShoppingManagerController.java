package com.westminster.controller;

import com.westminster.interfaces.ShoppingManager;
import com.westminster.view.GuiView;
import com.westminster.view.SignInUpView;
import com.westminster.view.WestminsterShoppingManagerView;

import javax.swing.*;
import java.util.Scanner;

public class WestminsterShoppingManagerController implements ShoppingManager {

    private static WestminsterShoppingManagerController westminsterShoppingManagerController;
    private static ProductController productController;

    private WestminsterShoppingManagerController() {
        super();
        productController = new ProductController();
    }

    public static WestminsterShoppingManagerController getInstance() {
        if (westminsterShoppingManagerController == null) {
            westminsterShoppingManagerController = new WestminsterShoppingManagerController();
        }
        return westminsterShoppingManagerController;
    }

    public void menu(String input) {

        boolean exit = false;
        while (!exit) {
            Scanner scanner = new Scanner(System.in);
            WestminsterShoppingManagerView.getInstance().printMenuOptions();

            String input1 = scanner.nextLine();
            switch (input1) {
                case "1":
                    addProduct();
                    break;
                case "2":
                    deleteProduct();
                    break;
                case "3":
                    printAllProducts();
                    break;
                case "4":
                    saveProducts();
                    break;
                case "5":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }


    public void addProduct() {
        productController.addProduct();
    }

    public void deleteProduct() {
        productController.deleteProduct();
    }

    public void printAllProducts() {
        productController.printAllProducts();
    }

    public void saveProducts() {
        System.out.println("Already saved to the database");
        System.out.flush();
    }
}
