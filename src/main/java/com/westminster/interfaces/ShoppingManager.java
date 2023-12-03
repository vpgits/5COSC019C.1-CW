package com.westminster.interfaces;

import com.westminster.model.Product;

import java.util.ArrayList;

interface ShoppingManager {

    void addProduct(Product product);

    void removeProduct(Product product);

    void updateProduct(Product product);

    void checkAvailability(Product product);

    void buyProduct(Product product);

    void viewAllProducts();

    void viewProduct(Product product);

    void saveProducts(ArrayList<Product> products);

}
