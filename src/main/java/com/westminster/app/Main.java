package com.westminster.app;

import com.westminster.controller.WestminsterShoppingManagerController;
import com.westminster.view.WestminsterShoppingManagerView;

import java.util.logging.Logger;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    static Logger logger = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) throws Exception {
        // Press Alt+Enter with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.
        logger.info("Hello and welcome!");
        WestminsterShoppingManagerView.start();
    }
}