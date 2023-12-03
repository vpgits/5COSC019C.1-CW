package com.westminster.util;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.westminster.dao.ProductDao;
import com.westminster.model.Product;
import com.westminster.view.UserView;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Validator {
    static Scanner scanner;
    private Validator(){
        throw new AssertionError("Utility class cannot be instantiated");
    }
    /**
     * validates the email address
     * Uses the Apache Commons Validator library to validate the email
     * @return String email
     */
    public static String validateEmail(){
        //TODO check against the database if the email is already taken. create a hashmap with email as the key and the user object as the value
        boolean isValid = false;
        String email = null;
        while(!isValid){
            try{
                email = UserView.getInstance().callArgument(UserView.EMAILPROMPT);
                //regex pattern for email validation
                String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
                if (!email.isBlank() && regexMatcher(email,regex )){
                    isValid = true;
                } else {
                    throw new IllegalArgumentException("Invalid email");
                }
            } catch (Exception e){
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
     * @return String bcryptHashString
     */
    public static String validatePassword(){
        boolean isValid = false;
        String bcryptHashString = null;
        //regex string for password valiadtion, min 8 characters , one lowercase, one uppercase, one number, one special character minimum
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$";
        while(!isValid){
            try{

                String password = UserView.getInstance().callArgument(UserView.PASSWORDPROMPT);
                if (!password.isBlank() && regexMatcher(password, regex) && password.length() >= 8
                        && password.length() <= 16){
                    //https://github.com/patrickfav/bcrypt
                    bcryptHashString = BCrypt.withDefaults().hashToString(12, password.toCharArray());
                    isValid = true;
                } else {
                    throw new IllegalArgumentException("Invalid password");
                }
            } catch (Exception e){
                UserView.getInstance().printError(e.getMessage());
            }
        }
        return bcryptHashString;
    }
    private static boolean regexMatcher(String argument, String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(argument);
        return matcher.matches();
    }
    public static boolean checkPasswordHash(String password_plaintext, String stored_hash) {
        if(null == stored_hash || !stored_hash.startsWith("$2a$"))
            throw new java.lang.IllegalArgumentException("Invalid hash provided for comparison");
        return BCrypt.verifyer().verify(password_plaintext.toCharArray(), stored_hash).verified;

    }

    /**
     * validates a new product
     * @param product product
     * @return booelan
     * @throws Exception exception
     */
    public static boolean validateNewProduct(Product product) throws Exception {
        return !ProductDao.isFull() && !ProductDao.doesExist(product);
    }

    public static boolean validateProduct(Product product) throws Exception {
        return ProductDao.doesExist(product);
    }
}
