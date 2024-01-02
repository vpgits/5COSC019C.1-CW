package com.westminster.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

class ClothingTest {
    private Clothing clothing;
    @BeforeEach
    void setUp() {
        clothing = new Clothing("CLOTHTEST", "Test Clothing", 10, 10.00, "Test Brand", ClothingSize.M, "Test Color");
    }

    @Test
    void getBrand() {
        assertEquals("Test Brand", clothing.getBrand());
        
    }

    @Test
    void setBrand() {
        clothing.setBrand("Test Brand 2");
        assertEquals("Test Brand 2", clothing.getBrand());
    }

    @Test
    void getSize() {
        assertEquals(ClothingSize.M, clothing.getSize());
    }

    @Test
    void setSize() {
        clothing.setSize(ClothingSize.L);
        assertEquals(ClothingSize.L, clothing.getSize());
    }

    @Test
    void getClothingType() {
        assertEquals(ProductType.Clothing, clothing.getClothingType());
    }

    @Test
    void setClothingType() {
        clothing.setClothingType(ProductType.Electronics);
        assertEquals(ProductType.Electronics, clothing.getClothingType());
    }

    @Test
    void getColor() {
        assertEquals("Test Color", clothing.getColor());
    }

    @Test
    void setColor() {
        clothing.setColor("Test Color 2");
        assertEquals("Test Color 2", clothing.getColor());
    }

    @Test
    void testToString() {
        assertEquals(" brand : Test Brand\n" +
                " size : M\n" +
                " type : Clothing\n" +
                " color : Test Color\n", clothing.toString());
    }

    @Test
    void testToStringConsole() {
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