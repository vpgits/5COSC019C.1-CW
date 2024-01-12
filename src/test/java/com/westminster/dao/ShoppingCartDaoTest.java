package com.westminster.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.westminster.model.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

class ShoppingCartDaoTest {
    // instance variables
    private static ShoppingCartDao shoppingCartDao;
    private ShoppingCart shoppingCart;

    /**
     * This method is run before all tests. Sets up the ProductDao instance.
     */
    @BeforeAll
    static void init() {
        shoppingCartDao = new ShoppingCartDao();
    }

    /**
     * This method is run after all tests.
     */
    @AfterAll
    static void cleanUp() {

    }

    /**
     * This method is run before each test. creates a shoppingCart under testUser and adds a product.
     */
    @BeforeEach
    void setUp() {
        shoppingCart = shoppingCartDao.createShoppingCart("testUser");
        Product product = new Clothing("CLOTH999", "T-Shirt", 10, 10.0, "Nike", ClothingSize.S, "Red");
        ProductDao productDao = new ProductDao();
        productDao.addProduct(product);
        assertTrue(productDao.doesProductExist("CLOTH999"));

    }

    /**
     * This method is run after each test. removes the shoppingCart from the database and deletes the product.
     */
    @AfterEach
    void tearDown() {
        shoppingCartDao.clearShoppingCart("testUser");
        shoppingCartDao.removeShoppingCart("testUser");
        ProductDao productDao = new ProductDao();
        productDao.deleteProduct("CLOTH999");

    }

    /**
     * This method tests the createShoppingCart method.
     */
    @Test
    void createShoppingCart() {
        // Test if a shopping cart is created successfully

        assertNotNull(shoppingCart);
        assertEquals("testUser", shoppingCart.getUsername());
    }

    /**
     * This method tests the removeShoppingCart method.
     */
    @Test
    void addProductToShoppingCart() {
        // Test adding a product to the shopping cart

        shoppingCartDao.addProductToShoppingCart("testUser", "CLOTH999", 1);
        ShoppingCart shoppingCart = shoppingCartDao.getShoppingCart("testUser");
        assertNotNull(shoppingCart);
        assertEquals(1, shoppingCart.getProducts().size());
    }

    /**
     * This method tests the removeShoppingCart method.
     */
    @Test
    void removeProductFromShoppingCart() {
        // Test removing a product from the shopping cart

        shoppingCartDao.addProductToShoppingCart("testUser", "CLOTH999", 1);
        shoppingCartDao.removeProductFromShoppingCart("testUser", "CLOTH999", 1);
        ShoppingCart shoppingCart = shoppingCartDao.getShoppingCart("testUser");
        assertNotNull(shoppingCart);
        assertEquals(0, shoppingCart.getProducts().size());
    }

    /**
     * This method tests the removeShoppingCart method.
     */
    @Test
    void updateProduct() {
        // Test updating the quantity of a product in the shopping cart

        shoppingCartDao.addProductToShoppingCart("testUser", "CLOTH999", 1);
        shoppingCartDao.updateProduct("testUser", "CLOTH999", 2);
        ShoppingCart shoppingCart = shoppingCartDao.getShoppingCart("testUser");
        assertNotNull(shoppingCart);
        assertEquals(3, shoppingCart.getProductFromTheCart("CLOTH999").getAvailableItems());
    }

    /**
     * This method tests the removeShoppingCart method.
     */
    @Test
    void getProductsInShoppingCart() {
        // Test getting the products in the shopping cart

        shoppingCartDao.addProductToShoppingCart("testUser", "CLOTH999", 2);
        ShoppingCart shoppingCart = shoppingCartDao.getShoppingCart("testUser");
        assertNotNull(shoppingCart);
        assertEquals(1, shoppingCart.getProducts().size());
    }

    /**
     * This method tests the removeShoppingCart method.
     */
    @Test
    void getTotalPrice() {
        // Test getting the total price of the shopping cart

        shoppingCartDao.addProductToShoppingCart("testUser", "CLOTH999", 4);
        double totalPrice = shoppingCartDao.getTotalPrice("testUser");
        assertEquals(40.0, totalPrice);
    }

    /**
     * This method tests the removeShoppingCart method.
     */
    @Test
    void getDiscount() {
        // Test getting the discount for the shopping cart

        shoppingCartDao.addProductToShoppingCart("testUser", "CLOTH999", 1);
        double discount = shoppingCartDao.getDiscount("testUser", 0.1);
        assertEquals(1.0, discount);
    }

    /**
     * This method tests the removeShoppingCart method.
     */
    @Test
    void getThreeItemsInSameCategoryDiscount() {
        // Test getting the discount for having three items in the same category

        shoppingCartDao.addProductToShoppingCart("testUser", "CLOTH999", 3);
        double discount = shoppingCartDao.getThreeItemsInSameCategoryDiscount("testUser");
        assertEquals(6.0, discount);
    }

    /**
     * This method tests the removeShoppingCart method.
     */
    @Test
    void getFirstPurchaseDiscount() {
        // Test getting the discount for the first purchase

        double discount = shoppingCartDao.getfirstPurchaseDiscount("testUser");
        assertEquals(0.0, discount);
    }

    /**
     * This method tests the removeShoppingCart method.
     */
    @Test
    void getFinalPrice() {
        // Test getting the final price of the shopping cart

        shoppingCartDao.addProductToShoppingCart("testUser", "CLOTH999", 3);
        double finalPrice = shoppingCartDao.getFinalPrice("testUser");
        assertEquals(21.0, finalPrice);
    }

    /**
     * This method tests the removeShoppingCart method.
     */
    @Test
    void clearShoppingCart() {
        // Test clearing the shopping cart

        shoppingCartDao.addProductToShoppingCart("testUser", "CLOTH999", 1);
        shoppingCartDao.clearShoppingCart("testUser");
        shoppingCartDao.updateShoppingCart("testUser", shoppingCart);
        ShoppingCart shoppingCart = shoppingCartDao.getShoppingCart("testUser");
        assertNotNull(shoppingCart);
        assertEquals(0, shoppingCart.getProducts().size());
    }

    /**
     * This method tests the removeShoppingCart method.
     */
    @Test
    void getCurrentProductStock() {
        // Test getting the current stock of a product in the shopping cart

        shoppingCartDao.addProductToShoppingCart("testUser", "CLOTH999", 1);
        int currentStock = shoppingCartDao.getCurrentProductStock("testUser", "CLOTH999");
        assertEquals(1, currentStock);
    }
}