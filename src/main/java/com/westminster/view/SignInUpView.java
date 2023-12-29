package com.westminster.view;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.westminster.dao.ProductDao;
import com.westminster.dao.UserDao;
import com.westminster.util.Validator;

public class SignInUpView extends JFrame {

    public SignInUpView() {
        initComponents();
    }

    private void initComponents()  {
        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel signInPanel = new JPanel(new GridBagLayout());
        JPanel signUpPanel = new JPanel(new GridBagLayout());

        // Sign In Components
        GridBagConstraints signInConstraints = new GridBagConstraints();
        signInConstraints.anchor = GridBagConstraints.WEST;
        signInConstraints.insets = new Insets(5, 5, 5, 5);

        JLabel signInUsernameLabel = new JLabel("Username");
        JTextField signInUsernameField = new JTextField(16);
        signInConstraints.gridx = 0;
        signInConstraints.gridy = 0;
        signInPanel.add(signInUsernameLabel, signInConstraints);
        signInConstraints.gridx = 1;
        signInConstraints.gridy = 0;
        signInPanel.add(signInUsernameField, signInConstraints);

        JLabel signInPasswordLabel = new JLabel("Password");
        JPasswordField signInPasswordField = new JPasswordField(16);
        signInConstraints.gridx = 0;
        signInConstraints.gridy = 1;
        signInPanel.add(signInPasswordLabel, signInConstraints);
        signInConstraints.gridx = 1;
        signInConstraints.gridy = 1;
        signInPanel.add(signInPasswordField, signInConstraints);

        signInConstraints.gridwidth = 2;
        JButton signInButton = new JButton("Sign In");
        signInConstraints.gridx = 1;
        signInConstraints.gridy = 2;
        signInPanel.add(signInButton, signInConstraints);

        JLabel signInMessageLabel = new JLabel();
        signInConstraints.gridy = 3;
        signInPanel.add(signInMessageLabel, signInConstraints);

        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String username = signInUsernameField.getText();
                String password = String.valueOf(signInPasswordField.getPassword());
                if (username.isBlank() && password.isBlank()){
                    signInMessageLabel.setText("Please fill in all fields.");
                }
                if (!username.isBlank() && !password.isBlank() && Validator.doesUserExist(username) && Validator.checkPasswordHash(username, password)) {
                    signInMessageLabel.setText("User logged in successfully.");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        Window currentWindow =  SwingUtilities.getWindowAncestor((Component) event.getSource());
                        if (currentWindow != null) {
                            currentWindow.dispose();
                        }
                        try {
                            new WestminsterShoppingCenterGuiView(username);
                        } catch (ProductDao.ProductDaoException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                } else {
                    System.out.println("User does not exist");
                }
                System.out.println(username + " " + password);
                
            }
        });

        // Sign Up Components
        GridBagConstraints signUpConstraints = new GridBagConstraints();
        signUpConstraints.anchor = GridBagConstraints.WEST;
        signUpConstraints.insets = new Insets(5, 5, 5, 5);

        JLabel signUpUsernameLabel = new JLabel("Username");
        JTextField signUpUsernameField = new JTextField(16);
        signUpConstraints.gridx = 0;
        signUpConstraints.gridy = 0;
        signUpPanel.add(signUpUsernameLabel, signUpConstraints);
        signUpConstraints.gridx = 1;
        signUpConstraints.gridy = 0;
        signUpPanel.add(signUpUsernameField, signUpConstraints);

        JLabel signUpPasswordLabel = new JLabel("Password");
        JPasswordField signUpPasswordField = new JPasswordField(16);
        signUpConstraints.gridx = 0;
        signUpConstraints.gridy = 1;
        signUpPanel.add(signUpPasswordLabel, signUpConstraints);
        signUpConstraints.gridx = 1;
        signUpConstraints.gridy = 1;
        signUpPanel.add(signUpPasswordField, signUpConstraints);

        JLabel signUpFirstNameLabel = new JLabel("First Name");
        JTextField signUpFirstNameField = new JTextField(16);
        signUpConstraints.gridx = 0;
        signUpConstraints.gridy = 2;
        signUpPanel.add(signUpFirstNameLabel, signUpConstraints);
        signUpConstraints.gridx = 1;
        signUpConstraints.gridy = 2;
        signUpPanel.add(signUpFirstNameField, signUpConstraints);

        JLabel signUpLastNameLabel = new JLabel("Last Name");
        JTextField signUpLastNameField = new JTextField(16);
        signUpConstraints.gridx = 0;
        signUpConstraints.gridy = 3;
        signUpPanel.add(signUpLastNameLabel, signUpConstraints);
        signUpConstraints.gridx = 1;
        signUpConstraints.gridy = 3;
        signUpPanel.add(signUpLastNameField, signUpConstraints);

        JLabel signUpEmailLabel = new JLabel("Email");
        JTextField signUpEmailField = new JTextField(16);
        signUpConstraints.gridx = 0;
        signUpConstraints.gridy = 4;
        signUpPanel.add(signUpEmailLabel, signUpConstraints);
        signUpConstraints.gridx = 1;
        signUpConstraints.gridy = 4;
        signUpPanel.add(signUpEmailField, signUpConstraints);

        JLabel signUpConfirmPasswordLabel = new JLabel("Confirm Password");
        JPasswordField signUpConfirmPasswordField = new JPasswordField(16);
        signUpConstraints.gridx = 0;
        signUpConstraints.gridy = 5;
        signUpPanel.add(signUpConfirmPasswordLabel, signUpConstraints);
        signUpConstraints.gridx = 1;
        signUpConstraints.gridy = 5;
        signUpPanel.add(signUpConfirmPasswordField, signUpConstraints);

        JButton signUpButton = new JButton("Sign Up");
        signUpConstraints.gridx = 1;
        signUpConstraints.gridy = 6;
        signUpConstraints.gridwidth = 2;
        signUpPanel.add(signUpButton, signUpConstraints);

        JLabel signUpMessageLable = new JLabel();
        signUpConstraints.gridy = 9;
        signUpPanel.add(signUpMessageLable, signUpConstraints);

        // Sign Up Button ActionListener
        signUpButton.addActionListener(new ActionListener()  {
            @Override
            public void actionPerformed(ActionEvent event){
                String username = signUpUsernameField.getText();
                String password = String.valueOf(signUpPasswordField.getPassword());
                String firstName = signUpFirstNameField.getText();
                String lastName = signUpLastNameField.getText();
                String email  =  signUpEmailField.getText();
                if (username.isBlank() || password.isBlank() || firstName.isBlank() || lastName.isBlank() || email.isBlank()) {
                    signUpMessageLable.setText("Please fill in all fields.");
                }
                if (Validator.doesUserExist(username)) {
                    signUpMessageLable.setText("Username already exists.");
                } else if (!Validator.regexMatcher(email, "^[A-Za-z0-9+_.-]+@(.+)$")){
                    signUpMessageLable.setText("Please enter a valid email");
                }
                if (!Validator.regexMatcher(password, "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$")){
                    signUpMessageLable.setText("Please enter a valid password");    
                }
                try {
                    UserDao.addUser(username, password,firstName ,lastName, email);
                    signUpMessageLable.setText("User Added");
                } catch (UserDao.UserDaoException e) {
                    signUpMessageLable.setText("Something went wrong.");
                }
                
            }
        });

        tabbedPane.addTab("Sign In", signInPanel);
        tabbedPane.addTab("Sign Up", signUpPanel);

        add(tabbedPane);
        setSize(350, 300);
        setTitle("Welcome");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SignInUpView::new);
    }
}