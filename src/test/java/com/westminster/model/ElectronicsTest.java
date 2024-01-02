package com.westminster.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;

class ElectronicsTest {
    private Electronics electronics;

    @BeforeEach
    void setUp() {
        electronics = new Electronics("ELECTRONICTEST", "Test Electronics", 10, 10.00, "Test Brand", "Test Model", String.valueOf(ProductType.Electronics), 1);
    }

    @Test
    void testToString() {
        assertEquals(" brand : Test Brand\n" +
                " model : Test Model\n" +
                " type : Electronics\n" +
                " warranty : 1", electronics.toString());
    }

    @Test
    void toStringConsole() {
        assertEquals("Product ID : ELECTRONICTEST\n" +
                "- Product Name : Test Electronics\n" +
                "- Available Items : 10\n" +
                "- Price : 10.0£\n" +
                "- brand : Test Brand\n" +
                "- model : Test Model\n" +
                "- type : Electronics\n" +
                "- warranty : 1\n", electronics.toStringConsole());
    }

    @Test
    void getBrand() {
        assertEquals("Test Brand", electronics.getBrand());
    }

    @Test
    void setBrand() {
        electronics.setBrand("Test Brand 2");
        assertEquals("Test Brand 2", electronics.getBrand());
    }

    @Test
    void getModel() {
        assertEquals("Test Model", electronics.getModel());
    }

    @Test
    void setModel() {
        electronics.setModel("Test Model 2");
        assertEquals("Test Model 2", electronics.getModel());
    }

    @Test
    void getElectricalProductType() {
        assertEquals(String.valueOf(ProductType.Electronics), electronics.getElectricalProductType());
    }

    @Test
    void setElectricalProductType() {
        electronics.setElectricalProductType(String.valueOf(ProductType.Clothing));
        assertEquals(String.valueOf(ProductType.Clothing), electronics.getElectricalProductType());
    }

    @Test
    void getWarranty() {
        assertEquals(1, electronics.getWarranty());
    }

    @Test
    void setWarranty() {
        electronics.setWarranty(2);
        assertEquals(2, electronics.getWarranty());
    }
}