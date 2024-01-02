package com.westminster.controller;

import com.westminster.interfaces.ShoppingManager;
import com.westminster.view.WestminsterShoppingManagerView;

public class WestminsterShoppingManagerController implements ShoppingManager {

    private final WestminsterShoppingManagerView westminsterShoppingManagerView;

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
                    loadProducts(productController);
                case "6":
                    exit=true;
                    break;
                default:
                    westminsterShoppingManagerView.printError("Invalid input");
                    break;
            }
        }
    }


    private void addProduct(ProductController productController) {
        productController.addProduct();
    }

    private void deleteProduct(ProductController productController) {
        productController.deleteProduct();
    }

    private void printAllProducts(ProductController productController) {
        productController.printAllProducts();
    }

    private void saveProducts(ProductController productController) {
        westminsterShoppingManagerView.printMessage("Saving products to the products.ser file");
        productController.saveProducts();
        westminsterShoppingManagerView.printMessage("Products saved successfully");
        System.out.flush();
    }
    private void loadProducts(ProductController productController) {
        westminsterShoppingManagerView.printMessage("Loading products from the products.ser file");
        productController.loadSavedProducts();
        westminsterShoppingManagerView.printMessage("Products loaded successfully");
        System.out.flush();
    }
}
