package com.westminster.controller;

import com.westminster.dao.ProductDao;
import com.westminster.model.*;
import com.westminster.util.Validator;
import com.westminster.view.ProductView;

import java.util.Objects;

/**
 * This class contains methods for ProductController/ Console.
 */
public class ProductController {
    //instance variables
    ProductView productView;
    ProductDao productDao;

    /**
     * Constructor for ProductController
     */
    public ProductController() {
        super();
        this.productView = new ProductView();
        this.productDao = new ProductDao();

    }

    /**
     * Initiates creation of a new product when requested by the westminsterShoppingManagerController
     * @return Product
     */
    public Product createNewProduct() {
        String productID = Validator.validateProductID();
        return createProductHelper(productID);
    }

    /**
     * creates a new product when requested by the productController
     * contains step by step validation of user input
     * @param productID String
     * @return Product
     */
    private Product createProductHelper(String productID) {
        try {
            Product product;
            String productName = Validator.validateNonEmpty(productView, ProductView.PRODUCTNAMEPROMPT); //validate product name is non-empty
            int availableItems = Validator.validateAvailableItems(productView); //validate available items is an integer
            double price = Validator.validatePrice(productView); //validate price is a double
            ProductType type = Validator.validateProductType(productView); //validate product type is either clothing or electronics
            String brand = Validator.validateNonEmpty(productView, ProductView.BRANDPROMPT); //validate brand name is non-empty
            if (type == ProductType.Clothing) {
                ClothingSize size = Validator.validateClothingSize(productView, ProductView.SIZEPROMPT, ProductView.SIZEERROR); //validate clothing size is either S, M, L, XL, XXL
                String color = Validator.validateNonEmpty(productView, ProductView.COLORPROMPT); //validate color is non-empty
                product = new Clothing(productID, productName, availableItems, price, brand, size, color); //create clothing product
            } else {

                String model = Validator.validateBrandModelName(productView, ProductView.MODELPROMPT, ProductView.MODELERROR);
                String electronicType = Validator.validateBrandModelName(productView, ProductView.ELECTRONICPRODUCTTYPEPROMPT, ProductView.PRODUCTTYPEERROR);
                int warranty = Validator.validateWarranty(productView);
                product = new Electronics(productID, productName, availableItems, price, brand, model, electronicType, warranty);
            }
            return product;
        } catch (Exception e) {
            throw new ProductControllerException("Unable to create product: "+e.getMessage());
        }

    }

    /**
     * Adds a new product to the product list
     */
    public void addProduct() {
        Product product = createNewProduct();
        try {
            if (productDao.getProductCount() <= 50) {
                if (Validator.validateNewProduct(product)) {
                    if (product.getType() == ProductType.Clothing)
                        productDao.addProduct(product);
                    else
                        productDao.addProduct(product);
                    productView.printMessage("Product added successfully");
                    productView.printMessage("Here's the product information\n" + product.toStringConsole());
                }else{
                    productView.printError(ProductView.PRODUCTALREADYEXISTS);
                }
            } else {
                productView.printError(ProductView.MAXPRODUCT);
            }
        } catch (Exception e) {
            productView.printError(ProductControllerException.class.getName() + ": " + e.getMessage());
            throw new ProductControllerException("Unable to add product");
        }
    }

    /**
     * Deletes a product from the product list
     */
    public void deleteProduct() {
        try {
            String productId = Validator.validateProductID();
            if (productDao.doesProductExist(productId)) {
                productView.printMessage("This product will be deleted\n" + Objects.requireNonNull(productDao.getProduct(productId)).toStringConsole() + "\n");
                productDao.deleteProduct(productId);
                productView.printMessage("Product deleted successfully");
                productView.printMessage("Remaining amount of products: "+productDao.getProductCount());
            } else {
                productView.printError(ProductView.PRODUCTNOTFOUND);
            }
        } catch (Exception e) {
            productView.printError(ProductControllerException.class.getName() + ": " + e.getMessage());
            throw new ProductControllerException("Unable to delete product");
        }


    }

    /**
     * Prints all products in the product list
     */
    public void printAllProducts() {
        try {
            System.out.println("\nPrinting all products");
            System.out.println("Total number of products: " + productDao.getProductCount());
            for (Product product : productDao.getAllProducts()) {
                productView.printMessage(product.toStringConsole());
            }
        } catch (Exception e) {
            productView.printError(ProductControllerException.class.getName() + ": " + e.getMessage());
            throw new ProductControllerException("Unable to print all products");
        }
    }

    /**
     * Saves all products in the product list to a file
     */
    public void saveProducts() {
        try {
            productDao.saveProducts();
        } catch (Exception e) {
            productView.printError(ProductControllerException.class.getName() + ": " + e.getMessage());
            throw new ProductControllerException("Unable to save products");
        }
    }

    /**
     * Loads all products from a file
     */
    public void loadSavedProducts() {
        try {
            productDao.loadProducts();
        } catch (Exception e) {
            productView.printError(ProductControllerException.class.getName() + ": " + e.getMessage());
            throw new ProductControllerException("Unable to load products");
        }
    }

    /**
     * Custom exception for ProductController
     */
    private static class ProductControllerException extends RuntimeException {
        public ProductControllerException(String message) {
            super(message);
        }
    }
}
