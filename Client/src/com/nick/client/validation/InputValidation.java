package com.nick.client.validation;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidation {
    private final String FULL_NAME_REGEX = "^[A-Za-z\\s]{2,}$";
    private final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private final String PHONE_REGEX = "^\\d{10}$";
    private  final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    private final String VEHICLE_REGEX = "^[A-Za-z\\d]{2,8}( [A-Za-z\\d]{2,6})?$";
    private final String ENUM_TYPE_REGEX = "^[A-Za-z_]{3,17}$";
    private final String INTEGER_REGEX = "\\d+";


    public String readName(Scanner sc) throws InterruptedException {
        return readStringField(sc, "Name", FULL_NAME_REGEX, "Name must not contain numbers and be 1-15 characters long");
    }
    public String readLastName(Scanner sc) throws InterruptedException {
        return readStringField(sc, "SurName", FULL_NAME_REGEX, "SurName must not contain numbers and be 1-15 characters long");
    }
    public String readEmail(Scanner sc) throws InterruptedException {
        return readStringField(sc, "Email", EMAIL_REGEX, "Email is not valid!");
    }
    public String readPhone(Scanner sc) throws InterruptedException {
        return readStringField(sc, "Phone", PHONE_REGEX, "Phone must be 10 characters long");
    }
    public String readPassword(Scanner sc) throws InterruptedException {
        return readStringField(sc, "password", PASSWORD_PATTERN, "Password must be at least 8 characters long, contain at least one uppercase and lowercase letter, one digit, one special character, and no whitespace characters");
    }
    public String readStringData(Scanner sc, String message) throws InterruptedException {
        return readStringField(sc, message, VEHICLE_REGEX, "Name must not contain numbers and be 3-8 characters long");
    }
    public String readStringEnum(Scanner sc, String message) throws InterruptedException {
        return readStringField(sc, message, ENUM_TYPE_REGEX, "Name must be 3-17 characters long");
    }
    public int readIntData(Scanner sc, String message) throws InterruptedException {
        return readIntField(sc, message, INTEGER_REGEX , "Only integers are allowed");
    }
    public boolean isValidInput(String input, String regex) {
        Pattern pattern;
        Matcher matcher;
        final String checkName = regex;

        pattern = Pattern.compile(checkName);
        matcher = pattern.matcher(input);
        return matcher.matches();
    }
    private String readStringField(Scanner sc, String fieldName, String regex, String errorMessage) throws InterruptedException {
        String inputString;
        while (true) {
            System.out.print("Give " + fieldName + ": ");
            if (sc.hasNext()) {
                inputString = sc.next();
                if (isValidInput(inputString, regex)) {
                    break;
                } else {
                    System.err.println(errorMessage);
                }
            } else {
                System.err.println("\nError... not valid input");
                sc.next();
            }
            Thread.sleep(400);
        }
        sc.nextLine();
        return inputString.trim();
    }
    private int readIntField(Scanner sc, String fieldName, String regex, String errorMessage) throws InterruptedException {
        int inputInt;
        while (true) {
            System.out.print("Give " + fieldName + ": ");
            if (sc.hasNextInt()) {
                inputInt = sc.nextInt();
                if (isValidInput(String.valueOf(inputInt), regex)) {
                    break;
                } else {
                    System.err.println(errorMessage);
                }
            } else {
                System.err.println("\nError... not valid input");
                sc.next();
            }
            Thread.sleep(400);
        }
        sc.nextLine();
        return inputInt;
    }
    public int readIntMenu(Scanner sc, int lower, int upper) {
        int choice;
        while(true) {
            System.out.print("Your choice: ");
            if (sc.hasNextInt()) {
                choice = sc.nextInt();
                if (choice < lower || choice > upper) {
                    System.out.println("Error: Between" + lower + " and " + upper );
                }
                return choice;
            }
            else {
                System.out.println("Error: Invalid input");
                sc.nextLine();
            }
        }
    }
    @Deprecated
    public String readString(Scanner sc, String message) {
        String input;
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        while (true) {
            System.out.print("\n" + message);
            input = sc.next();
            if (!input.isEmpty()) {
                break;
            } else {
                System.err.println("Error: Input cannot be empty.");
                sc.nextLine();
            }
        }
        return input.trim();
    }
    public String readAnswer(Scanner sc, String message) throws InterruptedException {
        while (true) {
            Thread.sleep(100);
            String choice = readString(sc, message);
            if (choice.equalsIgnoreCase("Y")) {
                return "Y";
            } else if (choice.equalsIgnoreCase("N")) {
                return "N";
            } else {
                System.err.print("\n" + "Error: Wrong input, allowed only Y or N " + "\n");
                sc.nextLine();
            }
        }
    }
}
