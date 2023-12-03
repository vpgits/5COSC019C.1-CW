package com.westminster.controller;
import com.westminster.util.Validator

public class ProductController {
    public ProductController() {
        super();
    }
    public void addProduct() throws Exception {
        String productID = Validator.validateProductID();
        String productName = Validator.validateProductName();
        int availableItems = Validator.validateAvailableItems();
        double price = Validator.validatePrice();
        String type = Validator.validateType();
        if(!ProductDao.isFull()){
            ProductDao.addProduct(productID, productName, availableItems, price, type);
            ProductView.getInstance().callArgument(ProductView.PRODUCTCONFIRM);
        } else {
            ProductView.getInstance().printError(ProductView.MAXPRODUCT);
        }
    }
}
