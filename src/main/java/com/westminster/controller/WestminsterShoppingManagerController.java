package com.westminster.controller;

import com.westminster.dao.ProductDao;
import com.westminster.interfaces.ShoppingManager;
import com.westminster.model.Product;
import com.westminster.util.Validator;
import com.westminster.view.ProductView;
import com.westminster.view.WestminsterShoppingManagerView;
import com.westminster.view.UserView;

public abstract class WestminsterShoppingManagerController implements ShoppingManager {
    private static UserController userController;
    private static UserView userView;
    private static ProductController productController;
    private static ProductView productView;
    public WestminsterShoppingManagerController() {
        super();
        userController = new UserController();
        userView = new UserView();
        productController = new ProductController();
        productView = new ProductView();
    }

    public static void menu(String input) throws Exception{
        switch (input) {
            case "1":
                productController.
                break;
            case "2":
                userController
                break;
            case "3":
                System.exit(0);
                break;
            case "4":
                WestminsterShoppingManagerView.start();
                break;
            case "5":
                WestminsterShoppingManagerView.printMessage("Thank you for using the system");
                break;
            default:
                UserView.getInstance().printError("Invalid input");
                break;
        }
    }


    public void addProduct(Product product) throws Exception {
        if (Validator.validateNewProduct(product)){
            ProductDao.addProduct(product);
            WestminsterShoppingManagerView.printMessage("Product added successfully");
        }
    }

    public void deleteProduct(Product product) throws Exception{
        if (Validator.validateProduct(product)){
            ProductDao.deleteProduct(product);
            WestminsterShoppingManagerView.printMessage("Product deleted successfully");
        }
    }

    public void updateProduct(Product product) throws Exception{
        if (Validator.validateProduct(product)){
            ProductDao.updateProduct(product);
            WestminsterShoppingManagerView.printMessage("Product updated successfully");
        }
    }

}
