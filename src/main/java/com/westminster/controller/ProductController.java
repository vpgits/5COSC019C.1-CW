package com.westminster.controller;

import com.westminster.dao.ProductDao;
import com.westminster.model.*;
import com.westminster.util.Validator;
import com.westminster.view.ProductView;
import com.westminster.view.WestminsterShoppingManagerView;

public class ProductController {
    ProductView productView;

    public ProductController() {
        super();
        productView = ProductView.getInstance();
    }

    public Product createNewProduct()  {
        String productID = Validator.validateProductID(productView);
        return createProductHelper(productID);
    }

    private Product createProductHelper(String productID) {
        Product product;
        String productName = productView.callArgument(ProductView.PRODUCTNAMEPROMPT);
        int availableItems = Validator.validateAvailableItems(productView);
        double price = Validator.validatePrice(productView);
        ProductType type = Validator.validateProductType(productView);
        String brand = Validator.validateBrandModelName(productView, ProductView.BRANDPROMPT, ProductView.BRANDERROR);
        if (type == ProductType.Clothing) {
            ClothingSize size = Validator.validateClothingSize(productView, ProductView.SIZEPROMPT,ProductView.SIZEERROR);
            String color = Validator.validateBrandModelName(productView, ProductView.COLORPROMPT, ProductView.COLORERROR);
            product = new Clothing(productID, productName, availableItems, price, brand, size, color);
        } else {

            String model = Validator.validateBrandModelName(productView, ProductView.MODELPROMPT, ProductView.MODELERROR);
            String electronicType = Validator.validateBrandModelName(productView, ProductView.ELECTRONICPRODUCTTYPEPROMPT, ProductView.PRODUCTTYPEERROR);
            int warranty = Validator.validateWarranty(productView);
            product = new Electronics(productID, productName, availableItems, price, brand, model,electronicType, warranty);
        }
        return product;
    }


    public void addProduct()  {
        Product product = createNewProduct();
        try {
            if (ProductDao.getProductCount()<=50) {
                if (Validator.validateNewProduct(product)) {
                    if(product.getType() == ProductType.Clothing)
                        ProductDao.addProduct(product);
                    else
                        ProductDao.addProduct(product);
                    productView.printMessage("Product added successfully");
                    productView.printMessage("Here's the product information\n"+product.toStringConsole());
                }
            } else {
                productView.printError(ProductView.MAXPRODUCT);
            }
        } catch (Exception e) {
            productView.printError("Unable to add product");
            productView.printError(ProductControllerException.class.getName()+": " +e.getMessage());
        }
    }

    public void deleteProduct()  {
        try{
            String productId = Validator.validateProductID(productView);
            if (ProductDao.doesProductExist(productId)) {
                productView.printMessage("This product will be deleted\n"+ProductDao.getProduct(productId).toStringConsole()+"\n");
                ProductDao.deleteProduct(productId);
                productView.printMessage("Product deleted successfully");
            } else {
                productView.printError(ProductView.PRODUCTNOTFOUND);
            }
        } catch (Exception e){
            productView.printError("Unable to delete product");
            productView.printError(ProductControllerException.class.getName() + ": " + e.getMessage());
        }


    }


    public void printAllProducts() {
        try {
            for (Product product : ProductDao.getAllProducts()) {
                productView.printMessage(product.toStringConsole());
            }
        } catch (Exception e) {
            productView.printError("Unable to print all products");
            productView.printError(ProductControllerException.class.getName() + ": " + e.getMessage());
        }
    }

    private static class ProductControllerException extends RuntimeException {
        public ProductControllerException(String message) {
            super(message);
        }
    }
}
