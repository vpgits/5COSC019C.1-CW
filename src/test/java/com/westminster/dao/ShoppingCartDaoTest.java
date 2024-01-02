package com.westminster.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.westminster.model.Clothing;
import com.westminster.model.ClothingSize;
import com.westminster.model.Product;
import com.westminster.model.ShoppingCart;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

class ShoppingCartDaoTest {
    private static ShoppingCartDao shoppingCartDao;
    private ShoppingCart shoppingCart;

    @BeforeAll
    static void init()  {
        shoppingCartDao = new ShoppingCartDao();

    }

    @AfterAll
    static void cleanUp() {

    }


    @BeforeEach
    void setUp() {
        shoppingCart = shoppingCartDao.createShoppingCart("testUser");
        Product product = new Clothing("CLOTH999", "T-Shirt", 10, 10.0, "Nike", ClothingSize.S, "Red");
        ProductDao productDao = new ProductDao();
        productDao.addProduct(product);
        assertTrue(productDao.doesProductExist("CLOTH999"));

    }

    @AfterEach
    void tearDown() {
        shoppingCartDao.clearShoppingCart("testUser");
        shoppingCartDao.removeShoppingCart("testUser");
        ProductDao productDao = new ProductDao();
        productDao.deleteProduct("CLOTH999");

    }


    @Test
    void createShoppingCart() {
        // Test if a shopping cart is created successfully
       
        assertNotNull(shoppingCart);
        assertEquals("testUser", shoppingCart.getUsername());
    }

    @Test
    void addProductToShoppingCart() {
        // Test adding a product to the shopping cart

        shoppingCartDao.addProductToShoppingCart("testUser", "CLOTH999", 1);
        ShoppingCart shoppingCart = shoppingCartDao.getShoppingCart("testUser");
        assertNotNull(shoppingCart);
        assertEquals(1, shoppingCart.getProducts().size());
    }

    @Test
    void removeProductFromShoppingCart() {
        // Test removing a product from the shopping cart

        shoppingCartDao.addProductToShoppingCart("testUser", "CLOTH999", 1);
        shoppingCartDao.removeProductFromShoppingCart("testUser", "CLOTH999", 1);
        ShoppingCart shoppingCart = shoppingCartDao.getShoppingCart("testUser");
        assertNotNull(shoppingCart);
        assertEquals(0, shoppingCart.getProducts().size());
    }

    @Test
    void updateProduct() {
        // Test updating the quantity of a product in the shopping cart

        shoppingCartDao.addProductToShoppingCart("testUser", "CLOTH999", 1);
        shoppingCartDao.updateProduct("testUser", "CLOTH999", 2);
        ShoppingCart shoppingCart = shoppingCartDao.getShoppingCart("testUser");
        assertNotNull(shoppingCart);
        assertEquals(3, shoppingCart.getProductFromTheCart("CLOTH999").getAvailableItems());
    }

    @Test
    void getProductsInShoppingCart() {
        // Test getting the products in the shopping cart

        shoppingCartDao.addProductToShoppingCart("testUser", "CLOTH999", 2);
        ShoppingCart shoppingCart = shoppingCartDao.getShoppingCart("testUser");
        assertNotNull(shoppingCart);
        assertEquals(1, shoppingCart.getProducts().size());
    }

    @Test
    void getTotalPrice() {
        // Test getting the total price of the shopping cart

        shoppingCartDao.addProductToShoppingCart("testUser", "CLOTH999", 4);
        double totalPrice = shoppingCartDao.getTotalPrice("testUser");
        assertEquals(40.0, totalPrice);
    }

    @Test
    void getDiscount() {
        // Test getting the discount for the shopping cart

        shoppingCartDao.addProductToShoppingCart("testUser", "CLOTH999", 1);
        double discount = shoppingCartDao.getDiscount("testUser", 0.1);
        assertEquals(1.0, discount);
    }

    @Test
    void getThreeItemsInSameCategoryDiscount() {
        // Test getting the discount for having three items in the same category

        shoppingCartDao.addProductToShoppingCart("testUser", "CLOTH999", 3);
        double discount = shoppingCartDao.getThreeItemsInSameCategoryDiscount("testUser");
        assertEquals(6.0, discount);
    }

    @Test
    void getfirstPurchaseDiscount() {
        // Test getting the discount for the first purchase

        double discount = shoppingCartDao.getfirstPurchaseDiscount("testUser");
        assertEquals(0.0, discount);
    }

    @Test
    void getFinalPrice() {
        // Test getting the final price of the shopping cart

        shoppingCartDao.addProductToShoppingCart("testUser", "CLOTH999", 3);
        double finalPrice = shoppingCartDao.getFinalPrice("testUser");
        assertEquals(21.0, finalPrice);
    }

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

    @Test
    void getCurrentProductStock() {
        // Test getting the current stock of a product in the shopping cart

        shoppingCartDao.addProductToShoppingCart("testUser", "CLOTH999", 1);
        int currentStock = shoppingCartDao.getCurrentProductStock("testUser", "CLOTH999");
        assertEquals(1, currentStock);
    }
}