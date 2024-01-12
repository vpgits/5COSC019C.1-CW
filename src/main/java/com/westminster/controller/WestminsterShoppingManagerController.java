package com.westminster.controller;

import com.westminster.interfaces.ShoppingManager;
import com.westminster.view.WestminsterShoppingManagerView;

/**
 * This class contains methods for WestminsterShoppingManagerController/ Console.
 */
public class WestminsterShoppingManagerController implements ShoppingManager {
    private final WestminsterShoppingManagerView westminsterShoppingManagerView; //instance variable

    public WestminsterShoppingManagerController() {
        super();
        westminsterShoppingManagerView = new WestminsterShoppingManagerView();
    }

    public void menu() {
        boolean exit = false;
        while (!exit) {
            ProductController productController = new ProductController();
            WestminsterShoppingManagerView.printMenuOptions();
            String input1 = westminsterShoppingManagerView.callArgument("Enter your choice: ");
            switch (input1) {
                case "1":
                    addProduct(productController);
                    break;
                case "2":
                    deleteProduct(productController);
                    break;
                case "3":
                    printAllProducts(productController);
                    break;
                case "4":
                    saveProducts(productController);
                    break;
                case "5":
                    exit = true;
                    break;
                default:
                    westminsterShoppingManagerView.printError("Invalid input");
                    break;
            }
        }
    }

    @Override
    public void addProduct(ProductController productController) {
        productController.addProduct();
    }

    @Override
    public void deleteProduct(ProductController productController) {
        productController.deleteProduct();
    }

    @Override
    public void printAllProducts(ProductController productController) {
        productController.printAllProducts();
    }

    @Override
    public void saveProducts(ProductController productController) {
        productController.saveProducts();
        System.out.flush();
    }

    public static class WestminsterShoppingManagerControllerException extends Exception {
        public WestminsterShoppingManagerControllerException(String message) {
            super(message);
        }
    }
}
