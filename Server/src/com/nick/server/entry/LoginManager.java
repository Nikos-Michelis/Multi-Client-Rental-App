package com.nick.server.entry;

import com.google.gson.JsonSyntaxException;
import com.nick.server.domain.Member;
import com.nick.server.rental_crud_utilities.FileHandler;
import org.mindrot.jbcrypt.BCrypt;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LoginManager {
    private final Path customersPath = Path.of("Your-Path\\customers.dat");
    private final Path employeePath = Path.of("Your-Path\\employees.dat");
    private final ReadWriteLock membersLock = new ReentrantReadWriteLock();
    private List<Map<String, Object>> loadedMembers = new ArrayList<>();
    private List<Map<String, Object>> newMembersList = new ArrayList<>();
    private FileHandler fileHandler = new FileHandler();
    public LoginManager(){

    }
    public boolean passwordAuthentication(String clientHashedPassword, String serverHashedPassword) {
        return BCrypt.checkpw(clientHashedPassword, serverHashedPassword);
    }
    private List<Member> convertJsonToMemberObject(List<Map<String, Object>> membersList) {
        List<Member> memberObjectList = new ArrayList<>();

        for (var item : membersList) {
            try {
                Map<String, Object> memberData = (Map<String, Object>) item.get("Member");
                Member memberObject = new Member();
                memberObject.setEmail((String) memberData.get("email"));
                memberObject.setPassword((String) memberData.get("password"));
                memberObject.setRole((String) memberData.get("role"));
                memberObjectList.add(memberObject);
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return memberObjectList;
    }
    private void isMemberExists(List<Member> inputMemberObjectList){
        boolean exists = false;
        for (var item : loadedMembers) {
            try {
                Member existingMemberObject = (Member) item.get("Member");

                for (Member memberObject : inputMemberObjectList){
                    System.out.println("- Input Member: " + memberObject);
                    System.err.println("existingMemberObject: " + existingMemberObject);
                    System.err.println("existingMemberObject - password: " + existingMemberObject.getPassword());
                    System.err.println("MemberObject: " + memberObject.getPassword());

                    if (passwordAuthentication(memberObject.getPassword(), existingMemberObject.getPassword()) && memberObject.getEmail().equals(existingMemberObject.getEmail())) {
                        newMembersList.add(item);
                        System.out.println("The User " + newMembersList + " has been sign-in successfully");
                        exists = true;
                        break;
                    } else {
                        System.err.println("Sorry user " + memberObject + " is not exists...");
                        newMembersList.clear();
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
                loadedMembers.clear();
                break;
            }
        }
    }
    public List<Map<String, Object>> loginMember(List<Map<String, Object>> inputMembersList) {
        synchronized (membersLock) {
            membersLock.readLock().lock();

            try {
                // convert Json to member object
                List<Member> membersObjectList = convertJsonToMemberObject(inputMembersList);
                if (!membersObjectList.isEmpty()) {
                    for (var item: membersObjectList) {
                        System.out.println(item.getRole().equals("employee"));
                        System.out.println(item.getRole().equals("user"));
                        if (item.getRole().equals("user")) {
                            loadedMembers = fileHandler.loadFromFile(loadedMembers,customersPath.toFile(), "- Load 'customers.dat'");
                        }else if (item.getRole().equals("employee")){
                            loadedMembers = fileHandler.loadFromFile(loadedMembers,employeePath.toFile(), "- Load 'employees.dat'");
                        }else{
                            System.err.println("Error...Unknown member role");
                        }
                    }
                    if (!loadedMembers.isEmpty()) {
                        isMemberExists(membersObjectList);

                    }else{
                        newMembersList.addAll(inputMembersList);
                    }
                }else{
                    System.err.println("Input Error...Not found members for login");
                }
            } finally {
                membersLock.readLock().unlock();
            }
            System.out.println("Return member " + newMembersList);
            return newMembersList;
        }
    }
}
