package com.westminster.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Represents a shopping cart for a user.
 */
public class ShoppingCart implements Serializable {
    // instance variables
    private String username; // ID of the user
    private UUID uuid; // ID of the shopping cart
    private ArrayList<Product> products; // list of products in the shopping cart

    /**
     * Constructs a new ShoppingCart object with the given username.
     *
     * @param username the ID of the user
     */
    public ShoppingCart(String username) {
        super();
        this.username = username;
        this.uuid = UUID.randomUUID();
        products = new ArrayList<>();
    }

    /**
     * Adds a product to the shopping cart.
     *
     * @param product the product to be added
     */
    public void addProduct(Product product) {
        products.add(product);
    }

    /**
     * Removes a product from the shopping cart based on the product ID.
     *
     * @param productId the ID of the product to be removed
     */
    public void removeProduct(String productId) {
        products.remove(
                products.stream().filter(product -> product.getProductID().equals(productId)).findFirst().orElse(null));
    }

    /**
     * Updates a product in the shopping cart.
     *
     * @param product the updated product
     */
    public void updateProduct(Product product) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getProductID().equals(product.getProductID())) {
                products.set(i, product);
                break;
            }
        }
    }

    /**
     * Returns the list of products in the shopping cart.
     *
     * @return the list of products
     */
    public ArrayList<Product> getProducts() {
        return products;
    }

    /**
     * Calculates and returns the total price of all products in the shopping cart.
     *
     * @return the total price
     */
    public double getTotalPrice() {
        return products.stream().mapToDouble(product -> product.getPrice() * product.getAvailableItems()).sum();
    }

    /**
     * Calculates and returns the discount amount based on the given discount percentage.
     *
     * @param discountPercentage the discount percentage
     * @return the discount amount
     */
    public double getDiscount(double discountPercentage) {
        return getTotalPrice() * (discountPercentage);
    }

    /**
     * Clears the shopping cart by removing all products.
     */
    public void clear() {
        products.clear();
    }

    /**
     * Returns the UUID of the shopping cart.
     *
     * @return the UUID as a string
     */
    public String getUuid() {
        return uuid.toString();
    }

    /**
     * Returns the username of the user associated with the shopping cart.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Checks if a product with the given product ID is in the shopping cart.
     *
     * @param productId the ID of the product to check
     * @return true if the product is in the cart, false otherwise
     */
    public boolean isProductInTheCart(String productId) {
        return products.stream().anyMatch(product -> product.getProductID().equals(productId));
    }

    /**
     * Retrieves the product with the given product ID from the shopping cart.
     *
     * @param productId the ID of the product to retrieve
     * @return the product if found, null otherwise
     */
    public Product getProductFromTheCart(String productId) {
        return products.stream().filter(product -> product.getProductID().equals(productId)).findFirst().orElse(null);
    }
}
