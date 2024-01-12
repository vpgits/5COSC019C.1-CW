package com.westminster.view;

import com.westminster.interfaces.View;

import java.util.Scanner;

public class ProductView implements View {
    //utility prompts
    public static final String PRODUCTIDPROMPT = "Enter the product ID: ";
    public static final String PRODUCTNAMEPROMPT = "Enter the product name: ";
    public static final String AVAILABLEITEMSPROMPT = "Enter the available items stock: ";
    public static final String PRICEPROMPT = "Enter the price: ";
    public static final String PRODUCTTYPEPROMPT = "Enter the product type (Clothing | Electronics): ";
    public static final String MAXPRODUCT = "The product list is full";
    public static final String PRODUCTNOTFOUND = "Product not found";
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
    public static final String PRODUCTALREADYEXISTS = "Product already exists";

    /**
     * Constructor for the ProductView class.
     */
    public ProductView() {
        super();
    }

    /**
     * This method is used to get user input.
     *
     * @param message The prompt to be displayed.
     */
    public void printMessage(String message) {
        System.out.flush();
        System.out.println(message);
    }

    /**
     * This method is used to print errors.
     *
     * @param message The error message to be printed.
     */
    public void printError(String message) {
        System.out.flush();
        System.err.println(message);
    }

    /**
     * This method is used to get user input.
     *
     * @param prompt The prompt to be displayed.
     */
    public String callArgument(String prompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(prompt);
        System.out.flush();
        return scanner.nextLine();
    }

}
