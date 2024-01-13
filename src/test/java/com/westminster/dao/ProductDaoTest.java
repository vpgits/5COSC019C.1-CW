package com.westminster.dao;

import org.junit.jupiter.api.Test;
import com.westminster.model.*;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

/**
 * This class contains unit tests for the ProductDao class.
 */
class ProductDaoTest {
    // instance variables
    static ProductDao productDao;

    /**
     * This method is run before all tests. Sets up the ProductDao instance.
     */
    @BeforeAll
    static void setUp() {
        productDao = new ProductDao();

    }

    /**
     * This method is run after all tests.
     */
    @AfterAll
    static void tearDown() {
    }

    /**
     * This method is run before each test. Adds a product to the database.
     */
    @BeforeEach
    void init() {
    }

    /**
     * This method is run after each test. Deletes the product from the database.
     */
    @AfterEach
    void cleanUp() {


    }

    /**
     * This method tests the addProduct method.
     */
    @Test
    void addProduct() {
        Product product1 = new Clothing("Test002", "T-Shirt", 10, 10.0, "Nike", ClothingSize.S, "Red");
        productDao.addProduct(product1);
        assertTrue(productDao.doesProductExist(product1.getProductID()));
        productDao.deleteProduct(product1.getProductID());

    }
    @Test
    void addFiveProducts(){
        Product product1 = new Clothing("Test002", "T-Shirt", 10, 10.0, "Nike", ClothingSize.S, "Red");
        Product product2 = new Clothing("Test003", "T-Shirt", 10, 10.0, "Nike", ClothingSize.S, "Red");
        Product product3 = new Clothing("Test004", "T-Shirt", 10, 10.0, "Nike", ClothingSize.S, "Red");
        Product product4 = new Clothing("Test005", "T-Shirt", 10, 10.0, "Nike", ClothingSize.S, "Red");
        Product product5 = new Clothing("Test006", "T-Shirt", 10, 10.0, "Nike", ClothingSize.S, "Red");
        productDao.addProduct(product1);
        productDao.addProduct(product2);
        productDao.addProduct(product3);
        productDao.addProduct(product4);
        productDao.addProduct(product5);
        assertTrue(productDao.doesProductExist(product1.getProductID()));
        assertTrue(productDao.doesProductExist(product2.getProductID()));
        assertTrue(productDao.doesProductExist(product3.getProductID()));
        assertTrue(productDao.doesProductExist(product4.getProductID()));
        assertTrue(productDao.doesProductExist(product5.getProductID()));

    }

    @Test
    void deleteFiveProducts(){
        Product product1 = new Clothing("Test002", "T-Shirt", 10, 10.0, "Nike", ClothingSize.S, "Red");
        Product product2 = new Clothing("Test003", "T-Shirt", 10, 10.0, "Nike", ClothingSize.S, "Red");
        Product product3 = new Clothing("Test004", "T-Shirt", 10, 10.0, "Nike", ClothingSize.S, "Red");
        Product product4 = new Clothing("Test005", "T-Shirt", 10, 10.0, "Nike", ClothingSize.S, "Red");
        Product product5 = new Clothing("Test006", "T-Shirt", 10, 10.0, "Nike", ClothingSize.S, "Red");
        productDao.deleteProduct(product1.getProductID());
        productDao.deleteProduct(product2.getProductID());
        productDao.deleteProduct(product3.getProductID());
        productDao.deleteProduct(product4.getProductID());
        productDao.deleteProduct(product5.getProductID());
        assertFalse(productDao.doesProductExist(product1.getProductID()));
        assertFalse(productDao.doesProductExist(product2.getProductID()));
        assertFalse(productDao.doesProductExist(product3.getProductID()));
        assertFalse(productDao.doesProductExist(product4.getProductID()));
        assertFalse(productDao.doesProductExist(product5.getProductID()));
    }

    /**
     * This method tests the getProduct method.
     */
    @Test
    void deleteProduct() {
        Product product1 = new Clothing("CLOTH998", "T-Shirt", 10, 10.0, "Nike", ClothingSize.S, "Red");
        productDao.addProduct(product1);
        productDao.deleteProduct(product1.getProductID());
        assertFalse(productDao.doesProductExist(product1.getProductID()));
    }

    /**
     * This method tests the getProduct method.
     */
    @Test
    void updateStock() {
        Product product = new Clothing("TEST0001", "T-Shirt", 10, 10.0, "Nike", ClothingSize.L, "Red");
        productDao.addProduct(product);
        productDao.updateStock(product.getProductID(), 20);
        assertEquals(20, productDao.getCurrentStock(product.getProductID()));
        productDao.deleteProduct(product.getProductID());
    }

    /**
     * This method tests the getProduct method.
     */
    @Test
    void doesProductExist() {
        Product product = new Clothing("TEST0001", "T-Shirt", 10, 10.0, "Nike", ClothingSize.L, "Red");
        productDao.addProduct(product);
        assertTrue(productDao.doesProductExist(product.getProductID()));
        productDao.deleteProduct(product.getProductID());
    }

    /**
     * This method tests the getProduct method.
     */
    @Test
    void getCurrentStock() {
        Product product = new Clothing("TEST0001", "T-Shirt", 10, 10.0, "Nike", ClothingSize.L, "Red");
        productDao.addProduct(product);
        assertEquals(10, productDao.getCurrentStock(product.getProductID()));
        productDao.deleteProduct(product.getProductID());
    }

    /**
     * This method tests the getProduct method.
     */
    @Test
    void getAllProducts() {
        assertEquals(productDao.getAllProducts().size(), productDao.getProductCount());
    }

    /**
     * This method tests the getProduct method.
     */
    @Test
    void saveProducts() {
        productDao.loadProducts();
        productDao.saveProducts();
        String relativePath = "src/main/resources/database/products.ser";
        Path path = Paths.get(relativePath);
        assertTrue(Files.exists(path));
    }

    /**
     * This method tests the getProduct method.
     */
    @Test
    void loadProducts() {
        productDao.loadProducts();
        String relativePath = "src/main/resources/database/products.ser";
        Path path = Paths.get(relativePath);
        assertTrue(Files.exists(path));
    }
}