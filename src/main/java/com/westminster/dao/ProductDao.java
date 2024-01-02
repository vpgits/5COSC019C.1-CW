package com.westminster.dao;

import com.westminster.model.*;
import com.westminster.util.SQLiteConnection;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class ProductDao {
    private  final HashMap<String, Product> products = new HashMap<>();
    public ProductDao() {
        super();
        loadProducts();

    }

    public  void addProduct(Product product) {
        String sqlProduct = "INSERT INTO product(productId, productName, availableItems, price, productType) VALUES(?,?,?,?,?)";
        String sqlClothing = "INSERT INTO clothing(productID, brand, clothingSize, color) VALUES(?,?,?,?)";
        String sqlElectronics = "INSERT INTO electronics(productID, brand, model, type, warranty) VALUES(?,?,?,?,?)";
        try (
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmtProduct = conn.prepareStatement(sqlProduct);
                PreparedStatement pstmtClothing = conn.prepareStatement(sqlClothing);
                PreparedStatement pstmtElectronics = conn.prepareStatement(sqlElectronics)
        ) {
            pstmtProduct.setString(1, product.getProductID());
            pstmtProduct.setString(2, product.getProductName());
            pstmtProduct.setInt(3, product.getAvailableItems());
            pstmtProduct.setDouble(4, product.getPrice());
            pstmtProduct.setString(5, product.getType().toString());
            pstmtProduct.execute();

            if (product instanceof Clothing clothing) {
                pstmtClothing.setString(1, clothing.getProductID());
                pstmtClothing.setString(2, clothing.getBrand());
                pstmtClothing.setString(3, clothing.getSize().toString());
                pstmtClothing.setString(4, clothing.getColor());
                pstmtClothing.execute();
                products.put(clothing.getProductID(), clothing);
            } else if (product instanceof Electronics electronics) {
                pstmtElectronics.setString(1, electronics.getProductID());
                pstmtElectronics.setString(2, electronics.getBrand());
                pstmtElectronics.setString(3, electronics.getModel());
                pstmtElectronics.setString(4, electronics.getElectricalProductType());
                pstmtElectronics.setInt(5, electronics.getWarranty());
                pstmtElectronics.execute();
                products.put(electronics.getProductID(), electronics);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public  void deleteProduct(String productId) {
        Product product;
        String table=null;
        try{
            product = getProduct(productId);
            table = product.getType().toString().toLowerCase();
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        
        String sql1 = "DELETE FROM product WHERE productId = ?";
        String sql2 = "DELETE FROM "+table+" WHERE productId = ?";
        try (
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt1 = conn.prepareStatement(sql1);
                PreparedStatement pstmt2 = conn.prepareStatement(sql2);
        ) {
            pstmt1.setString(1, product.getProductID());
            pstmt1.execute();
            pstmt2.setString(1,product.getProductID());
            pstmt2.execute();
            products.remove(product.getProductID());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public   void updateStock(String productId, int quantity) throws ProductDaoException {
        String sql = "UPDATE product SET availableItems = ? WHERE productId = ?";
        try (
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, quantity);
            pstmt.setString(2, productId);
            pstmt.executeUpdate();
            products.put(productId, getProduct(productId));
        } catch (Exception e) {
            throw new ProductDaoException(e.getMessage());
        }
    }


//    public  void updateClothingProduct(Clothing product) throws ProductDaoException {
//        String productSql = "UPDATE product SET productName = ?, " +
//                "productType = ?, price = ?, availableItems = ? WHERE productId = ?";
//        String clothingSql = "UPDATE clothing SET brand = ?, clothingSize = ?, color = ? WHERE productId = ?";
//        try (
//                Connection conn = SQLiteConnection.connect();
//                PreparedStatement productPstmt = conn.prepareStatement(productSql);
//                PreparedStatement clothingPstmt = conn.prepareStatement(clothingSql);
//        ) {
//            if (doesProductExist(product.getProductID())) { //checks if the product exists in the database, database lookup
//                // is more efficient than looping through the arraylist
//                productPstmt.setString(1, product.getProductName());
//                productPstmt.setString(2, product.getType().toString());
//                productPstmt.setDouble(3, product.getPrice());
//                productPstmt.setInt(4, product.getAvailableItems());
//                productPstmt.setString(5, product.getProductID());
//                productPstmt.executeUpdate();
//
//                clothingPstmt.setString(1, product.getBrand());
//                clothingPstmt.setString(2, product.getSize().toString());
//                clothingPstmt.setString(3, product.getColor());
//                clothingPstmt.setString(4, product.getProductID());
//                clothingPstmt.executeUpdate();
//            } else {
//                throw new ProductDaoException("Product not found");
//            }
//        } catch (Exception e) {
//            throw new ProductDaoException(e.getMessage());
//        }
//    }
//
//    public  void updateElectronicsProduct(Electronics product) throws ProductDaoException {
//        String productSql = "UPDATE product SET productName = ?, " +
//                "productType = ?, price = ?, availableItems = ? WHERE productId = ?";
//        String electronicsSql = "UPDATE electronics SET brand = ?, model = ?, type = ?, warranty = ? WHERE productId = ?";
//        try (
//                Connection conn = SQLiteConnection.connect();
//                PreparedStatement productPstmt = conn.prepareStatement(productSql);
//                PreparedStatement electronicsPstmt = conn.prepareStatement(electronicsSql);
//        ) {
//            if (doesProductExist(product.getProductID())) { //checks if the product exists in the database, database lookup
//                // is more efficient than looping through the arraylist
//                productPstmt.setString(1, product.getProductName());
//                productPstmt.setString(2, product.getType().toString());
//                productPstmt.setDouble(3, product.getPrice());
//                productPstmt.setInt(4, product.getAvailableItems());
//                productPstmt.setString(5, product.getProductID());
//                productPstmt.executeUpdate();
//
//                electronicsPstmt.setString(1, product.getBrand());
//                electronicsPstmt.setString(2, product.getModel());
//                electronicsPstmt.setString(3, product.getElectricalProductType());
//                electronicsPstmt.setInt(4, product.getWarranty());
//                electronicsPstmt.setString(5, product.getProductID());
//                electronicsPstmt.executeUpdate();
//            } else {
//                throw new ProductDaoException("Product not found");
//            }
//        } catch (Exception e) {
//            throw new ProductDaoException(e.getMessage());
//        }
//    }

    public  boolean doesProductExist(String productID) throws ProductDaoException {
        String sql = "SELECT productId FROM product WHERE productId = ?";
        try (
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, productID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getString("productId").equals(productID)) {
                return true;
            }
        } catch (Exception e) {
            throw new ProductDaoException(e.getMessage());
        }
        return false;
    }


    public  int getProductCount() {
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
            System.err.println(e.getMessage());
            return 0;
        }
    }


    public  Product getProduct(String productID) {
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
                    int availableItems = rs.getInt("availableItems");
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

    public  ArrayList<Product> getProducts(ProductType productType) throws ProductDaoException {
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
                PreparedStatement pstmt = conn.prepareStatement(sql)
                ){
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                String productID = rs.getString("productID");
                String productName = rs.getString("productName");
                int availableItems = rs.getInt("availableItems");
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


    public  int getCurrentStock(String productID) {
        String sql = "SELECT availableItems FROM product WHERE productId = ?";
        try (
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, productID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("availableItems");
            } else {
                throw new ProductDaoException("Error getting product count");
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public  List<Product> getAllProducts(){
        ArrayList<Product> products = new ArrayList<>();
        try{
            products.addAll(getProducts(ProductType.Clothing));
            products.addAll(getProducts(ProductType.Electronics));
            products.sort(Comparator.comparing(Product::getProductID));
            return products;
        } catch (Exception e){
            System.err.println(e.getMessage());
        }
        return products;
    }


public void saveProducts() {
    try {
        String basePath = new File("").getAbsolutePath();
        String relativePath = "/src/main/resources/database/"; 
        Path resourcePath = Paths.get(basePath, relativePath, "products.ser");

        if (Files.exists(resourcePath.getParent())) {
            OutputStream fos = Files.newOutputStream(resourcePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(products);
            oos.close();
            fos.close();
        } else {
            System.out.println("Resource directory not found: /resources/database/");
        }
    } catch (IOException e) {
        System.out.println(e.getMessage());
    }
}

    public void loadProducts()  {
        try{

            if (new File("resources/database/products.ser").exists() && products.isEmpty()){
            products.clear();
            deserializeProducts();
            loadProductsToDatabase();
            } else{
                loadProductsFromDatabase();
            }
        }
        catch (Exception e){
            System.err.println(e.getMessage());
            loadProductsFromDatabase();
        }
    }

    private void loadProductsFromDatabase() {
        try{
            products.clear();
            getProducts(ProductType.Clothing).forEach(product -> products.put(product.getProductID(), product));
            getProducts(ProductType.Electronics).forEach(product -> products.put(product.getProductID(), product));
        } catch (Exception e){
            System.err.println(e.getMessage());
        }

    }

    private void loadProductsToDatabase() throws ProductDaoException {
        products.forEach((productId, product) -> {
            try {
                emptyDatabase();
                addProduct(product);
            } catch (ProductDaoException e) {
                System.err.println(e.getMessage());
            }
        });
    }

    private  void emptyDatabase() throws ProductDaoException {
        String sql = "DELETE FROM product";
        try (
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new ProductDaoException(e.getMessage());
        }
    }

    private void deserializeProducts() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream("/resources/database/products.ser");
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object oisObject = ois.readObject();
        if (oisObject instanceof HashMap<?, ?> rawMap){
            for (Map.Entry<?,?> entry: rawMap.entrySet()){
                if (entry.getKey() instanceof String && entry.getValue() instanceof Product){
                    products.put((String) entry.getKey(), (Product) entry.getValue());
                }
            }
        }
        ois.close();
        fis.close();
    }


    public static class ProductDaoException extends RuntimeException {
        public ProductDaoException(String message) {
            super(message);
        }
    }

    // public static void main(String[] args) {
    //     ProductDao productDao = new ProductDao();
    //     productDao.saveProducts();
    //     productDao.loadProducts();

    // }
}
