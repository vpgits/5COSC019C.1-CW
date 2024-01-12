package com.westminster.util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Connector class for SQLite database
 */
public class SQLiteConnection {

    private SQLiteConnection() {
        super();
    }

    /**
     * Connects to the sqlite database
     * Connect to the database
     * creates the database if it doesn't exist
     *
     * @return Connection object
     * @throws DatabaseConnectionException if connection fails
     */
    public static synchronized Connection connect() throws DatabaseConnectionException, DatabaseQueryException {
        //sqlite connection string
        String url = "jdbc:sqlite:src/main/resources/database/w1985483.sqlite";
        if (!new File("src/main/resources/database/w1985483.sqlite").exists()) {
            String userSql = """
                    create table user
                    (
                        username TEXT not null
                            constraint user_pk
                                primary key,
                        password TEXT not null,
                        name     TEXT,
                        email    TEXT
                    );
                    """;
            String productSql = """
                    create table product
                    (
                        productId      TEXT not null
                            constraint product_pk
                                primary key
                            constraint product_clothing_productID_fk
                                references clothing
                                on update cascade on delete cascade
                            constraint product_electronics_productID_fk
                                references electronics
                                on update cascade on delete cascade,
                        productName    TEXT not null,
                        availableItems integer,
                        price          REAL,
                        productType    TEXT not null
                    );
                    """;
            String clothingSql = """
                    create table clothing
                    (
                        productId    TEXT not null
                            constraint clothing_pk
                                primary key
                            constraint clothing_product_productId_fk
                                references product,
                        brand        TEXT not null,
                        clothingSize TEXT not null,
                        color        TEXT not null
                    );
                    """;
            String electronicsSql = """
                    create table electronics
                    (
                        productId TEXT    not null
                            constraint electronics_pk
                                primary key
                            constraint electronics_product_productID_fk
                                references product,
                        brand     TEXT    not null,
                        model     TEXT    not null,
                        type      TEXT    not null,
                        warranty  integer not null
                    );
                    """;
            String shoppingCartSql = """
                    create table shopping_carts
                    (
                        username     TEXT              not null,
                        uuid         integer TEXT      not null,
                        finished     integer default 0 not null,
                        shoppingcart BLOB,
                        constraint shopping_carts_pk
                            primary key (uuid, username)
                    );
                    """;
            try {
                Connection conn = DriverManager.getConnection(url);
                conn.createStatement().execute(userSql);
                conn.createStatement().execute(productSql);
                conn.createStatement().execute(clothingSql);
                conn.createStatement().execute(electronicsSql);
                conn.createStatement().execute(shoppingCartSql);
                return conn;
            } catch (SQLException e) {
                throw new DatabaseConnectionException("Error connecting to database: " + e.getMessage());
            } catch (Exception e) {
                throw new DatabaseQueryException("Database query error: " + e.getMessage());
            }
        } else {
            try {
                Connection conn = DriverManager.getConnection(url);
                return conn;
            } catch (SQLException e) {
                throw new DatabaseConnectionException("Error connecting to database: " + e.getMessage());
            } catch (Exception e) {
                throw new DatabaseQueryException("Database query error: " + e.getMessage());
            }
        }
    }

    public static class DatabaseConnectionException extends Exception {
        public DatabaseConnectionException(String message) {
            super(message);
        }
    }

    public static class DatabaseQueryException extends Exception {
        public DatabaseQueryException(String message) {
            super(message);
        }
    }
}


