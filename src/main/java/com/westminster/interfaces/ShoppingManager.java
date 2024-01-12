package com.westminster.interfaces;


import com.westminster.controller.ProductController;

/**
 * This interface contains methods for managing products.
 */
public interface ShoppingManager {
    //abstract methods
    void addProduct(ProductController productController);

    void deleteProduct(ProductController productController);

    void printAllProducts(ProductController productController);

    void saveProducts(ProductController productController);

}
