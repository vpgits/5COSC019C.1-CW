package com.westminster.dao;

import org.junit.jupiter.api.Test;

import com.westminster.model.Clothing;
import com.westminster.model.ClothingSize;
import com.westminster.model.Product;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

class ProductDaoTest {
    static ProductDao productDao;
    static Product product;



    @BeforeAll
    static void setUp() {
        productDao = new ProductDao();
    }

    @AfterAll
    static void tearDown() {
        
    }
    @BeforeEach
     void init() {
        product = new Clothing("CLOTH999", "T-Shirt", 10, 10.0, "Nike", ClothingSize.S, "Red");
        productDao.addProduct(product);
    
    }
    @AfterEach
     void cleanUp() {
        productDao.deleteProduct(product.getProductID());
    }

    @Test
    void addProduct() {
        Product product1 = new Clothing("CLOTH998", "T-Shirt", 10, 10.0, "Nike", ClothingSize.S, "Red");
        productDao.addProduct(product1);
        assertTrue(productDao.doesProductExist(product1.getProductID()));
        productDao.deleteProduct(product1.getProductID());
    }

    @Test
    void deleteProduct() {
        Product product1 = new Clothing("CLOTH998", "T-Shirt", 10, 10.0, "Nike", ClothingSize.S, "Red");
        productDao.addProduct(product1);
        productDao.deleteProduct(product1.getProductID());
        assertFalse(productDao.doesProductExist(product1.getProductID()));
    }

    @Test
    void updateStock() {
        productDao.updateStock(product.getProductID(), 20);
        assertEquals(productDao.getCurrentStock(product.getProductID()), 20);
    }

    @Test
    void doesProductExist() {
        assertTrue(productDao.doesProductExist(product.getProductID()));
    }


    @Test
    void getCurrentStock() {
        assertEquals(productDao.getCurrentStock(product.getProductID()), 10);
    }
    @Test
    void getAllProducts() {
        assertEquals(productDao.getAllProducts().size(), productDao.getProductCount());
    }

    @Test
    void saveProducts() {
        productDao.saveProducts();
        String relativePath = "src/main/resources/database/products.ser";
        Path path = Paths.get(relativePath);
        assertTrue(Files.exists(path));
    }

    @Test
    void loadProducts() {
        productDao.loadProducts();
        String relativePath = "src/main/resources/database/products.ser";
        Path path = Paths.get(relativePath);
        assertTrue(Files.exists(path));
    }
}