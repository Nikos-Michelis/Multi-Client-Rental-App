package com.nick.client.entry;

import com.nick.client.validation.InputValidation;
import org.mindrot.jbcrypt.BCrypt;

import java.util.*;


public class RegisterManager {
    private Scanner scanner;
    List<Map<String, Object>> membersList = new ArrayList<>();
    List<Map<String, Object>> employeeList = new ArrayList<>();
    public RegisterManager(Scanner scanner){
        this.scanner = scanner;
    }

    public List<Map<String, Object>> initializeCustomerRegister(){
        InputValidation inputValidation = new InputValidation();
        String name;
        String surName;
        String email;
        String phone;
        String txtPassword;
        try {
            name = inputValidation.readName(scanner);
            surName = inputValidation.readLastName(scanner);
            email = inputValidation.readEmail(scanner);
            phone = inputValidation.readPhone(scanner);
            txtPassword = inputValidation.readPassword(scanner);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Map<String, Object> memberData = new HashMap<>();
        memberData.put("surName", surName);
        memberData.put("name", name);
        memberData.put("email", email);
        memberData.put("phone", phone);
        memberData.put("password", BCrypt.hashpw(txtPassword, BCrypt.gensalt(12)));
        memberData.put("role", "user");

        Map<String, Object> membersMap = new HashMap<>();
        membersMap.put("Member", memberData);
        membersList.add(membersMap);
        System.out.println("Returned Register: " + membersMap);
        return membersList;
    }
    public List<Map<String, Object>> initializeEmployeeRegister(){
        InputValidation inputValidation = new InputValidation();
        String name;
        String surName;
        String email;
        String phone;
        String txtPassword;
        try {
            name = inputValidation.readName(scanner);
            surName = inputValidation.readLastName(scanner);
            email = inputValidation.readEmail(scanner);
            phone = inputValidation.readPhone(scanner);
            txtPassword = inputValidation.readPassword(scanner);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Map<String, Object> memberData = new HashMap<>();
        memberData.put("surName", surName);
        memberData.put("name", name);
        memberData.put("email", email);
        memberData.put("phone", phone);
        memberData.put("password", BCrypt.hashpw(txtPassword, BCrypt.gensalt(12)));
        memberData.put("role", "employee");

        Map<String, Object> membersMap = new HashMap<>();
        membersMap.put("Member", memberData);
        employeeList.add(membersMap);
        return employeeList;
    }
}
