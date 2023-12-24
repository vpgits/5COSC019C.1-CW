package com.westminster.dao;

import org.junit.jupiter.api.*;

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
        Assertions.assertTrue(ProductDao.doesProductExist("E002"));
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