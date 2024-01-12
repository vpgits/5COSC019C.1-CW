package com.westminster.util;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.westminster.dao.ProductDao;
import com.westminster.dao.UserDao;
import com.westminster.model.ClothingSize;
import com.westminster.model.Product;
import com.westminster.model.ProductType;
import com.westminster.view.ProductView;
import com.westminster.view.UserView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class contains methods to validate user input.
 */
public abstract class Validator {
    private static final ProductDao productDao = new ProductDao();
    private static final UserDao userDao = new UserDao();
    private static final ProductView productView = new ProductView();

    /**
     * This is a private constructor to prevent instantiation of the class.
     */
    private Validator() {
        throw new AssertionError("Utility class cannot be instantiated");
    }

    /**
     * validates the email address
     * Uses the Apache Commons Validator library to validate the email
     *
     * @return String email
     */
    public static String validateEmail(UserView userview) {
        boolean isValid = false;
        String email = null;
        while (!isValid) {
            try {
                email = userview.callArgument(UserView.EMAILPROMPT);
                //regex pattern for email validation
                String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
                if (!email.isBlank() && regexMatcher(email, regex)) {
                    isValid = true;
                } else {
                    throw new IllegalArgumentException("Invalid email");
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
                System.out.println("Email should be in the format of abc@def.com");
            }
        }
        return email;
    }

    /**
     * validates the password
     * Uses the BCrypt library to hash the password
     * Uses the Apache Commons Validator library to validate the password using a regex
     *
     * @return String bcryptHashString
     */
    public static String validatePassword(UserView userview) {
        boolean isValid = false;
        String bcryptHashString = null;
        //regex string for password valiadtion, min 8 characters , one lowercase, one uppercase, one number, one special character minimum
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$";
        while (!isValid) {
            try {

                String password = userview.callArgument(UserView.PASSWORDPROMPT);
                if (!password.isBlank() && regexMatcher(password, regex) && password.length() >= 8 && password.length() <= 16) {
                    //https://github.com/patrickfav/bcrypt
                    bcryptHashString = BCrypt.withDefaults().hashToString(12, password.toCharArray());
                    isValid = true;
                } else {
                    throw new IllegalArgumentException("Invalid password");
                }
            } catch (Exception e) {
                userview.printError(e.getMessage());
            }
        }
        return bcryptHashString;
    }

    /**
     * Utility method to match a string with a regex
     *
     * @param argument argument
     * @param regex    regex string
     * @return boolean
     */
    public static boolean regexMatcher(String argument, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(argument);
        return matcher.matches();
    }

    /**
     * checks if the password matches the hash
     * Uses the BCrypt library to verify the password
     *
     * @param username          username
     * @param passwordPlaintext password
     * @return boolean
     */
    public static boolean checkPasswordHash(String username, String passwordPlaintext) {
        String storedHash = userDao.getUserPasswordHash(username);
        if (null == storedHash || !storedHash.startsWith("$2a$"))
            throw new java.lang.IllegalArgumentException("Invalid hash provided for comparison");
        return BCrypt.verifyer().verify(passwordPlaintext.toCharArray(), storedHash).verified;
    }

    /**
     * checks if the user exists. Calls from the database
     *
     * @param username username
     * @return boolean
     */
    public static boolean doesUserExist(String username) {
        return userDao.doesExist(username);
    }

    /**
     * validation for the username
     *
     * @param userView UserView
     * @return boolean
     */
    public static String validateUsername(UserView userView) {
        try {
            String username = userView.callArgument(UserView.USERNAMEPROMPT);
            if (!username.isBlank()) {
                if (!doesUserExist(username)) {
                    return username;
                } else {
                    throw new IllegalArgumentException("Username already exists");
                }
            } else {
                throw new IllegalArgumentException("Invalid username");
            }
        } catch (Exception e) {
            userView.printError(e.getMessage());
            return validateUsername(userView);
        }
    }

    /**
     * validates the product name
     *
     * @param userView UserView
     * @param prompt   String
     * @return String productName
     */
    public static String validateNonEmpty(UserView userView, String prompt) {
        try {
            String arg = userView.callArgument(prompt);
            if (!arg.isBlank()) {
                return arg;
            } else {
                throw new IllegalArgumentException("Invalid argument");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return validateNonEmpty(userView, prompt);
        }
    }

    /**
     * validates the product name
     * checks if its blank
     *
     * @return String productName
     */
    public static String validateNonEmpty(ProductView productView, String prompt) {
        try {
            String arg = productView.callArgument(prompt);
            if (!arg.isBlank()) {
                return arg;
            } else {
                throw new IllegalArgumentException("Invalid argument");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return validateNonEmpty(productView, prompt);
        }
    }

    /**
     * validates a new product
     *
     * @param product product
     * @return booelan
     * @throws Exception exception
     */
    public static boolean validateNewProduct(Product product) {
        return productDao.getProductCount() < 50 && !productDao.doesProductExist(product.getProductID());
    }

    /**
     * validates the product ID
     * checks if its blank or contains 8 characters
     *
     * @return String productID
     */
    public static String validateProductID() {
        try {
            String productID = productView.callArgument(ProductView.PRODUCTIDPROMPT);
            if (!productID.isBlank() && productID.length() == Product.PRODUCTIDLENGTH) {
                return productID;
            } else {
                throw new IllegalArgumentException("Invalid product ID");
            }
        } catch (Exception e) {
            productView.printError(e.getMessage());
            return validateProductID();
        }
    }

    /**
     * validates the product stock at creation
     * checks if it's a positive integer
     *
     * @return String productName
     */
    public static int validateAvailableItems(ProductView productView) {
        try {
            int availableItems = Integer.parseInt(productView.callArgument(ProductView.AVAILABLEITEMSPROMPT));
            if (availableItems > 0) {
                return availableItems;
            } else {
                throw new IllegalArgumentException("Invalid available items please enter zero or more integers");
            }
        } catch (NumberFormatException e) {
            productView.printError("Please enter an integer for the available items");
            return validateAvailableItems(productView);
        } catch (Exception e) {
            productView.printError(e.getMessage());
            return validateAvailableItems(productView);
        }
    }

    /**
     * validates the product price at creation
     * checks if it's a positive double
     *
     * @return double price
     */
    public static double validatePrice(ProductView productView) {
        try {
            double price = Double.parseDouble(productView.callArgument(ProductView.PRICEPROMPT));
            if (price > 0) {
                return price;
            } else {
                throw new IllegalArgumentException("Invalid price please enter zero or more integers");
            }
        } catch (NumberFormatException e) {
            productView.printError("Please enter an integer or a floating point number for the price");
            return validatePrice(productView);
        } catch (Exception e) {
            productView.printError(e.getMessage());
            return validatePrice(productView);
        }
    }

    /**
     * validates the product brand, model name
     * checks if it's blank
     *
     * @return String brand
     */
    public static String validateBrandModelName(ProductView productView, String prompt, String errorMessage) {
        try {
            String brand = productView.callArgument(prompt);
            if (!brand.isBlank()) {
                return brand;
            } else {
                throw new IllegalArgumentException(errorMessage);
            }
        } catch (Exception e) {
            productView.printError(e.getMessage());
            return validateBrandModelName(productView, prompt, errorMessage);
        }
    }

    /**
     * validates the product type
     * checks if it's blank, converts the output to enum
     *
     * @return Enum ProductType
     */
    public static ProductType validateProductType(ProductView productView) {
        try {
            String type = productView.callArgument(ProductView.PRODUCTTYPEPROMPT);
            if (!type.isBlank() && (type.equalsIgnoreCase(ProductType.Clothing.toString()))) {
                return ProductType.Clothing;
            } else if (!type.isBlank() && (type.equalsIgnoreCase(ProductType.Electronics.toString()))) {
                return ProductType.Electronics;
            } else {
                throw new IllegalArgumentException("Invalid product type. Please enter either Clothing or Electronics");
            }
        } catch (Exception e) {
            productView.printError(e.getMessage());
            return validateProductType(productView);
        }
    }

    /**
     * validates the clothing size
     * checks if it's blank, converts the output to enum
     *
     * @return Enum ClothingSize
     */
    public static ClothingSize validateClothingSize(ProductView productView, String prompt, String errorMessage) {
        try {
            productView.printMessage("1. XS\n2. S\n3. M\n4. L\n5. XL\n6. XXL");
            int size = Integer.parseInt(productView.callArgument(prompt));
            if (size >= 1 && size <= 6) {
                return ClothingSize.values()[size - 1];
            } else {
                throw new NumberFormatException("Invalid size please enter a whole number between 1 and 6");
            }
        } catch (Exception e) {
            productView.printError(e.getMessage());
            return validateClothingSize(productView, prompt, errorMessage);
        }
    }

    /**
     * valiadte the warranty months
     *
     * @param productView ProductView
     * @return int warranty
     */
    public static int validateWarranty(ProductView productView) {
        try {
            int warranty = Integer.parseInt(productView.callArgument(ProductView.WARRANTYPROMPT));
            if (warranty >= 0) {
                return warranty;
            } else {
                throw new IllegalArgumentException("Invalid warranty please enter zero or more integers");
            }
        } catch (NumberFormatException e) {
            productView.printError("Please enter an integer for the warranty");
            return validateWarranty(productView);
        } catch (Exception e) {
            productView.printError(e.getMessage());
            return validateWarranty(productView);
        }
    }

    /**
     * validates the product ID
     *
     * @param password password
     * @return String bcryptHashString
     */
    public static String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }
}
