package com.westminster.view;

import com.westminster.dao.ProductDao;

import java.util.Scanner;

public class ProductView {
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
