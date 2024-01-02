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
        Product product = new Electronics("ELECTRONICTEST", "Test Electronics", 10, 10.00, "Test Brand", "Test Model", String.valueOf(ProductType.Electronics), 1);
        shoppingCart.addProduct(product);
        assertEquals(1, shoppingCart.getProducts().size());
    }

    @Test
    void removeProduct() {
        Product product = new Electronics("ELECTRONICTEST", "Test Electronics", 10, 10.00, "Test Brand", "Test Model", String.valueOf(ProductType.Electronics), 1);
        shoppingCart.addProduct(product);
        shoppingCart.removeProduct(product.getProductID());
        assertEquals(0, shoppingCart.getProducts().size());
    }

    @Test
    void updateProduct() {
        Product product = new Electronics("ELECTRONICTEST", "Test Electronics", 10, 10.00, "Test Brand", "Test Model", String.valueOf(ProductType.Electronics), 1);
        shoppingCart.addProduct(product);
        product = new Electronics("ELECTRONICTEST", "Test Electronics ", 8, 10.00, "Test Brand", "Test Model", String.valueOf(ProductType.Electronics), 1);
        shoppingCart.updateProduct(product);
        assertEquals(8, shoppingCart.getProducts().get(0).getAvailableItems());
    }

    @Test
    void getProducts() {
        Product product1 = new Electronics("ELECTRONICTEST", "Test Electronics", 10, 10.00, "Test Brand", "Test Model", String.valueOf(ProductType.Electronics), 1);
        Product product2 = new Clothing("CLOTHINGTEST", "Test Clothing", 10, 10.00, "Test Brand", ClothingSize.L, "Test Color");
        ArrayList<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        shoppingCart.addProduct(product1);
        shoppingCart.addProduct(product2);
        assertEquals(products, shoppingCart.getProducts());

    }

    @Test
    void getTotalPrice() {
        Product product1 = new Electronics("ELECTRONICTEST", "Test Electronics", 10, 10.00, "Test Brand", "Test Model", String.valueOf(ProductType.Electronics), 1);
        Product product2 = new Clothing("CLOTHINGTEST", "Test Clothing", 10, 10.00, "Test Brand", ClothingSize.L, "Test Color");
        shoppingCart.addProduct(product1);
        shoppingCart.addProduct(product2);
        assertEquals(200.00, shoppingCart.getTotalPrice());
    }

    @Test
    void getDiscount() {
        Product product1 = new Electronics("ELECTRONICTEST", "Test Electronics", 10, 10.00, "Test Brand", "Test Model", String.valueOf(ProductType.Electronics), 1);
        Product product2 = new Clothing("CLOTHINGTEST", "Test Clothing", 10, 10.00, "Test Brand", ClothingSize.L, "Test Color");
        shoppingCart.addProduct(product1);
        shoppingCart.addProduct(product2);
        assertEquals(20.00, shoppingCart.getDiscount(0.1));
    }

    @Test
    void clear() {
        Product product1 = new Electronics("ELECTRONICTEST", "Test Electronics", 10, 10.00, "Test Brand", "Test Model", String.valueOf(ProductType.Electronics), 1);
        Product product2 = new Clothing("CLOTHINGTEST", "Test Clothing", 10, 10.00, "Test Brand", ClothingSize.L, "Test Color");
        shoppingCart.addProduct(product1);
        shoppingCart.addProduct(product2);
        shoppingCart.clear();
        assertEquals(0, shoppingCart.getProducts().size());
    }

    @Test
    void getUuid() {
        ShoppingCart shoppingCart1 = new ShoppingCart("TESTUSER");
        ShoppingCart shoppingCart2 = new ShoppingCart("TESTUSER");
        assertNotEquals(shoppingCart1.getUuid(), shoppingCart2.getUuid());
    }

    @Test
    void getUsername() {
        assertEquals("TESTUSER", shoppingCart.getUsername());
    }

    @Test
    void isProductInTheCart() {
        Product product = new Electronics("ELECTRONICTEST", "Test Electronics", 10, 10.00, "Test Brand", "Test Model", String.valueOf(ProductType.Electronics), 1);
        shoppingCart.addProduct(product);
        assertTrue(shoppingCart.isProductInTheCart(product.getProductID()));
    }

    @Test
    void getProductFromTheCart() {
        Product product = new Electronics("ELECTRONICTEST", "Test Electronics", 10, 10.00, "Test Brand", "Test Model", String.valueOf(ProductType.Electronics), 1);
        shoppingCart.addProduct(product);
        assertEquals(product, shoppingCart.getProductFromTheCart(product.getProductID()));
    }
}