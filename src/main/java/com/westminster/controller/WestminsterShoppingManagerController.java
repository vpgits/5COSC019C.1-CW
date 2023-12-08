package com.westminster.controller;

import com.westminster.dao.ProductDao;
import com.westminster.interfaces.ShoppingManager;
import com.westminster.model.Product;
import com.westminster.util.Validator;
import com.westminster.view.ProductView;
import com.westminster.view.WestminsterShoppingManagerView;
import com.westminster.view.UserView;

public abstract class WestminsterShoppingManagerController implements ShoppingManager {

    private static WestminsterShoppingManagerController westminsterShoppingManagerController;
    private static UserController userController;
    private static UserView userView;
    private static ProductController productController;
    private static ProductView productView;
    private WestminsterShoppingManagerController() {
        super();
        userController = new UserController();
        userView = new UserView();
        productController = new ProductController();
        productView = new ProductView();
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
}
