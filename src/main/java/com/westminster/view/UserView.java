package com.westminster.view;


import java.util.Scanner;

public class UserView {
    private final Scanner scanner =new Scanner(System.in);
    private static UserView instance; //singleton instance
    public static final String EMAILPROMPT = "Enter the user email (username): ";
    public static final String PASSWORDHELPER = "Password should be 8-16 characters long and contain at least one " +
            "uppercase letter, one lowercase letter, one number and one special character";
    public static final String PASSWORDPROMPT = "Enter the user password."+PASSWORDHELPER+"\nPassword: ";
    public static final String USERCONFIRM = "User added successfully";
    public static final String MAXUSER = "Maximum number of users reached";



    public UserView() {
        super();
    }

    public static synchronized UserView getInstance() {
        if (instance == null) {
            instance = new UserView();
        }
        return instance;
    }

     public String callArgument(String prompt){
         System.out.print(prompt);
         return scanner.nextLine();
     }

     public void printError(String message){
         System.err.println(message);
     }
}
