package com.westminster.model;

class Clothing {
    //instance variables
    private String brand; //brand of the clothing product
    private String size; //size of the clothing product(S/M/L/XL)
    private String type; //type of the clothing product(Male/Female/Adult/Child)
    private String color; //color of the clothing product

    //default constructor
    public Clothing() {
        super();
    }
    //getters and setters

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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
        return "Clothing{" +
                "brand='" + brand + '\'' +
                ", size='" + size + '\'' +
                ", type='" + type + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
