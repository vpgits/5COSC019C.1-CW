package com.westminster.model;

import java.io.Serializable;

public class Electronics extends Product implements Serializable {
    //instance variables
    private String brand; //brand of the electronic product
    private String model; //model of the electronic product
    private String type; //type of the electronic product
    private int warranty; //warranty of the electronic product in months

    @Override
    public String toString() {
        return
                "brand='" + brand + '\n' +
                ", model='" + model + '\n' +
                ", type='" + type + '\n' +
                ", warranty=" + warranty
                ;
    }


    public String toStringConsole() {
        return
                super.toString() +
                "- brand='" + brand + '\n' +
                "- model='" + model + '\n' +
                "- type='" + type + '\n' +
                "- warranty=" + warranty+'\n'
                ;
    }

    //default constructor
    public Electronics() {
        super();
    }

    public Electronics (String productID, String productName, int availableItems, double price, String brand,
                        String model, String type, int warranty){
        super(productID, productName, availableItems, price , ProductType.Electronics);
        this.brand = brand;
        this.model = model;
        this.type = type;
        this.warranty = warranty;
    }

    //getters and setters
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getElectricalProductType() {
        return type;
    }

    public void setElectricalProductType(String type) {
        this.type = type;
    }

    public int getWarranty() {
        return warranty;
    }
    //toString method
    public void setWarranty(int warranty) {
        this.warranty = warranty;
    }
}
