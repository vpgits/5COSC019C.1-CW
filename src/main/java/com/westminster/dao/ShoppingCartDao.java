package com.westminster.dao;


import com.westminster.model.Product;
import com.westminster.model.ProductType;
import com.westminster.model.ShoppingCart;
import com.westminster.util.SQLiteConnection;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Data Access Object for the shopping cart.
 */
public class ShoppingCartDao {
    ProductDao productDao; // productDao is used to access the product table in the database.

    /**
     * Constructor for the ShoppingCartDao class.
     */
    public ShoppingCartDao() {
        super();
        productDao = new ProductDao();
    }

    /**
     * Creates a new shopping cart for the user.
     * @param username The username of the user.
     * @return The shopping cart.
     */
    public ShoppingCart createShoppingCart(String username) {
        ShoppingCart shoppingCart = new ShoppingCart(username);
        String sql = "INSERT INTO shopping_carts (username, uuid, shoppingcart) VALUES (?, ?, ?)";
        try (
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, username);
            pstmt.setString(2, shoppingCart.getUuid());
            pstmt.setBytes(3, serializeShoppingCart(shoppingCart));
            pstmt.execute();
        } catch (SQLException | IOException | SQLiteConnection.DatabaseConnectionException
                 | SQLiteConnection.DatabaseQueryException e) {
            throw new ShoppingCartDaoException("Unable to create shopping cart: " + e.getMessage());
        }
        return shoppingCart;
    }

    /**
     * Adds a product to the shopping cart.
     *
     * @param username  The username of the user.
     * @param productId The ID of the product.
     * @param quantity  The quantity of the product.
     */
    public void addProductToShoppingCart(String username, String productId, int quantity) {
        ShoppingCart shoppingCart = getShoppingCart(username);
        Product product = null;
        try {
            if (productDao.doesProductExist(productId)) {
                product = productDao.getProduct(productId);
                assert product != null;
                if (shoppingCart.isProductInTheCart(productId)) {
                    updateProduct(username, productId, quantity);
                    return;
                }
                if (productDao.getCurrentStock(productId) >= quantity) {
                    product.setAvailableItems(quantity);
                    productDao.updateStock(productId, productDao.getCurrentStock(productId) - quantity);
                    shoppingCart.addProduct(product);
                    updateShoppingCart(username, shoppingCart);
                } else {
                    throw new IllegalStateException("Not enough items available for ID: " + productId);
                }
            } else {
                throw new IllegalStateException("Product must not be null for ID: " + productId);
            }
        } catch (Exception e) {
            throw new ShoppingCartDaoException("Unable to add product: " + e.getMessage());
        }
    }

    /**
     * Removes a product from the shopping cart.
     *
     * @param username  The username of the user.
     * @param productId The ID of the product.
     * @param quantity  The quantity of the product.
     */
    public void removeProductFromShoppingCart(String username, String productId, int quantity) {
        ShoppingCart shoppingCart = getShoppingCart(username);

        try {
            if (productDao.doesProductExist(productId)) {
                productDao.updateStock(productId, productDao.getCurrentStock(productId) + quantity);
                shoppingCart.removeProduct(productId);
                updateShoppingCart(username, shoppingCart);
            } else {
                throw new IllegalStateException("Product must not be null for ID: " + productId);
            }
        } catch (Exception e) {
            throw new ShoppingCartDaoException("Unable to remove product: " + e.getMessage());
        }
    }

    /**
     * Updates the quantity of a product in the shopping cart.
     *
     * @param username  The username of the user.
     * @param productId The ID of the product.
     * @param deviation The deviation of the quantity.
     */
    public void updateProduct(String username, String productId, int deviation) {
        ShoppingCart shoppingCart = getShoppingCart(username);
        Product product = null;
        try {
            product = shoppingCart.getProductFromTheCart(productId);
            if (product == null) {
                throw new IllegalStateException("Product must not be null for ID: " + productId);
            }
            if (product.getAvailableItems() + (deviation) >= 0) {
                if ((deviation) <= productDao.getCurrentStock(productId)) {
                    product.setAvailableItems(product.getAvailableItems() + (deviation));
                    shoppingCart.updateProduct(product);
                    productDao.updateStock(productId, productDao.getCurrentStock(productId) - (deviation));
                    updateShoppingCart(username, shoppingCart);
                } else {
                    throw new IllegalStateException("Not enough items available for ID: " + productId);
                }
            } else {
                removeProductFromShoppingCart(username, productId, (deviation));
            }
        } catch (Exception e) {
            throw new ShoppingCartDaoException("Unable to update product: " + e.getMessage());
        }
    }

    /**
     * Gets the products in the shopping cart.
     *
     * @param username The username of the user.
     * @return an arraylist of products in the shopping cart.
     */
    public ArrayList<Product> getProductsInShoppingCart(String username) {
        ShoppingCart shoppingCart = getShoppingCart(username);
        return shoppingCart.getProducts();
    }

    /**
     * Gets the total price of the products in the shopping cart.
     *
     * @param username The username of the user.
     * @return The total price of the products in the shopping cart.
     */
    public double getTotalPrice(String username) {
        ShoppingCart shoppingCart = getShoppingCart(username);
        return shoppingCart.getTotalPrice();
    }

    /**
     * Gets the discount by having a percentage of the total price.
     *
     * @param username           The username of the user.
     * @param discountPercentage The percentage of the total price as decimals
     * @return The discounted amount
     */
    public double getDiscount(String username, double discountPercentage) {
        ShoppingCart shoppingCart = getShoppingCart(username);
        return shoppingCart.getDiscount(discountPercentage);
    }

    /**
     * Custom three items in the same category discount.
     *
     * @param username The username of the user.
     * @return The discounted amount
     */
    public double getThreeItemsInSameCategoryDiscount(String username) {
        ShoppingCart shoppingCart = getShoppingCart(username);
        ArrayList<Product> products = shoppingCart.getProducts();
        HashMap<ProductType, Integer> productCount = new HashMap<>();
        for (Product product : products) {
            if (product.getType() == ProductType.Clothing) {
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

    /**
     * Custom first purchase discount.
     *
     * @param username The username of the user.
     * @return The discounted amount
     */
    public double getfirstPurchaseDiscount(String username) {

        String sql = "SELECT COUNT(*) FROM shopping_carts WHERE username = ?";
        try (
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) == 1) {
                    ShoppingCart shoppingCart = getShoppingCart(username);
                    return shoppingCart.getDiscount(0.1);
                }
            }
        } catch (SQLiteConnection.DatabaseQueryException | SQLiteConnection.DatabaseConnectionException |
                 SQLException e) {
            throw new ShoppingCartDaoException("Unable to get first purchase discount: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Gets the final price of the products in the shopping cart.
     * @param username The username of the user.
     * @return The final price of the products in the shopping cart.
     */
    public double getFinalPrice(String username) {
        ShoppingCart shoppingCart = getShoppingCart(username);
        return shoppingCart.getTotalPrice() - getfirstPurchaseDiscount(username) - getThreeItemsInSameCategoryDiscount(username);
    }

    /**
     * Clears the shopping cart.
     * @param username username
     */
    public void clearShoppingCart(String username) {
        ShoppingCart shoppingCart = getShoppingCart(username);
        shoppingCart.clear();
    }

    /**
     * Gets the shopping cart, using the username.
     * @param username The username of the user.
     * @return
     */
    public ShoppingCart getShoppingCart(String username) {

        String sql = "SELECT shoppingcart FROM shopping_carts WHERE username = ? AND finished = 0";
        try (
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                ShoppingCart shoppingCart = deserializeShoppingCart((byte[]) rs.getObject("shoppingcart"));
                assert shoppingCart != null;
                return shoppingCart;
            }
        } catch (SQLiteConnection.DatabaseQueryException | SQLiteConnection.DatabaseConnectionException |SQLException
                | IOException | ClassNotFoundException |NullPointerException e) {
            throw new ShoppingCartDaoException("Unable to get shopping cart: " + e.getMessage());
        }
        return createShoppingCart(username);
    }

    /**
     * Updates the shopping cart.
     *
     * @param username     The username of the user.
     * @param shoppingCart The shopping cart object.
     */
    public void updateShoppingCart(String username, ShoppingCart shoppingCart) {
        String sql = "UPDATE shopping_carts SET shoppingcart = ? WHERE username = ?";
        try (
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            byte[] shoppingCarBlob = serializeShoppingCart(shoppingCart);
            pstmt.setString(2, shoppingCart.getUsername());
            pstmt.setBytes(1, shoppingCarBlob);
            pstmt.executeUpdate();
        } catch (SQLiteConnection.DatabaseQueryException | SQLiteConnection.DatabaseConnectionException | SQLException |
                 IOException e) {
            throw new ShoppingCartDaoException("Unable to update shopping cart: " + e.getMessage());
        }
    }

    /**
     * Checks out the shopping cart.
     *
     * @param username The username of the user.
     */
    public void checkout(String username) {
        ShoppingCart shoppingCart = getShoppingCart(username);

        String sql = "UPDATE shopping_carts SET finished = 1 WHERE username = ?";
        try (
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            updateShoppingCart(username, shoppingCart);
            pstmt.setString(1, shoppingCart.getUsername());
            pstmt.execute();
        } catch (SQLiteConnection.DatabaseQueryException | SQLiteConnection.DatabaseConnectionException |
                 SQLException e) {
            throw new ShoppingCartDaoException("Unable to checkout shopping cart: " + e.getMessage());
        }
    }

    /**
     * Gets the current stock of a product.
     *
     * @param username  username of the user
     * @param productId id of the product
     * @return the current stock of the product
     */
    public int getCurrentProductStock(String username, String productId) {
        ShoppingCart shoppingCart = getShoppingCart(username);
        return shoppingCart.getProductFromTheCart(productId).getAvailableItems();
    }

    /**
     * Serializes the shopping cart.
     *
     * @param shoppingCart The shopping cart object.
     * @return The serialized shopping cart.
     * @throws IOException If the shopping cart cannot be serialized.
     */
    private byte[] serializeShoppingCart(ShoppingCart shoppingCart) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(shoppingCart);
        oos.close();
        baos.close();
        return baos.toByteArray();
    }

    /**
     * Deserializes the shopping cart.
     *
     * @param serializedShoppingCart The serialized shopping cart.
     * @return ShoppingCart
     * @throws IOException            If the shopping cart cannot be deserialized.
     * @throws ClassNotFoundException If the shopping cart class cannot be found.
     */
    private ShoppingCart deserializeShoppingCart(byte[] serializedShoppingCart) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedShoppingCart);
        ObjectInputStream ois = new ObjectInputStream(bais);
        ShoppingCart shoppingCart = (ShoppingCart) ois.readObject();
        ois.close();
        bais.close();
        return shoppingCart;
    }

    /**
     * removes the shopping cart from the database
     *
     * @param username username of the user
     */
    public void removeShoppingCart(String username) {
        String sql = "DELETE FROM shopping_carts WHERE username = ?";
        try (
                Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, username);
            pstmt.execute();
        } catch (Exception e) {
            throw new ShoppingCartDaoException("Unable to remove shopping cart: " + e.getMessage());
        }
    }

    /**
     * Exception class for the ShoppingCartDao.
     */
    public static class ShoppingCartDaoException extends RuntimeException {
        public ShoppingCartDaoException(String message) {
            super(message);
        }
    }


}
