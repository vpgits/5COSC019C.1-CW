package com.westminster.view;


import java.util.Scanner;

public class ProductView {
    public static final String PRODUCTIDPROMPT = "Enter the product ID: ";
    public static final String PRODUCTNAMEPROMPT = "Enter the product name: ";
    public static final String AVAILABLEITEMSPROMPT = "Enter the available items stock: ";
    public static final String PRICEPROMPT = "Enter the price: ";
    public static final String PRODUCTTYPEPROMPT = "Enter the product type (Clothing | Electronics): ";
    public static final String MAXPRODUCT = "The product list is full";
    public static final String PRODUCTNOTFOUND = "Product not found";
    public static final String PRODUCTFOUND = "Product found. Please enter the information for the updated fields. Leave blank for no change";
    public static final String BRANDPROMPT = "Enter the brand name of the product: ";
    public static final String BRANDERROR = "Invalid brand name";
    public static final String MODELPROMPT = "Enter the model name of the product: ";
    public static final String MODELERROR = "Invalid model name";
    public static final String SIZEPROMPT = "Enter the size of the clothing product: ";
    public static final String SIZEERROR = "Invalid size";
    public static final String COLORPROMPT = "Enter the color of the clothing product: ";
    public static final String COLORERROR = "Invalid color";
    public static final String WARRANTYPROMPT = "Enter the warranty of the electronic product in months: ";
    public static final String PRODUCTTYPEERROR = "Invalid product type";
    public static final String ELECTRONICPRODUCTTYPEPROMPT = "Enter the type of the electronic product:";
    Scanner scanner = new Scanner(System.in);

    public ProductView() {
        super();
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public void printError(String message) {
        System.err.println(message);
    }

    public String callArgument(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine();
    }
}
