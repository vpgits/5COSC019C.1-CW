package com.westminster.model;


/**
 * Represents a clothing product.
 * Inherits from the Product class.
 */
public class Clothing extends Product {
    //instance variables
    private String brand; //brand of the clothing product
    private ClothingSize size; //size of the clothing product(S/M/L/XL)
    private String color; //color of the clothing product

    //default constructor
    public Clothing() {
        super();
    }

    /**
     * Constructs a Clothing object with the specified parameters.
     *
     * @param productID      the ID of the clothing product
     * @param productName    the name of the clothing product
     * @param availableItems the number of available items of the clothing product
     * @param price          the price of the clothing product
     * @param brand          the brand of the clothing product
     * @param size           the size of the clothing product
     * @param color          the color of the clothing product
     */
    public Clothing(String productID, String productName, int availableItems, double price
            , String brand, ClothingSize size, String color) {
        super(productID, productName, availableItems, price, ProductType.Clothing);
        this.brand = brand;
        this.size = size;
        this.color = color;
    }

    //getters and setters

    /**
     * Gets the brand of the clothing product.
     *
     * @return the brand of the clothing product
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Sets the brand of the clothing product.
     *
     * @param brand the brand of the clothing product
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Gets the size of the clothing product.
     *
     * @return the size of the clothing product
     */
    public ClothingSize getSize() {
        return size;
    }

    /**
     * Sets the size of the clothing product.
     *
     * @param size the size of the clothing product
     */
    public void setSize(ClothingSize size) {
        this.size = size;
    }

    /**
     * Gets the color of the clothing product.
     *
     * @return the color of the clothing product
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the color of the clothing product.
     *
     * @param color the color of the clothing product
     */
    public void setColor(String color) {
        this.color = color;
    }

    //toString method
    @Override
    public String toString() {
        return
                " brand : " + brand + '\n' +
                " size : " + size + '\n' +
                " type : " + type + '\n' +
                " color : " + color + '\n' ;
    }

    /**
     * Returns a string representation of the Clothing object in table format.
     *
     * @return a string representation of the Clothing object in table format
     */
    @Override
    public String toStringTable(){
        return brand + ' ' + size + ' ' + type + ' ' + color;
    }

    /**
     * Returns a string representation of the Clothing object for console output.
     *
     * @return a string representation of the Clothing object for console output
     */
    public String toStringConsole(){
        return
                super.toString() +
                "- Brand : " + brand + '\n' +
                "- Size : " + size + '\n' +
                "- Type : " + type + '\n' +
                "- Color : " + color + '\n' ;
    }
}
