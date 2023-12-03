package com.westminster.dao;

import com.westminster.model.Product;
import com.westminster.util.SQLiteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class ProductDao {
    private static ProductDao instance;
    public static final int MAX_PRODUCTS = 50;
    private static final ArrayList<Product> products = new ArrayList<Product>(MAX_PRODUCTS);

    private ProductDao() {
        super();
    }
    public static void addProduct(Product product) throws ProductDaoException {
        products.add(product);
        String sql = "INSERT INTO product(productID, productName, productType, price, avaliableItems) VALUES(?,?,?,?,?)";
        try(
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)
                ) {
            pstmt.setString(1, product.getProductID());
            pstmt.setString(2, product.getProductName());
            pstmt.setString(3, product.getType().toString());
            pstmt.setDouble(4, product.getPrice());
            pstmt.setInt(5, product.getAvailableItems());
        }  catch (Exception e) {
            throw new ProductDaoException(e.getMessage());
        }
    }
    public static void deleteProduct(Product product) {
        products.remove(product);
    }

    public static void updateProduct(Product product) {
        products.set(products.indexOf(product), product);
    }
    public static boolean doesExist(Product product) throws ProductDaoException {
        String sql = "SELECT productID FROM product WHERE productID = ?";
        try(
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)
                )
        {
            pstmt.setString(1, product.getProductID());
            pstmt.executeUpdate();
            if(pstmt.getResultSet().next()) {
                return true;
            }
        } catch (Exception e) {
            throw new ProductDaoException(e.getMessage());
        }
        return false;
    }

    public static boolean isFull() {
        return products.size() >= MAX_PRODUCTS;
    }

    public static class ProductDaoException extends Exception {
        public ProductDaoException(String message) {
            super(message);
        }
    }
}
