package com.westminster.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ElectronicsTest {
    private Electronics electronics;

    @BeforeEach
    void setUp() {
        // Create an instance of Electronics for testing
        electronics = new Electronics("ELECTRONICTEST", "Test Electronics", 10, 10.00, "Test Brand", "Test Model", String.valueOf(ProductType.Electronics), 1);
    }

    @Test
    void testToString() {
        // Verify that the toString() method returns the expected string representation
        assertEquals(" brand : Test Brand\n" +
                " model : Test Model\n" +
                " type : Electronics\n" +
                " warranty : 1", electronics.toString());
    }

    @Test
    void toStringConsole() {
        // Verify that the toStringConsole() method returns the expected string representation
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
        // Verify that the getBrand() method returns the expected brand value
        assertEquals("Test Brand", electronics.getBrand());
    }

    @Test
    void setBrand() {
        // Set a new brand value using the setBrand() method
        electronics.setBrand("Test Brand 2");
        // Verify that the brand value has been updated
        assertEquals("Test Brand 2", electronics.getBrand());
    }

    @Test
    void getModel() {
        // Verify that the getModel() method returns the expected model value
        assertEquals("Test Model", electronics.getModel());
    }

    @Test
    void setModel() {
        // Set a new model value using the setModel() method
        electronics.setModel("Test Model 2");
        // Verify that the model value has been updated
        assertEquals("Test Model 2", electronics.getModel());
    }

    @Test
    void getElectricalProductType() {
        // Verify that the getElectricalProductType() method returns the expected electrical product type value
        assertEquals(String.valueOf(ProductType.Electronics), electronics.getElectricalProductType());
    }

    @Test
    void setElectricalProductType() {
        // Set a new electrical product type value using the setElectricalProductType() method
        electronics.setElectricalProductType(String.valueOf(ProductType.Clothing));
        // Verify that the electrical product type value has been updated
        assertEquals(String.valueOf(ProductType.Clothing), electronics.getElectricalProductType());
    }

    @Test
    void getWarranty() {
        // Verify that the getWarranty() method returns the expected warranty value
        assertEquals(1, electronics.getWarranty());
    }

    @Test
    void setWarranty() {
        // Set a new warranty value using the setWarranty() method
        electronics.setWarranty(2);
        // Verify that the warranty value has been updated
        assertEquals(2, electronics.getWarranty());
    }
}