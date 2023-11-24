package com.westminster.model;

import java.util.ArrayList;

class ShoppingCart {
    //instance variables
    private String cartID; //ID of the shopping cart
    private String userID; //ID of the user
    private ArrayList<Product> products; //list of products in the shopping cart

    public ShoppingCart() {
        super();
        products = new ArrayList<>();
    }

}
