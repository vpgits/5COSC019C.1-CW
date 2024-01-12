package com.westminster.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

class ClothingTest {
    private Clothing clothing;

    @BeforeEach
    void setUp() {
        // Create a new Clothing object for each test
        clothing = new Clothing("CLOTHTEST", "Test Clothing", 10, 10.00, "Test Brand", ClothingSize.M, "Test Color");
    }

    @Test
    void getBrand() {
        // Test the getBrand() method
        assertEquals("Test Brand", clothing.getBrand());
    }

    @Test
    void setBrand() {
        // Test the setBrand() method
        clothing.setBrand("Test Brand 2");
        assertEquals("Test Brand 2", clothing.getBrand());
    }

    @Test
    void getSize() {
        // Test the getSize() method
        assertEquals(ClothingSize.M, clothing.getSize());
    }

    @Test
    void setSize() {
        // Test the setSize() method
        clothing.setSize(ClothingSize.L);
        assertEquals(ClothingSize.L, clothing.getSize());
    }

    @Test
    void getColor() {
        // Test the getColor() method
        assertEquals("Test Color", clothing.getColor());
    }

    @Test
    void setColor() {
        // Test the setColor() method
        clothing.setColor("Test Color 2");
        assertEquals("Test Color 2", clothing.getColor());
    }

    @Test
    void testToString() {
        // Test the toString() method
        assertEquals(" brand : Test Brand\n" +
                " size : M\n" +
                " type : Clothing\n" +
                " color : Test Color\n", clothing.toString());
    }

    @Test
    void testToStringConsole() {
        // Test the toStringConsole() method
        assertEquals("Product ID : CLOTHTEST\n" +
                "- Product Name : Test Clothing\n" +
                "- Available Items : 10\n" +
                "- Price : 10.0£\n" +
                "- Brand : Test Brand\n" +
                "- Size : M\n" +
                "- Type : Clothing\n"+
                "- Color : Test Color\n", clothing.toStringConsole());
    }
}