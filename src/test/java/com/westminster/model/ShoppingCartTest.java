package com.westminster.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;

class ShoppingCartTest {
    private ShoppingCart shoppingCart;

    @BeforeEach
    void setUp() {
        shoppingCart = new ShoppingCart("TESTUSER");
    }

    @Test
    void addProduct() {
        // Create a test product
        Product product = new Electronics("ELECTRONICTEST", "Test Electronics", 10, 10.00, "Test Brand", "Test Model", String.valueOf(ProductType.Electronics), 1);
        // Add the product to the shopping cart
        shoppingCart.addProduct(product);
        // Verify that the product is added successfully
        assertEquals(1, shoppingCart.getProducts().size());
    }

    @Test
    void removeProduct() {
        // Create a test product
        Product product = new Electronics("ELECTRONICTEST", "Test Electronics", 10, 10.00, "Test Brand", "Test Model", String.valueOf(ProductType.Electronics), 1);
        // Add the product to the shopping cart
        shoppingCart.addProduct(product);
        // Remove the product from the shopping cart
        shoppingCart.removeProduct(product.getProductID());
        // Verify that the product is removed successfully
        assertEquals(0, shoppingCart.getProducts().size());
    }

    @Test
    void updateProduct() {
        // Create a test product
        Product product = new Electronics("ELECTRONICTEST", "Test Electronics", 10, 10.00, "Test Brand", "Test Model", String.valueOf(ProductType.Electronics), 1);
        // Add the product to the shopping cart
        shoppingCart.addProduct(product);
        // Update the product in the shopping cart
        product = new Electronics("ELECTRONICTEST", "Test Electronics ", 8, 10.00, "Test Brand", "Test Model", String.valueOf(ProductType.Electronics), 1);
        shoppingCart.updateProduct(product);
        // Verify that the product is updated successfully
        assertEquals(8, shoppingCart.getProducts().get(0).getAvailableItems());
    }

    @Test
    void getProducts() {
        // Create test products
        Product product1 = new Electronics("ELECTRONICTEST", "Test Electronics", 10, 10.00, "Test Brand", "Test Model", String.valueOf(ProductType.Electronics), 1);
        Product product2 = new Clothing("CLOTHINGTEST", "Test Clothing", 10, 10.00, "Test Brand", ClothingSize.L, "Test Color");
        // Create a list of expected products
        ArrayList<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        // Add the products to the shopping cart
        shoppingCart.addProduct(product1);
        shoppingCart.addProduct(product2);
        // Verify that the products in the shopping cart match the expected products
        assertEquals(products, shoppingCart.getProducts());
    }

    @Test
    void getTotalPrice() {
        // Create test products
        Product product1 = new Electronics("ELECTRONICTEST", "Test Electronics", 10, 10.00, "Test Brand", "Test Model", String.valueOf(ProductType.Electronics), 1);
        Product product2 = new Clothing("CLOTHINGTEST", "Test Clothing", 10, 10.00, "Test Brand", ClothingSize.L, "Test Color");
        // Add the products to the shopping cart
        shoppingCart.addProduct(product1);
        shoppingCart.addProduct(product2);
        // Verify that the total price of the shopping cart is calculated correctly
        assertEquals(200.00, shoppingCart.getTotalPrice());
    }

    @Test
    void getDiscount() {
        // Create test products
        Product product1 = new Electronics("ELECTRONICTEST", "Test Electronics", 10, 10.00, "Test Brand", "Test Model", String.valueOf(ProductType.Electronics), 1);
        Product product2 = new Clothing("CLOTHINGTEST", "Test Clothing", 10, 10.00, "Test Brand", ClothingSize.L, "Test Color");
        // Add the products to the shopping cart
        shoppingCart.addProduct(product1);
        shoppingCart.addProduct(product2);
        // Verify that the discount of the shopping cart is calculated correctly
        assertEquals(20.00, shoppingCart.getDiscount(0.1));
    }

    @Test
    void clear() {
        // Create test products
        Product product1 = new Electronics("ELECTRONICTEST", "Test Electronics", 10, 10.00, "Test Brand", "Test Model", String.valueOf(ProductType.Electronics), 1);
        Product product2 = new Clothing("CLOTHINGTEST", "Test Clothing", 10, 10.00, "Test Brand", ClothingSize.L, "Test Color");
        // Add the products to the shopping cart
        shoppingCart.addProduct(product1);
        shoppingCart.addProduct(product2);
        // Clear the shopping cart
        shoppingCart.clear();
        // Verify that the shopping cart is empty
        assertEquals(0, shoppingCart.getProducts().size());
    }

    @Test
    void getUuid() {
        // Create two shopping carts with the same username
        ShoppingCart shoppingCart1 = new ShoppingCart("TESTUSER");
        ShoppingCart shoppingCart2 = new ShoppingCart("TESTUSER");
        // Verify that the UUIDs of the shopping carts are different
        assertNotEquals(shoppingCart1.getUuid(), shoppingCart2.getUuid());
    }

    @Test
    void getUsername() {
        // Verify that the username of the shopping cart is correct
        assertEquals("TESTUSER", shoppingCart.getUsername());
    }

    @Test
    void isProductInTheCart() {
        // Create a test product
        Product product = new Electronics("ELECTRONICTEST", "Test Electronics", 10, 10.00, "Test Brand", "Test Model", String.valueOf(ProductType.Electronics), 1);
        // Add the product to the shopping cart
        shoppingCart.addProduct(product);
        // Verify that the product is in the shopping cart
        assertTrue(shoppingCart.isProductInTheCart(product.getProductID()));
    }

    @Test
    void getProductFromTheCart() {
        // Create a test product
        Product product = new Electronics("ELECTRONICTEST", "Test Electronics", 10, 10.00, "Test Brand", "Test Model", String.valueOf(ProductType.Electronics), 1);
        // Add the product to the shopping cart
        shoppingCart.addProduct(product);
        // Get the product from the shopping cart
        assertEquals(product, shoppingCart.getProductFromTheCart(product.getProductID()));
    }
}