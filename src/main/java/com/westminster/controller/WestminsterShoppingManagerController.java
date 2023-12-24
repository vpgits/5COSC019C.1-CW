package com.westminster.controller;

import com.westminster.interfaces.ShoppingManager;
import com.westminster.view.GuiView;
import com.westminster.view.WestminsterShoppingManagerView;

public abstract class WestminsterShoppingManagerController implements ShoppingManager {

    private static WestminsterShoppingManagerController westminsterShoppingManagerController;
    private static ProductController productController;
    private WestminsterShoppingManagerController() {
        super();
        productController = new ProductController();
    }

    public static WestminsterShoppingManagerController getInstance(){
        if (westminsterShoppingManagerController == null){
            westminsterShoppingManagerController = new WestminsterShoppingManagerController() {
            };
        }
        return westminsterShoppingManagerController;
    }

    public void menu(String input) throws Exception{
        switch (input) {
            case "1":
                addProduct();
                break;
            case "2":
                updateProduct();
                break;
            case "3":
                deleteProduct();
                break;
            case "4":
                WestminsterShoppingManagerView.start();
                break;
            case "5":
                launchGUI();
                break;
            case "6":
                WestminsterShoppingManagerView.printMessage("Thank you for using the system");
                System.exit(0);
                break;
            default:
                WestminsterShoppingManagerView.printError("Invalid input");
                System.out.flush();
                break;
        }
    }
    public void addProduct() throws Exception {
        productController.addProduct();
    }
    public void updateProduct() throws Exception {
        productController.updateProduct();
    }
    public void deleteProduct() throws Exception {
        productController.deleteProduct();
    }
    public void launchGUI() throws Exception {
        GuiView.start();
    }
}
