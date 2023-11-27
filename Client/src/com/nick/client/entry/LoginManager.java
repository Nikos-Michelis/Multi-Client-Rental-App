package com.nick.client.entry;

import com.nick.client.validation.InputValidation;
import java.util.*;

public class LoginManager {
    private Scanner scanner;
    List<Map<String, Object>> membersList = new ArrayList<>();
    List<Map<String, Object>> employeeList = new ArrayList<>();
    public LoginManager(Scanner scanner){
        this.scanner = scanner;
    }

    public List<Map<String, Object>> initializeCustomerLogin(){
        InputValidation inputValidation = new InputValidation();
        String email;
        String password;

        try {
            email = inputValidation.readEmail(scanner);
            password = inputValidation.readPassword(scanner);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Map<String, Object> memberData = new HashMap<>();
        memberData.put("email", email);
        memberData.put("password", password);
        memberData.put("role", "user");

        Map<String, Object> memberMap = new HashMap<>();
        memberMap.put("Member", memberData);
        membersList.add(memberMap);
        return membersList;
    }

    public List<Map<String, Object>> initializeEmployeeLogin(){
        InputValidation inputValidation = new InputValidation();
        String email;
        String password;

        try {
            email = inputValidation.readEmail(scanner);
            password = inputValidation.readPassword(scanner);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Map<String, Object> memberData = new HashMap<>();
        memberData.put("email", email);
        memberData.put("password", password);
        memberData.put("role", "employee");

        Map<String, Object> membersMap = new HashMap<>();
        membersMap.put("Member", memberData);
        employeeList.add(membersMap);
        return employeeList;
    }
}
