package com.westminster.dao;

import com.westminster.model.Electronics;
import com.westminster.model.Product;
import com.westminster.model.ShoppingCart;
import com.westminster.model.Clothing;
import com.westminster.model.ProductType;
import com.westminster.util.SQLiteConnection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;


import javax.sql.rowset.serial.SerialBlob;

public class ShoppingCartDao {
    private ShoppingCartDao() {
        super();
    }
    public static ShoppingCart createShoppingCart(String username)  {
        ShoppingCart shoppingCart = new ShoppingCart( username);
        String sql = "INSERT INTO shopping_carts (username, uuid, shoppingcart) VALUES (?, ?, ?)";
        try(
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ) {
            pstmt.setString(1, username);
            pstmt.setString(2, shoppingCart.getUuid());
            pstmt.setBytes(3, serializeShoppingCart(shoppingCart));
            pstmt.execute();
        } catch (SQLException | IOException |SQLiteConnection.DatabaseConnectionException
                | SQLiteConnection.DatabaseQueryException e ) {
            throw new RuntimeException(e.getMessage());
        }
        return shoppingCart;
    }
    public static void addProductToShoppingCart(String username, String productId, int quantity) {
        ShoppingCart shoppingCart = getShoppingCart(username);
        Product product = null;
        try{
            if (ProductDao.doesProductExist(productId)) {
                product = ProductDao.getProduct(productId);
                assert product != null;
                if (shoppingCart.isProductInTheCart(productId)){
                    updateProduct(username, productId, quantity);
                    return;
                }
                if (ProductDao.getCurrentStock(productId) >= quantity){
                    product.setAvailableItems(quantity);
                    ProductDao.updateStock(productId, ProductDao.getCurrentStock(productId) - quantity);
                    shoppingCart.addProduct(product);
                    updateShoppingCart(username, shoppingCart);
                } else {
                    throw new IllegalStateException("Not enough items available for ID: " + productId);
                }
            } else {
                throw new IllegalStateException("Product must not be null for ID: " + productId);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
    public static void removeProductFromShoppingCart(String username, String productId, int quantity) {
        ShoppingCart shoppingCart = getShoppingCart(username);
 
        Product product = null;
        try{
            if (ProductDao.doesProductExist(productId)){
                ProductDao.updateStock(productId, ProductDao.getCurrentStock(productId) + quantity);
                shoppingCart.removeProduct(productId);
            } else {
                throw new IllegalStateException("Product must not be null for ID: " + productId);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void updateProduct(String username, String productId, int deviation){
        ShoppingCart shoppingCart = getShoppingCart(username);
        Product product = null;
        try{
            product = shoppingCart.getProductFromTheCart(productId);
            if (product == null) {
                throw new IllegalStateException("Product must not be null for ID: " + productId);
            }
            if (product.getAvailableItems() + (deviation) >= 0){
                int i0 = product.getAvailableItems();
                int i = product.getAvailableItems()+deviation;
                int i1 = ProductDao.getCurrentStock(productId) - deviation;
                if((deviation)<= ProductDao.getCurrentStock(productId)){
                    product.setAvailableItems(product.getAvailableItems()+(deviation));
                    shoppingCart.updateProduct(product);
                    ProductDao.updateStock(productId, ProductDao.getCurrentStock(productId) - (deviation));
                    updateShoppingCart(username, shoppingCart);
                } else {
                    throw new IllegalStateException("Not enough items available for ID: " + productId);
                }
            } else {
                removeProductFromShoppingCart(username, productId, (deviation));
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public static ArrayList<Product> getProductsInShoppingCart(String username) {
        ShoppingCart shoppingCart = getShoppingCart(username);
        return shoppingCart.getProducts();

    }
    public static double getTotalPrice(String username) {
        ShoppingCart shoppingCart = getShoppingCart(username);
        return shoppingCart.getTotalPrice();
    }
    public static double getDiscount(String username, double discountPercentage){
        ShoppingCart shoppingCart = getShoppingCart(username);
        return shoppingCart.getDiscount(discountPercentage);
    }

    public static  double getThreeItemsInSameCategoryDiscount(String username) {
    	ShoppingCart shoppingCart = getShoppingCart(username);
        ArrayList<Product> products = shoppingCart.getProducts();
        HashMap<ProductType, Integer> productCount = new HashMap<>();
        ArrayList<Product> moreThanThree = new ArrayList<>();
        for (Product product : products) {
            if (product.getType()==ProductType.Clothing){
                if (productCount.containsKey(ProductType.Clothing)) {
                    productCount.put(ProductType.Clothing, productCount.get(ProductType.Clothing) + product.getAvailableItems());
                } else {
                    productCount.put(ProductType.Clothing, product.getAvailableItems());
                }
            } else {
                if (productCount.containsKey(ProductType.Electronics)) {
                    productCount.put(ProductType.Electronics, productCount.get(ProductType.Electronics) + product.getAvailableItems());
                } else {
                    productCount.put(ProductType.Electronics, product.getAvailableItems());
                }
            }
        }
        double sum = 0;
        for (Product product : products) {
            if (productCount.get(product.getType()) >= 3) {
                sum += product.getAvailableItems() * product.getPrice();
            }
        }
        return sum * 0.2;
    }

    public static double getfirstPurchaseDiscount(String username){

        String sql = "SELECT COUNT(*) FROM shopping_carts WHERE username = ?";
        try(
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) == 1) {
                    ShoppingCart shoppingCart = getShoppingCart(username);
                    return ShoppingCartDao.getDiscount(username, 0.1);
                }
            }
        } catch (SQLiteConnection.DatabaseQueryException |SQLiteConnection.DatabaseConnectionException |SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public static double getFinalPrice(String username) {
        ShoppingCart shoppingCart = getShoppingCart(username);
        return shoppingCart.getTotalPrice() - getfirstPurchaseDiscount(username) - getThreeItemsInSameCategoryDiscount(username);
    }
    public static void clearShoppingCart(String username) {
        ShoppingCart shoppingCart = getShoppingCart(username);
        shoppingCart.clear();
    }

    public static ShoppingCart getShoppingCart(String username)  {

        String sql = "SELECT shoppingcart FROM shopping_carts WHERE username = ? AND finished = 0";
        try(
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                ShoppingCart shoppingCart = deserializeShoppingCart((byte[]) rs.getObject("shoppingcart"));
                assert shoppingCart != null;
                return shoppingCart;
            }
        } catch (SQLiteConnection.DatabaseQueryException e) {
            throw new RuntimeException(e);
        } catch (SQLiteConnection.DatabaseConnectionException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return createShoppingCart(username);
    }

    public static void updateShoppingCart(String username, ShoppingCart shoppingCart){
        String sql = "UPDATE shopping_carts SET shoppingcart = ? WHERE username = ?";
        try(
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ) {
            byte[] shoppingCarBlob = serializeShoppingCart(shoppingCart);
            pstmt.setString(2, shoppingCart.getUsername());
            pstmt.setBytes(1, shoppingCarBlob);
            pstmt.executeUpdate();
        } catch (SQLiteConnection.DatabaseQueryException e) {
            throw new RuntimeException(e);
        } catch (SQLiteConnection.DatabaseConnectionException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void checkout(String username){
        ShoppingCart shoppingCart = getShoppingCart(username);
 
        String sql = "UPDATE shopping_carts SET finished = 1 WHERE username = ?";
        try(
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ) {
            updateShoppingCart(username, shoppingCart);
            pstmt.setString(1, shoppingCart.getUsername());
            pstmt.execute();
        } catch (SQLiteConnection.DatabaseQueryException e) {
            throw new RuntimeException(e);
        } catch (SQLiteConnection.DatabaseConnectionException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getCurrentProductStock(String username, String productId) {
    	ShoppingCart shoppingCart = getShoppingCart(username);
    	return shoppingCart.getProductFromTheCart(productId).getAvailableItems();
    }

    private static byte[] serializeShoppingCart(ShoppingCart shoppingCart) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(shoppingCart);
        oos.close();
        baos.close();
        byte[] serializedShoppingCart = baos.toByteArray();
        return serializedShoppingCart;
    }

    private static ShoppingCart deserializeShoppingCart(byte[] serializedShoppingCart) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedShoppingCart);
        ObjectInputStream ois = new ObjectInputStream(bais);
        ShoppingCart shoppingCart = (ShoppingCart) ois.readObject();
        ois.close();
        bais.close();
        return shoppingCart;
    }

    public static class ShoppingCartDaoException extends RuntimeException {
        public ShoppingCartDaoException(String message) {
            super(message);
        }
    }



}
