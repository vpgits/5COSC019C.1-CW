package com.westminster.controller;

import com.westminster.dao.ProductDao;
import com.westminster.model.*;
import com.westminster.util.Validator;
import com.westminster.view.ProductView;
import java.util.Objects;

public class ProductController {
    ProductView productView;
    ProductDao productDao;

    public ProductController() {
        super();
        productView = new ProductView();
        productDao = new ProductDao();

    }

    public Product createNewProduct() {
        String productID = Validator.validateProductID(productView);
        return createProductHelper(productID);
    }

    private Product createProductHelper(String productID) {
        try{
        Product product;
        String productName = productView.callArgument(ProductView.PRODUCTNAMEPROMPT);
        int availableItems = Validator.validateAvailableItems(productView);
        double price = Validator.validatePrice(productView);
        ProductType type = Validator.validateProductType(productView);
        String brand = Validator.validateBrandModelName(productView, ProductView.BRANDPROMPT, ProductView.BRANDERROR);
        if (type == ProductType.Clothing) {
            ClothingSize size = Validator.validateClothingSize(productView, ProductView.SIZEPROMPT, ProductView.SIZEERROR);
            String color = Validator.validateBrandModelName(productView, ProductView.COLORPROMPT, ProductView.COLORERROR);
            product = new Clothing(productID, productName, availableItems, price, brand, size, color);
        } else {

            String model = Validator.validateBrandModelName(productView, ProductView.MODELPROMPT, ProductView.MODELERROR);
            String electronicType = Validator.validateBrandModelName(productView, ProductView.ELECTRONICPRODUCTTYPEPROMPT, ProductView.PRODUCTTYPEERROR);
            int warranty = Validator.validateWarranty(productView);
            product = new Electronics(productID, productName, availableItems, price, brand, model, electronicType, warranty);
        }
        return product;
        } catch (Exception e) {
            productView.printError(ProductControllerException.class.getName() + ": " + e.getMessage());
            throw new ProductControllerException("Unable to create product");
        }

    }


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
                }
            } else {
                productView.printError(ProductView.MAXPRODUCT);
            }
        } catch (Exception e) {
            productView.printError(ProductControllerException.class.getName() + ": " + e.getMessage());
            throw new ProductControllerException("Unable to add product");
        }
    }

    public void deleteProduct() {
        try {
            String productId = Validator.validateProductID(productView);
            if (productDao.doesProductExist(productId)) {
                productView.printMessage("This product will be deleted\n" + Objects.requireNonNull(productDao.getProduct(productId)).toStringConsole() + "\n");
                productDao.deleteProduct(productId);
                productView.printMessage("Product deleted successfully");
            } else {
                productView.printError(ProductView.PRODUCTNOTFOUND);
            }
        } catch (Exception e) {
            productView.printError(ProductControllerException.class.getName() + ": " + e.getMessage());
            throw new ProductControllerException("Unable to delete product");
        }


    }


    public void printAllProducts() {
        try {
            for (Product product : productDao.getAllProducts()) {
                productView.printMessage(product.toStringConsole());
            }
        } catch (Exception e) {
            productView.printError(ProductControllerException.class.getName() + ": " + e.getMessage());
            throw new ProductControllerException("Unable to print all products");
        }
    }

    public void saveProducts() {
        try {
            productDao.saveProducts();
            productView.printMessage("Products saved successfully");
        } catch (Exception e) {
            productView.printError(ProductControllerException.class.getName() + ": " + e.getMessage());
            throw new ProductControllerException("Unable to save products");
        }
    }

    public void loadSavedProducts() {
        try {
            productDao.loadProducts();
            productView.printMessage("Products loaded successfully");
        } catch (Exception e) {
            productView.printError(ProductControllerException.class.getName() + ": " + e.getMessage());
            throw new ProductControllerException("Unable to load products");
        }
    }

    private static class ProductControllerException extends RuntimeException {
        public ProductControllerException(String message) {
            super(message);
        }
    }
}
