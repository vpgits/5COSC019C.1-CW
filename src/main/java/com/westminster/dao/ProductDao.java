package com.westminster.dao;

import com.westminster.model.*;
import com.westminster.util.SQLiteConnection;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

/**
 * This class is used to perform CRUD operations on the product table in the database. Product Data Access Object
 */
public class ProductDao {
    private final Map<String, Product> products =Collections.synchronizedMap( new HashMap<>()); //synchronizedMap to avoid concurrent modification exception

    /**
     * Default constructor
     */
    public ProductDao() {
        super();
    }

    /**
     * Adds a product to the database
     * @param product the product to be added
     */
    public void addProduct(Product product) {
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
            throw new ProductDaoException("Unable to add product"+e.getMessage());
        }
    }

    /**
     * Deletes a product from the database
     * @param productId the id of the product to be deleted
     */
    public void deleteProduct(String productId) {
        Product product;
        String table = null;
        try {
            product = getProduct(productId);
            table = product.getType().toString().toLowerCase();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        String sql1 = "DELETE FROM product WHERE productId = ?";
        String sql2 = "DELETE FROM " + table + " WHERE productId = ?";
        try (
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt1 = conn.prepareStatement(sql1);
                PreparedStatement pstmt2 = conn.prepareStatement(sql2);
        ) {
            pstmt1.setString(1, product.getProductID());
            pstmt1.execute();
            pstmt2.setString(1, product.getProductID());
            pstmt2.execute();
            products.remove(product.getProductID());
        } catch (Exception e) {
            throw new ProductDaoException("Unable to delete product" +e.getMessage());
        }
    }

    public void updateStock(String productId, int quantity) throws ProductDaoException {
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
            throw new ProductDaoException("Unable to update stock"+e.getMessage());
        }
    }

    /**
     * Checks if a product exists in the database
     * @param productID the id of the product to be checked
     * @return true if the product exists, false otherwise
     * @throws ProductDaoException if an error occurs
     */
    public boolean doesProductExist(String productID) throws ProductDaoException {
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
            throw new ProductDaoException("Unable to identify if the product exists"+e.getMessage());
        }
        return false;
    }

    /**
     * Gets the number of products in the database
     * @return the number of products in the database
     */
    public int getProductCount() {
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
            throw new ProductDaoException("Error getting product count"+e.getMessage());
        }
    }

    /**
     * Gets a product from the database
     * @param productID the id of the product to be retrieved
     * @return the product with the given id
     */
    public Product getProduct(String productID) {
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
            } catch (Exception e) {
                throw new ProductDaoException("Error getting product" +e.getMessage());
            }
        } else return null;
    }

    /**
     * Gets all the products of a given type from the database
     * @param productType the type of the products to be retrieved
     * @return an ArrayList of products of the given type
     * @throws ProductDaoException if an error occurs
     */
    public ArrayList<Product> getProducts(ProductType productType) throws ProductDaoException {
        ArrayList<Product> products = new ArrayList<>(getProductCount());
        String tableId = null;
        if (productType == ProductType.Clothing) {
            tableId = "clothing";
        } else if (productType == ProductType.Electronics) {
            tableId = "electronics";
        }
        String sql = "SELECT * FROM product INNER JOIN " + tableId + " ON product.productId = " + tableId + ".productId ORDER BY productId";
        try (
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String productID = rs.getString("productID");
                String productName = rs.getString("productName");
                int availableItems = rs.getInt("availableItems");
                double price = rs.getDouble("price");
                if (productType == ProductType.Clothing) {
                    String brand = rs.getString("brand");
                    ClothingSize size = ClothingSize.valueOf(rs.getString("clothingSize").toUpperCase());
                    String color = rs.getString("color");
                    products.add(new Clothing(productID, productName, availableItems, price, brand, size, color));
                } else if (productType == ProductType.Electronics) {
                    String brand = rs.getString("brand");
                    String model = rs.getString("model");
                    String type = rs.getString("type");
                    int warranty = rs.getInt("warranty");
                    products.add(new Electronics(productID, productName, availableItems, price, brand, model, type, warranty));
                }
            }
        } catch (Exception e) {
            throw new ProductDaoException("Error getting products"+e.getMessage());
        }
        return products;
    }

    /**
     * Gets the current stock of a product
     * @param productID the id of the product
     * @return the current stock of the product
     */
    public int getCurrentStock(String productID) {
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
            throw new ProductDaoException("Error getting product count"+e.getMessage());
        }
    }

    /**
     * Gets all the products from the database
     * @return an ArrayList of all the products
     */
    public List<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<>();
        try {
            products.addAll(getProducts(ProductType.Clothing));
            products.addAll(getProducts(ProductType.Electronics));
            products.sort(Comparator.comparing(Product::getProductID));
            return products;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return products;
    }

    /**
     * Saves the products to the products.ser file inside resources/database/
     */
    public void saveProducts() {

        String basePath = new File("").getAbsolutePath();
        String relativePath = "/src/main/resources/database/";
        Path resourcePath = Paths.get(basePath, relativePath, "products.ser");
        try {
            if (Files.exists(resourcePath.getParent())) {
                OutputStream fos = Files.newOutputStream(resourcePath);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                loadProductsFromDatabase();
                oos.writeObject(products);
                oos.close();
                fos.close();
                System.out.println("Products saved to the products.ser file");
            } else {
                System.out.println("Resource directory not found: /resources/database/");
            }
        } catch (Exception e) {
           throw new ProductDaoException("Error saving products"+e.getMessage());
        }
    }

    /**
     * Loads the products from the products.ser file inside resources/database/
     */
    public synchronized void loadProducts() {
        String basePath = new File("").getAbsolutePath();
        String relativePath = "/src/main/resources/database/";
        Path resourcePath = Paths.get(basePath, relativePath, "products.ser");
        try {
            if (!new File(String.valueOf(resourcePath)).exists()) {
                System.out.println("products.ser file not found");
                return;
            }
            System.out.println("Loading products from the products.ser file");
            products.clear();
            deserializeProducts();
            System.out.println("Products loaded successfully");
            System.out.println("Updating the database");
            loadProductsToDatabase();
            System.out.println("Database updated successfully");
        } catch (Exception e) {
            loadProductsFromDatabase();
            throw new ProductDaoException("Error loading products"+e.getMessage());
        }
    }

    private void loadProductsFromDatabase() {
        try {
            products.clear();
            getProducts(ProductType.Clothing).forEach(product -> products.put(product.getProductID(), product));
            getProducts(ProductType.Electronics).forEach(product -> products.put(product.getProductID(), product));
        } catch (Exception e) {
            throw new ProductDaoException("Error loading products from database"+e.getMessage());
        }

    }

    private void loadProductsToDatabase() throws ProductDaoException {
        emptyTable("product");
        emptyTable("clothing");
        emptyTable("electronics");

        products.forEach((productId, product) -> {
            try {
                addProduct(product);
            } catch (ProductDaoException e) {
                throw new ProductDaoException("Error loading products to database"+e.getMessage());
            }
        });
    }

    private void emptyTable(String table) throws ProductDaoException {
        String sql = "DELETE FROM " + table;
        try (
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new ProductDaoException("Error emptying table"+e.getMessage());
        }
    }

    private synchronized void deserializeProducts() throws IOException, ClassNotFoundException {
        String basePath = new File("").getAbsolutePath();
        String relativePath = "/src/main/resources/database/";
        Path resourcePath = Paths.get(basePath, relativePath, "products.ser");
        try {
            FileInputStream fis = new FileInputStream(String.valueOf(resourcePath));
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object oisObject = ois.readObject();
            if (oisObject instanceof Map<?, ?>) {
                Map<?, ?> rawMap = (Map<?, ?>) oisObject;
                for (Map.Entry<?, ?> entry : rawMap.entrySet()) {
                    if (entry.getKey() instanceof String && entry.getValue() instanceof Product) {
                        products.put((String) entry.getKey(), (Product) entry.getValue());
                    }
                }
            }
            ois.close();
            fis.close();
        } catch (Exception e) {
            throw new ProductDaoException("Error deserializing products"+e.getMessage());
        }
    }

    public synchronized void clearHashMap() {
        products.clear();
    }

    public static class ProductDaoException extends RuntimeException {
        public ProductDaoException(String message) {
            super(message);
        }
    }
}
