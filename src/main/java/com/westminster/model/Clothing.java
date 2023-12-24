package com.westminster.model;

import java.io.Serializable;

public class Clothing extends Product implements Serializable {
    //instance variables
    private String brand; //brand of the clothing product
    private ClothingSize size; //size of the clothing product(S/M/L/XL)
    private String color; //color of the clothing product

    //default constructor
    public Clothing() {
        super();
    }

    public Clothing(String productID, String productName, int availableItems, double price
            , String brand, ClothingSize size, String color) {
        super(productID, productName, availableItems, price, ProductType.Clothing);
        this.brand = brand;
        this.size = size;
        this.color = color;
    }
    //getters and setters

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public ClothingSize getSize() {
        return size;
    }

    public void setSize(ClothingSize size) {
        this.size = size;
    }

    public ProductType getClothingType() {
        return type;
    }

    public void setClothingType(ProductType type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    //toString method
    @Override
    public String toString() {
        return
                "brand='" + brand + '\n' +
                ", size='" + size + '\n' +
                ", type='" + type + '\n' +
                ", color='" + color + '\n' ;
    }
}
