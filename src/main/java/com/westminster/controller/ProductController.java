package com.westminster.controller;

import com.westminster.dao.ProductDao;
import com.westminster.model.*;
import com.westminster.util.Validator;
import com.westminster.view.ProductView;

public class ProductController {
    ProductView productView;

    public ProductController() {
        super();
        productView = new ProductView();
    }

    public Product createNewProduct() throws Exception {
        String productID = Validator.validateProductID(productView);
        return createProductHelper(productID);
    }

    public Product createProduct(String productID) throws Exception {
        return createProductHelper(productID);
    }

    private Product createProductHelper(String productID) {
        Product product;
        String productName = productView.callArgument(ProductView.PRODUCTNAMEPROMPT);
        int availableItems = Validator.validateAvailableItems(productView);
        double price = Validator.validatePrice(productView);
        ProductType type = Validator.validateProductType(productView);
        if (type == ProductType.Clothing) {
            String brand = Validator.validateBrandModelName(productView, ProductView.BRANDPROMPT, ProductView.BRANDERROR);
            ClothingSize size = Validator.validateClothingSize(productView, ProductView.SIZEPROMPT,ProductView.SIZEERROR);
            String color = Validator.validateBrandModelName(productView, ProductView.COLORPROMPT, ProductView.COLORERROR);
            product = new Clothing(productID, productName, availableItems, price, brand, size, color);
        } else {

            String brand = Validator.validateBrandModelName(productView, ProductView.BRANDPROMPT, ProductView.BRANDERROR);
            String model = Validator.validateBrandModelName(productView, ProductView.MODELPROMPT, ProductView.MODELERROR);
            String Electronictype = Validator.validateBrandModelName(productView, ProductView.ELECTRONICPRODUCTTYPEPROMPT, ProductView.PRODUCTTYPEERROR);
            int warranty = Validator.validateWarranty(productView);
            product = new Electronics(productID, productName, availableItems, price, brand, model,Electronictype, warranty);
        }
        return product;
    }


    public void addProduct() throws Exception {
        Product product = createNewProduct();
        try {
            if (ProductDao.isFull()) {
                if (Validator.validateNewProduct(product)) {
                    if(product.getType() == ProductType.Clothing)
                        ProductDao.addClothingProduct((Clothing) product);
                    else
                        ProductDao.addElectronicsProduct((Electronics) product);
                    productView.printMessage("Product added successfully");
                }
            } else {
                productView.printError(ProductView.MAXPRODUCT);
            }
        } catch (Exception e) {
            productView.printError(e.getMessage());
        }
    }

    public void updateProduct() throws Exception {
        Product product;
        try{
            String productID = Validator.validateProductID(productView);
            if (ProductDao.doesExist(productID)) {
                productView.printMessage(ProductView.PRODUCTFOUND);
                product = createProduct(productID);
                if (product.getType() == ProductType.Clothing) {
                    ProductDao.updateClothingProduct((Clothing) product);
                } else {
                    ProductDao.updateElectronicsProduct((Electronics) product);
                }
                productView.printMessage("Product updated successfully");
            } else {
                productView.printError(ProductView.PRODUCTNOTFOUND);
            }
        } catch (Exception e){
            productView.printError(e.getMessage());
        }


    }

    public void deleteProduct() throws Exception {
        try{
            String productID = Validator.validateProductID(productView);
            Product product = ProductDao.getProductFromHashmap(productID);
            assert product != null;
            ProductDao.deleteProduct(product);
            productView.printMessage("Product deleted successfully");
        } catch (Exception e){
            productView.printError(e.getMessage());
        }


    }


}
