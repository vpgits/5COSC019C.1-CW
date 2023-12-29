package com.westminster.dao;

import com.westminster.model.*;
import com.westminster.util.SQLiteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductDao {

    public ProductDao() {
        super();
    }

    public static void addProduct(Product product) throws ProductDaoException {
        String sqlProduct = "INSERT INTO product(productName, avaliableItems, price, productType) VALUES(?,?,?,?)";
        String sqlClothing = "INSERT INTO clothing(productID, brand, clothingSize, color) VALUES(?,?,?,?)";
        String sqlElectronics = "INSERT INTO electronics(productID, brand, model, type, warranty) VALUES(?,?,?,?,?)";
        try (
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmtProduct = conn.prepareStatement(sqlProduct);
                PreparedStatement pstmtClothing = conn.prepareStatement(sqlClothing);
                PreparedStatement pstmtElectronics = conn.prepareStatement(sqlElectronics)
        ) {
            pstmtProduct.setString(1, product.getProductName());
            pstmtProduct.setInt(2, product.getAvailableItems());
            pstmtProduct.setDouble(3, product.getPrice());
            pstmtProduct.setString(4, product.getType().toString());
            pstmtProduct.execute();

            if (product instanceof Clothing) {
                Clothing clothing = (Clothing) product;
                pstmtClothing.setString(1, clothing.getProductID());
                pstmtClothing.setString(2, clothing.getBrand());
                pstmtClothing.setString(3, clothing.getSize().toString());
                pstmtClothing.setString(4, clothing.getColor());
                pstmtClothing.execute();
            } else if (product instanceof Electronics) {
                Electronics electronics = (Electronics) product;
                pstmtElectronics.setString(1, electronics.getProductID());
                pstmtElectronics.setString(2, electronics.getBrand());
                pstmtElectronics.setString(3, electronics.getModel());
                pstmtElectronics.setString(4, electronics.getElectricalProductType());
                pstmtElectronics.setInt(5, electronics.getWarranty());
                pstmtElectronics.execute();
            }
        } catch (Exception e) {
            throw new ProductDaoException(e.getMessage());
        }
    }

    public static void deleteProduct(String productId) throws ProductDaoException {
        Product product = null;
        try{
            product = getProduct(productId);
        } catch (Exception e){
            throw new ProductDaoException(e.getMessage());
        }

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
    }

    public  static void updateStock(String productId, int quantity) throws ProductDaoException {
        String sql = "UPDATE product SET avaliableItems = ? WHERE productId = ?";
        try (
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, quantity);
            pstmt.setString(2, productId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new ProductDaoException(e.getMessage());
        }
    }


    public static void updateClothingProduct(Clothing product) throws ProductDaoException {
        String productSql = "UPDATE product SET productName = ?, " +
                "productType = ?, price = ?, avaliableItems = ? WHERE productId = ?";
        String clothingSql = "UPDATE clothing SET brand = ?, clothingSize = ?, color = ? WHERE productId = ?";
        try (
                Connection conn = SQLiteConnection.connect();
                PreparedStatement productPstmt = conn.prepareStatement(productSql);
                PreparedStatement clothingPstmt = conn.prepareStatement(clothingSql);
        ) {
            if (doesProductExist(product.getProductID())) { //checks if the product exists in the database, database lookup
                // is more efficient than looping through the arraylist
                productPstmt.setString(1, product.getProductName());
                productPstmt.setString(2, product.getType().toString());
                productPstmt.setDouble(3, product.getPrice());
                productPstmt.setInt(4, product.getAvailableItems());
                productPstmt.setString(5, product.getProductID());
                productPstmt.executeUpdate();

                clothingPstmt.setString(1, product.getBrand());
                clothingPstmt.setString(2, product.getSize().toString());
                clothingPstmt.setString(3, product.getColor());
                clothingPstmt.setString(4, product.getProductID());
                clothingPstmt.executeUpdate();
            } else {
                throw new ProductDaoException("Product not found");
            }
        } catch (Exception e) {
            throw new ProductDaoException(e.getMessage());
        }
    }

    public static void updateElectronicsProduct(Electronics product) throws ProductDaoException {
        String productSql = "UPDATE product SET productName = ?, " +
                "productType = ?, price = ?, avaliableItems = ? WHERE productId = ?";
        String electronicsSql = "UPDATE electronics SET brand = ?, model = ?, type = ?, warranty = ? WHERE productId = ?";
        try (
                Connection conn = SQLiteConnection.connect();
                PreparedStatement productPstmt = conn.prepareStatement(productSql);
                PreparedStatement electronicsPstmt = conn.prepareStatement(electronicsSql);
        ) {
            if (doesProductExist(product.getProductID())) { //checks if the product exists in the database, database lookup
                // is more efficient than looping through the arraylist
                productPstmt.setString(1, product.getProductName());
                productPstmt.setString(2, product.getType().toString());
                productPstmt.setDouble(3, product.getPrice());
                productPstmt.setInt(4, product.getAvailableItems());
                productPstmt.setString(5, product.getProductID());
                productPstmt.executeUpdate();

                electronicsPstmt.setString(1, product.getBrand());
                electronicsPstmt.setString(2, product.getModel());
                electronicsPstmt.setString(3, product.getElectricalProductType());
                electronicsPstmt.setInt(4, product.getWarranty());
                electronicsPstmt.setString(5, product.getProductID());
                electronicsPstmt.executeUpdate();
            } else {
                throw new ProductDaoException("Product not found");
            }
        } catch (Exception e) {
            throw new ProductDaoException(e.getMessage());
        }
    }

    public static boolean doesProductExist(String productID) throws ProductDaoException {
        String sql = "SELECT productId FROM product WHERE productId = ?";
        try (
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, productID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            throw new ProductDaoException(e.getMessage());
        }
        return false;
    }


    public static int getProductCount() throws ProductDaoException {
        String sql = "SELECT COUNT(*) FROM product";
        try (
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new ProductDaoException("Error getting product count");
            }
        } catch (Exception e) {
            throw new ProductDaoException(e.getMessage());
        }
    }


    public static Product getProduct(String productID) throws ProductDaoException {
        if (doesProductExist(productID)) {
            String sql = "SELECT * FROM product WHERE productID = ?";
            try (
                    Connection conn = SQLiteConnection.connect();
                    PreparedStatement pstmt = conn.prepareStatement(sql)
            ) {
                pstmt.setString(1, productID);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    String productName = rs.getString("productName");
                    int availableItems = rs.getInt("avaliableItems");
                    double price = rs.getDouble("price");
                    ProductType productType = ProductType.valueOf(rs.getString("productType"));
                    if (productType == ProductType.Clothing) {
                        String sqlClothing = "SELECT * FROM clothing WHERE productID = ?";
                        try (
                                PreparedStatement pstmtClothing = conn.prepareStatement(sqlClothing)
                        ) {
                            pstmtClothing.setString(1, productID);
                            ResultSet rsClothing = pstmtClothing.executeQuery();
                            if (rsClothing.next()) {
                                String brand = rsClothing.getString("brand");
                                ClothingSize size = ClothingSize.valueOf(rsClothing.getString("clothingSize").toUpperCase());
                                String color = rsClothing.getString("color");
                                return new Clothing(productID, productName, availableItems, price, brand, size, color);
                            } else {
                                throw new ProductDaoException("Error getting product");
                            }
                        } catch (Exception e) {
                            throw new ProductDaoException(e.getMessage());
                        }
                    } else if (productType == ProductType.Electronics) {
                        String sqlElectronics = "SELECT * FROM electronics WHERE productID = ?";
                        try (
                                PreparedStatement pstmtElectronics = conn.prepareStatement(sqlElectronics)
                        ) {
                            pstmtElectronics.setString(1, productID);
                            ResultSet rsElectronics = pstmtElectronics.executeQuery();
                            if (rsElectronics.next()) {
                                String brand = rsElectronics.getString("brand");
                                String model = rsElectronics.getString("model");
                                String type = rsElectronics.getString("type");
                                int warranty = rsElectronics.getInt("warranty");
                                return new Electronics(productID, productName, availableItems, price, brand, model, type, warranty);
                            } else {
                                throw new ProductDaoException("Error getting product");
                            }
                        } catch (Exception e) {
                            throw new ProductDaoException(e.getMessage());
                        }
                    } else {
                        throw new ProductDaoException("Error getting product");
                    }
                } else {
                    throw new ProductDaoException("Error getting product");
                }
            } catch (Exception e){
                throw new ProductDaoException(e.getMessage());
            }
        } else return null;
    }

    public static ArrayList<Product> getProducts(ProductType productType) throws ProductDaoException {
        ArrayList<Product> products = new ArrayList<>(getProductCount());
        String tableId = null;
        if (productType == ProductType.Clothing){
            tableId = "clothing";
        } else if (productType == ProductType.Electronics){
            tableId = "electronics";
        }
        String sql = "SELECT * FROM product INNER JOIN "+tableId+" ON product.productId = "+tableId+".productId ORDER BY productId";
        try(
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ){
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                String productID = rs.getString("productID");
                String productName = rs.getString("productName");
                int availableItems = rs.getInt("avaliableItems");
                double price = rs.getDouble("price");
                if (productType == ProductType.Clothing){
                    String brand = rs.getString("brand");
                    ClothingSize size = ClothingSize.valueOf(rs.getString("clothingSize").toUpperCase());
                    String color = rs.getString("color");
                    products.add(new Clothing(productID, productName, availableItems, price, brand, size, color));
                } else if (productType == ProductType.Electronics){
                    String brand = rs.getString("brand");
                    String model = rs.getString("model");
                    String type = rs.getString("type");
                    int warranty = rs.getInt("warranty");
                    products.add(new Electronics(productID, productName, availableItems, price, brand, model, type, warranty));
                }
            }
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
        return products;
    }


    public static int getCurrentStock(String productID) {
        String sql = "SELECT avaliableItems FROM product WHERE productID = ?";
        try (
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, productID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("avaliableItems");
            } else {
                throw new ProductDaoException("Error getting product count");
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public static List<Product> getAllProducts(){
        ArrayList<Product> products = new ArrayList<>();
        try{
            products.addAll(getProducts(ProductType.Clothing));
            products.addAll(getProducts(ProductType.Electronics));
            products.sort((p1, p2)->p1.getProductID().compareTo(p2.getProductID()));
            return products;
        } catch (Exception e){
            System.err.println(e.getMessage());
        }
        return products;
    }


    public static class ProductDaoException extends Exception {
        public ProductDaoException(String message) {
            super(message);
        }
    }
}
