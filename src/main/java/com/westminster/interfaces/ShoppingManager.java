package com.westminster.interfaces;

import com.westminster.model.Product;

import java.util.ArrayList;

public interface ShoppingManager {

    void addProduct(Product product) throws Exception;

    void removeProduct (Product product) throws Exception;

    void updateProduct(Product product) throws Exception;

    void checkAvailability(Product product) throws Exception;

    void buyProduct(Product product);

    void viewAllProducts();

    void viewProduct(Product product);

    void saveProducts(ArrayList<Product> products);

}
