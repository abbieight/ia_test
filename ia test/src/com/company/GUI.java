package com.company;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import java.awt.event.*; // need this import for the DocumentListener

public class GUI implements ActionListener, DocumentListener {

    private final int FRAME_WIDTH = 400;
    private final int FRAME_HEIGHT = 500;
    private final int LEFT_MARGIN = 100;
    private final int TOP_MARGIN = 20;
    private final int BUTTON_Y = 150;
    private final int BUTTON_WIDTH = 100;
    private final int BUTTON_HEIGHT = 40;
    private final int TEXT_WIDTH = 200;
    private final int TEXT_HEIGHT = 30;

    private JFrame frame;
    private JTextField userNameField;
    private JPasswordField passwordField;
    private JButton okButton;
    private JButton cancelButton;
    public static ArrayList<String> userNames = new ArrayList<>();
    public static ArrayList<String> passwords = new ArrayList<>();

    private int userNameLength = 0; // set username length to 0
    private int passwordLength = 0; // set password length to 0

    public GUI() {
        frame = new JFrame();

        readUserNames();
        readPassWords();

        // Instructions on a label
        JLabel instructions = new JLabel("Enter your user name:");
        instructions.setBounds(LEFT_MARGIN, TOP_MARGIN, TEXT_WIDTH, TEXT_HEIGHT);
        frame.add(instructions);

        // Input text field
        userNameField = new JTextField();
        userNameField.setBounds(LEFT_MARGIN, TOP_MARGIN + TEXT_HEIGHT, TEXT_WIDTH, TEXT_HEIGHT);
        userNameField.getDocument().addDocumentListener(this);
        userNameField.getDocument().putProperty("name", "username");
        frame.add(userNameField);

        // Instructions on a label
        JLabel passwordInstructions = new JLabel("Enter your password:");
        passwordInstructions.setBounds(LEFT_MARGIN, TOP_MARGIN + (2*TEXT_HEIGHT), TEXT_WIDTH, TEXT_HEIGHT);
        frame.add(passwordInstructions);

        // Input password field
        passwordField = new JPasswordField();
        passwordField.setBounds(LEFT_MARGIN, TOP_MARGIN + (3*TEXT_HEIGHT), TEXT_WIDTH, TEXT_HEIGHT);
        passwordField.getDocument().addDocumentListener(this);
        passwordField.getDocument().putProperty("name", "password");
        frame.add(passwordField);

        // ok button
        okButton = new JButton("OK");
        okButton.setBounds(LEFT_MARGIN, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        okButton.addActionListener(this);
        okButton.setEnabled(false);
        frame.add(okButton);

        // cancel button
        cancelButton = new JButton("CANCEL");
        cancelButton.setBounds(LEFT_MARGIN+BUTTON_WIDTH+10, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        cancelButton.addActionListener(this);
        cancelButton.setEnabled(false);
        frame.add(cancelButton);

        // other frame attributes
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    public static void readUserNames(){
        try(BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\abbie\\Documents\\abbie\\ib\\computer science\\ia\\usernames.txt"))) {
                String line = br.readLine();
                while (line != null) {
                    userNames.add(line);
                    line = br.readLine();
                }
        }
        catch(IOException e){
            e.printStackTrace();

        }
    }

    public static void readPassWords(){
        try(BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\abbie\\Documents\\abbie\\ib\\computer science\\ia\\usernames.txt"))) {
            String line = br.readLine();
            while (line != null) {
                passwords.add(line);
                line = br.readLine();
            }
        }
        catch(IOException e){
            e.printStackTrace();

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("OK")) {
            // if ok button is pressed then check password is correct
            if(checkCredentials()) {
                String userName = userNameField.getText();
                System.out.println("Welcome, " + userName);
            }else{
                System.out.println("passwords do not match!");
            }
        }
        if(e.getActionCommand().equals("CANCEL")){
            // if cancel button is pressed then set both text fields to empty
            // set both lengths to zero and make sure the cancel button is not enabled
            userNameField.setText("");
            passwordField.setText("");
            userNameLength = 0;
            passwordLength = 0;
            cancelButton.setEnabled(false);
        }
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        // triggers when the style of the text changes
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        // when a character is removed, run a method to update the length
        updateLength(e, "remove");

        // if either of the text fields have a length of zero, the ok button cannot be pressed
        if((userNameLength == 0) || (passwordLength == 0)){
            okButton.setEnabled(false);
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        // when a character is added, run a method to update the length
        updateLength(e, "insert");

        // if both of the text fields have any text, the ok button can be pressed
        if((userNameLength > 0) && (passwordLength > 0)){
            okButton.setEnabled(true);
        }

        // if either of the text fields have text, then the cancel button can be pressed
        if((userNameLength > 0) || (passwordLength > 0)){
            cancelButton.setEnabled(true);
        }
    }

    private void updateLength(DocumentEvent e, String action) {
        Document doc = (Document)e.getDocument();
        // update length of text in username field area according to the parameters given
        if(action.equals("insert") && (doc.getProperty("name").equals("username"))){
            userNameLength = userNameLength + e.getLength();
        } else if(action.equals("remove") && (doc.getProperty("name").equals("username"))){
            userNameLength = userNameLength - e.getLength();
        }

        // update length of text in password field area according to parameters given
        if(action.equals("insert") && (doc.getProperty("name").equals("password"))){
            passwordLength = passwordLength + e.getLength();
        } else if(action.equals("remove") && (doc.getProperty("name").equals("password"))){
            passwordLength = passwordLength - e.getLength();
        }
    }

    private boolean checkCredentials(){
        // check the user's password against text file of passwords
        for(int i=0; i<userNames.size(); i++){
            if(userNameField.getText().equals(userNames.get(i))){
                // if the username is found, check against corresponding password
                if(passwordField.getText().equals(passwords.get(i))){
                    // if match, display message
                    System.out.println("username and password match!");
                    return true;
                } else{
                    // this means the username exists, but the wrong password has been typed
                    System.out.println("password does not match!");
                    return false;
                }
            }
        }
        // the username is wrong
        System.out.println("username not found!");
        return false;
    }

}

