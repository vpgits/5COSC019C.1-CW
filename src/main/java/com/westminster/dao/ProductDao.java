package com.westminster.dao;

import com.westminster.model.Clothing;
import com.westminster.model.Electronics;
import com.westminster.model.Product;
import com.westminster.model.ProductType;
import com.westminster.util.SQLiteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class ProductDao {
    public static final int MAX_PRODUCTS = 50;
    private static final HashMap<String, Product> productHashMap = new HashMap<String, Product>(MAX_PRODUCTS); // ProductID, Product

    private ProductDao() {
        super();
    }

    public static void addElectronicsProduct(Electronics product) throws ProductDaoException {
        productHashMap.put(product.getProductID(), product);
        String sql = "INSERT INTO product(productID, productName, productType, price, avaliableItems" +
                "brand, model, type, warranty) VALUES(?,?,?,?,?, ?, ?, ?, ?)";
        try (
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, product.getProductID());
            pstmt.setString(2, product.getProductName());
            pstmt.setString(3, product.getType().toString());
            pstmt.setDouble(4, product.getPrice());
            pstmt.setInt(5, product.getAvailableItems());
            pstmt.setString(6, product.getBrand());
            pstmt.setString(7, product.getModel());
            pstmt.setString(8, product.getElectricalProductType());
            pstmt.setInt(9, product.getWarranty());
            pstmt.execute();
        } catch (Exception e) {
            throw new ProductDaoException(e.getMessage());
        }
    }

    public static void addClothingProduct(Clothing product) throws ProductDaoException {
        productHashMap.put(product.getProductID(), product);
        String sql = "INSERT INTO clothing(productID, productName, productType, price, avaliableItems, " +
                "brand, size, color ) VALUES(?,?,?,?,?, ?, ?, ?)";
        try (
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, product.getProductID());
            pstmt.setString(2, product.getProductName());
            pstmt.setString(3, product.getType().toString());
            pstmt.setDouble(4, product.getPrice());
            pstmt.setInt(5, product.getAvailableItems());
            pstmt.setString(6, product.getBrand());
            pstmt.setString(7, product.getSize().toString());
            pstmt.setString(8, product.getColor());
            pstmt.execute();
        } catch (Exception e) {
            throw new ProductDaoException(e.getMessage());
        }
    }

    public static void deleteProduct(Product product) throws ProductDaoException {
        String tableName = null;
        if (product.getType() == ProductType.Clothing) {
            tableName = "clothing";
        } else if (product.getType() == ProductType.Electronics) {
            tableName = "electronics";
        }
        String sql = "DELETE FROM " +tableName+" WHERE productID = ?";
        try (
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, product.getProductID());
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new ProductDaoException(e.getMessage());
        }
        productHashMap.remove(product.getProductID());
    }


    public static void updateClothingProduct(Clothing product) throws ProductDaoException {
        try (
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement("UPDATE clothing SET productName = ?, " +
                        "productType = ?, price = ?, avaliableItems = ?, " +
                        "brand = ?, size = ?, color = ? WHERE productID = ?")
        ) {
            if (doesExist(product.getProductID())) { //checks if the product exists in the database, database lookup
                // is more efficient than looping through the arraylist
                productHashMap.put(product.getProductID(), product);
                pstmt.setString(1, product.getProductName());
                pstmt.setString(2, product.getType().toString());
                pstmt.setDouble(3, product.getPrice());
                pstmt.setInt(4, product.getAvailableItems());
                pstmt.setString(5, product.getProductID());
                pstmt.setString(6, product.getBrand());
                pstmt.setString(7, product.getSize().toString());
                pstmt.setString(8, product.getColor());
                pstmt.executeUpdate();
            } else {
                throw new ProductDaoException("Product not found");
            }
        } catch (Exception e) {
            throw new ProductDaoException(e.getMessage());
        }
    }

    public static void updateElectronicsProduct(Electronics product) throws ProductDaoException {
        try (
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement("UPDATE electronics SET productName = ?, " +
                        "productType = ?, price = ?, avaliableItems = ?, " +
                        "brand = ?, model = ?, type = ? , warranty = ? WHERE productID = ?")
        ) {
            if (doesExist(product.getProductID())) { //checks if the product exists in the database, database lookup
                // is more efficient than looping through the arraylist
                productHashMap.put(product.getProductID(), product);
                pstmt.setString(1, product.getProductName());
                pstmt.setString(2, product.getType().toString());
                pstmt.setDouble(3, product.getPrice());
                pstmt.setInt(4, product.getAvailableItems());
                pstmt.setString(5, product.getProductID());
                pstmt.setString(6, product.getBrand());
                pstmt.setString(7, product.getModel());
                pstmt.setString(8, product.getElectricalProductType());
                pstmt.setInt(9, product.getWarranty());
                pstmt.executeUpdate();
            } else {
                throw new ProductDaoException("Product not found");
            }
        } catch (Exception e) {
            throw new ProductDaoException(e.getMessage());
        }
    }

    public static boolean doesExist(String productID) throws ProductDaoException {
        String sql = "SELECT * FROM product WHERE productID = ?";
        try (
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, productID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println(rs.getString("productID"));
                return true;
            }
        } catch (Exception e) {
            throw new ProductDaoException(e.getMessage());
        }
        return false;
    }


    public static boolean isFull() {
        return productHashMap.size() != MAX_PRODUCTS;
    }


    public static Product getProductFromHashmap(String productID) throws ProductDaoException {
        if (doesExist(productID)) {
            return productHashMap.get(productID);
        } else return null;
    }

    public static class ProductDaoException extends Exception {
        public ProductDaoException(String message) {
            super(message);
        }
    }
}
