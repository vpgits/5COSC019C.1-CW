package com.westminster.dao;

import com.westminster.model.Electronics;
import com.westminster.model.ProductType;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class ProductDaoTest {

    @BeforeAll
    static void setUp() throws ProductDao.ProductDaoException {

    }
    @AfterAll
    static void tearDown() throws ProductDao.ProductDaoException {

    }
    @Test
    void addProduct() throws ProductDao.ProductDaoException {

    }

    @Test
    void deleteProduct() throws ProductDao.ProductDaoException {

    }

    @Test
    void doesExist() throws ProductDao.ProductDaoException {
        Assertions.assertTrue(ProductDao.doesExist("E002"));
    }

    @Test
    void isFull() {
    }

    @Test
    void updateProduct() {
    }

    @Test
    void getProductFromHashmap() {
    }
}