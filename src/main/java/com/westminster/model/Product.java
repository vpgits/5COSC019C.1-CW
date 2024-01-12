package com.westminster.model;

import java.io.Serializable;

public abstract class Product implements Serializable {
    // instance variables
    private String productID; // ID of the product
    public static final int PRODUCTIDLENGTH = 7; // length of the product ID
    private String productName; // name of the product
    private int availableItems; // number of available items
    private double price; // price of the product
    ProductType type; // type of the product

    // default constructor
    public Product() {
        super();
    }

    // parameterized constructor
    public Product(String productID, String productName, int availableItems, double price, ProductType type) {
        this.productID = productID;
        this.productName = productName;
        this.availableItems = availableItems;
        this.price = price;
        this.type = type;
    }

    // getter and setter methods
    public String getProductID() {
        return productID;
    }


    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getAvailableItems() {
        return availableItems;
    }

    public void setAvailableItems(int availableItems) {
        this.availableItems = availableItems;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    // toString method
    @Override
    public String toString() {
        return
                "Product ID : " + productID + '\n' +
                "- Product Name : " + productName + '\n' +
                "- Available Items : " + availableItems + '\n' +
                "- Price : " + price + "£\n";
    }

    // abstract methods
    public abstract String toStringTable();

    public abstract String toStringConsole();
}
