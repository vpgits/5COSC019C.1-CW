package com.westminster.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShoppingCart implements Serializable {
    //instance variables
    private String username; //ID of the user
    private UUID uuid; //ID of the shopping cart
    private ArrayList< Product> products; //list of products in the shopping cart

    public ShoppingCart( String username) {
        super();
        this.username = username;
        this.uuid = UUID.randomUUID();
        products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(String productId) {
        products.remove(productId);
    }

    public void updateProduct(Product product) {
        products.set(products.indexOf(product), product);
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public double getTotalPrice() {
        return products.stream().mapToDouble(product -> product.getPrice()*product.getAvailableItems()).sum();
    }

    public double getDiscount(double discountPercentage) {
        return getTotalPrice()*( discountPercentage);
    }

    public double getFinalPrice(double discountPercentage){
        return getTotalPrice() - getDiscount(discountPercentage);
    }

    public void clear() {
        products.clear();
    }

    public String getUuid() {
        return uuid.toString();
    }

    public String getUsername() {
        return username;
    }

    public boolean isProductInTheCart(String productId) {
        return products.stream().anyMatch(product -> product.getProductID().equals(productId));
    }
    public Product getProductFromTheCart(String productId) {
        return products.stream().filter(product -> product.getProductID().equals(productId)).findFirst().orElse(null);
    }
}
