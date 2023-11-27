package com.nick.server.entry;

import com.google.gson.JsonSyntaxException;
import com.nick.server.domain.Member;
import com.nick.server.rental_crud_utilities.FileHandler;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RegisterManager {
    private final Path customersPath = Path.of("Your-Path\\customers.dat");
    private final Path employeePath = Path.of("Your-Path\\employees.dat");
    private final ReadWriteLock membersLock = new ReentrantReadWriteLock();
    private List<Map<String, Object>> loadedMembersList = new ArrayList<>();
    private List<Map<String, Object>> newMembersList = new ArrayList<>();
    private FileHandler fileHandler = new FileHandler();
    public RegisterManager(){}

    private List<Member> convertJsonToMemberObject(List<Map<String, Object>> membersList) {
        List<Member> memberObjectList = new ArrayList<>();

        for (var item : membersList) {
            try {
                Map<String, Object> memberData = (Map<String, Object>) item.get("Member");
                Member memberObject = new Member();
                memberObject.setSurName((String) memberData.get("surName"));
                memberObject.setName((String) memberData.get("name"));
                memberObject.setEmail((String) memberData.get("email"));
                memberObject.setPhone((String) memberData.get("phone"));
                memberObject.setPassword((String) memberData.get("password"));
                memberObject.setDriverLicense((String) memberData.get("driverLicense"));
                memberObject.setRole((String) memberData.get("role"));
                memberObjectList.add(memberObject);
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return memberObjectList;
    }

    public Object findMaxRecordID(List<Map<String, Object>> loadedRentalArrayList, Object inputObject){
        int loadedIdNumber = 0;
        if(inputObject instanceof Member) {
            if (!loadedRentalArrayList.isEmpty()) {
                for (Map<String, Object> memberInfo : loadedRentalArrayList) {
                    Member loadedMemberObject = (Member) memberInfo.get("Member");
                    if (loadedMemberObject.getDriverLicense().contains("L")){
                        String getNumber = loadedMemberObject.getDriverLicense().trim().substring(1, loadedMemberObject.getDriverLicense().length());
                        loadedIdNumber = Integer.parseInt(getNumber);
                        System.out.println("-- id numbers " + loadedIdNumber);
                        int max = 0;
                        if (loadedIdNumber > max) {
                            max = (loadedIdNumber + 1);
                            String setId = "L" + max;
                            ((Member) inputObject).setDriverLicense(setId);
                            System.out.println(inputObject);
                        }
                    }
                }
            } else {
                ((Member) inputObject).setDriverLicense("L100");
            }
        }
        System.out.println("returned object from id method: " + inputObject);
        return inputObject;
    }
    private void isMemberExists(List<Member> inputMember, List<Map<String, Object>> inputMembersList) {
        boolean exists = false;
        Map<String, Object> map = new HashMap<>();
        for (var item : loadedMembersList) {
            try {
                System.out.println("Loaded Members item Class Type: " + item.getClass());
                Member existingMemberObject = (Member)item.get("Member");

                for (Member memberObject : inputMember) {
                    System.out.println("- Input Member: " + memberObject);
                    System.err.println("Compare Existing Member: " + existingMemberObject + " Input Member: " + memberObject);
                    Member maxMemberObject = (Member) findMaxRecordID(loadedMembersList, memberObject);
                    if (existingMemberObject.getEmail().equals(maxMemberObject.getEmail())) {
                        System.err.println("Sorry, user " + existingMemberObject + " is already exists...");
                        loadedMembersList.clear();
                        newMembersList.clear();
                        exists = true;
                        break;
                    }else{
                        map.put("Member", maxMemberObject);
                    }
                }
            } catch (JsonSyntaxException e) {
                System.err.println("Error parsing JSON data: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
                e.printStackTrace();
            }
            if(exists){
                loadedMembersList.clear();
                break;
            }
        }
        loadedMembersList.add(map);
        newMembersList.add(map);

        for (var item : loadedMembersList) {
            Member member = (Member)item.get("Member");
            if(member.getRole().equals("user")) {
                fileHandler.saveToFile(loadedMembersList, customersPath.toFile(),"- Save to 'customers.dat'");
            }else if(member.getRole().equals("employee")){
                fileHandler.saveToFile(loadedMembersList, employeePath.toFile(),"- Save to 'employees.dat'");
            }else{
                System.err.println("Error...Unknown role...");
            }
            break;
        }
    }
    public List<Map<String, Object>> registerMember(List<Map<String, Object>> inputMembersList) {
        synchronized (membersLock) {
            membersLock.readLock().lock();
            try {
                System.out.println("New Members List: " + loadedMembersList.getClass());
                // convert Json to Member object
                List<Member> inputMemberObject = convertJsonToMemberObject(inputMembersList);
                if (!inputMemberObject.isEmpty()) {
                    for (var item : inputMemberObject) {
                        if(item.getRole().equals("user")) {
                            loadedMembersList = fileHandler.loadFromFile(loadedMembersList, customersPath.toFile(),"- Load 'customers.dat'");

                        }else if(item.getRole().equals("employee")){
                            loadedMembersList = fileHandler.loadFromFile(loadedMembersList, employeePath.toFile(),"- Load 'employee.dat'");

                        }else{
                            System.err.println("Error...Unknown role...");
                        }
                    }
                    System.out.println("check list: " + loadedMembersList);
                    if (!loadedMembersList.isEmpty()) {
                        isMemberExists(inputMemberObject, inputMembersList);
                    }else{
                        HashMap<String, Object> map = new HashMap<>();
                        for (var item : inputMemberObject) {
                            Member maxMemberObject = (Member) findMaxRecordID(loadedMembersList, item);
                            map.put("Member", maxMemberObject);
                        }

                        loadedMembersList.add(map);
                        newMembersList.add(map);
                        for (var item : loadedMembersList) {
                            Member member = (Member)item.get("Member");
                            if(member.getRole().equals("user")) {
                                fileHandler.saveToFile(loadedMembersList, customersPath.toFile(),"- Save to 'customers.dat'");
                            }else if(member.getRole().equals("employee")){
                                fileHandler.saveToFile(loadedMembersList, employeePath.toFile(),"- Save to 'employees.dat'");
                            }else{
                                System.err.println("Error...Unknown role...");
                            }
                        }
                    }
                }else{
                    System.err.println("Input Error...Not found members for register");
                }
            } finally {
                membersLock.readLock().unlock();
            }
            System.out.println("Return data of Class Type: " + newMembersList.getClass() + " Data: " + newMembersList);
            return newMembersList;
        }
    }
}
