package com.nick.server.server_utilities;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.nick.server.communication.Request;
import com.nick.server.communication.Response;
import com.nick.server.entry.LoginManager;
import com.nick.server.entry.RegisterManager;
import com.nick.server.domain.Member;
import com.nick.server.rental_crud.ChooseProduct;
import com.nick.server.rental_crud.CreateRental;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.*;

public class Handler implements Runnable {
    private Socket clientSocket;
    private Set<Handler> users;
    private BufferedReader reader;
    private BufferedWriter writer;

    public static void sendJsonResponseToClient(BufferedWriter writer, Object object) throws IOException {;
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(object);
        writer.write(jsonResponse);
        writer.newLine();
        writer.flush();
        System.out.println("Sent Request: " + jsonResponse);
    }
    Handler(Socket clientSocket, Set<Handler> users){
        this.clientSocket = clientSocket;
        this.users = users;
        try{
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        }catch (IOException e) {
            System.err.println("Error..." + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String jsonMember;
        Request clientRequest;
        Response response = new Response();
        // Create a Gson instance with custom deserializer
      Gson gson = new Gson();
         try {
             List<Map<String, Object>> registersList = null;
             jsonMember = reader.readLine();
             if (jsonMember != null){
                 clientRequest = gson.fromJson(jsonMember, Request.class);
                 String type = clientRequest.getType();
                 String message = clientRequest.getMessage();
                 System.out.println("Client request type:" + type);
                 System.out.println("Client request message: " + message);
                 switch (type) {
                     case "Login" -> {
                         System.out.println("Request: " + jsonMember);
                         System.out.println("_".repeat(10) + message + "_".repeat(10));
                         LoginManager loginManager = new LoginManager();
                         String jsonClientRegisterRequest = reader.readLine();
                         System.out.println("Json Request: " + jsonClientRegisterRequest);

                         Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
                         List<Map<String, Object>> memberList = gson.fromJson(jsonClientRegisterRequest, listType);

                         if(!memberList.isEmpty()) {
                             response.setStatus(true);
                             response.setMessage("sign-in has been completed successfully.");
                             for (Map<String, Object> request : memberList) {
                                 List<Map<String, Object>> singleRequestList = new ArrayList<>();
                                 singleRequestList.add(request);
                                 registersList = loginManager.loginMember(singleRequestList);
                                 System.out.println("Retrieved Members: " + registersList);
                             }
                         }
                     }
                     case "Register" -> {
                         System.out.println("Request: " + jsonMember);
                         System.out.println("_".repeat(10) + message + "_".repeat(10));
                         RegisterManager registerManager = new RegisterManager();
                         String jsonClientRegisterRequest = reader.readLine();
                         System.out.println("Json Request: " + jsonClientRegisterRequest);
                         Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
                         List<Map<String, Object>> memberList = gson.fromJson(jsonClientRegisterRequest, listType);
                         response.setStatus(true);
                         response.setMessage("Registration has been completed successfully.");
                         if(!memberList.isEmpty()) {
                             response.setStatus(true);
                             response.setMessage("sign-up has been completed successfully.");
                             for (Map<String, Object> request : memberList) {
                                 List<Map<String, Object>> singleRequestList = new ArrayList<>();
                                 singleRequestList.add(request);
                                 registersList = registerManager.registerMember(singleRequestList);
                                 System.out.println("Retrieved Members: " + registersList);
                             }
                         }else{
                             System.err.println("Error...Empty Members List");
                         }
                     }

                 }
             }else{
                 System.err.println("Error.. Nothing to read from client");
             }
             if (!registersList.isEmpty()) {
                 for (var item : registersList) {
                     Member member = (Member) item.get("Member");
                     response.setUser(member);
                     response.setRole(member.getRole());
                 }
                 System.out.println(response);
                 sendJsonResponseToClient(writer, response);
                 String jsonClientRequest;

                 while ((jsonClientRequest = reader.readLine()) != null) {
                     clientRequest = gson.fromJson(jsonClientRequest, Request.class);
                     String type = clientRequest.getType();
                     String message = clientRequest.getMessage();
                     int deleteChoice = clientRequest.getDeleteChoice();
                     int deleteID = clientRequest.getDeleteID();
                     int totalCostChoice = clientRequest.getTotalCostChoice();
                     int printRentalChoice = clientRequest.getPrintRentalChoice();
                     System.out.println("\n");
                     System.out.println("Client request type:" + type);
                     System.out.println("Client request message: " + message);
                     System.out.println("Client request delete choice: " + deleteChoice);
                     System.out.println("Client request to delete the ID: " + deleteID);
                     System.out.println("Client request total cost choice: " + totalCostChoice);
                     System.out.println("Client request print rental choice: " + printRentalChoice);

                     switch (type) {
                        case "Room" -> {
                            System.out.println("\n");
                            System.out.println("Request: " + jsonClientRequest);
                            System.out.println("_".repeat(10) + message + "_".repeat(10));
                            CreateRental createRental = new CreateRental();
                            String jsonClientDataRequest = reader.readLine();
                            System.out.println("Json Request: " + jsonClientDataRequest);

                            Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
                            List<Map<String, Object>> roomList = gson.fromJson(jsonClientDataRequest, listType);
                            for (Map<String, Object> request : roomList) {
                                List<Map<String, Object>> singleRequestList = new ArrayList<>();
                                singleRequestList.add(request);
                                registersList = createRental.saveRental(singleRequestList);
                                System.out.println("Retrieved Rooms: " + registersList);
                            }

                        }
                        case "AddCar" -> {
                            System.out.println("\n");
                            System.out.println("Request: " + jsonClientRequest);
                            System.out.println("_".repeat(10) + message + "_".repeat(10));
                            CreateRental createRental = new CreateRental();
                            String jsonClientDataRequest = reader.readLine();
                            System.out.println("Json Request: " + jsonClientDataRequest);
                            // Parse the JSON array into a list of maps
                            Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
                            List<Map<String, Object>> carList = gson.fromJson(jsonClientDataRequest, listType);

                            for (Map<String, Object> request : carList) {
                                List<Map<String, Object>> singleRequestList = new ArrayList<>();
                                singleRequestList.add(request);
                                registersList = createRental.saveRental(singleRequestList);
                                System.out.println("Retrieved Cars: " + registersList);
                            }
                        }
                        case "ChooseCar" -> {
                            System.out.println("\n");
                            System.out.println("Request: " + jsonClientRequest);
                            System.out.println("_".repeat(10) + message + "_".repeat(10));
                            ChooseProduct chooseProduct = new ChooseProduct();
                            String jsonClientDataRequest = reader.readLine();
                            System.out.println("Json Request: " + jsonClientDataRequest);
                            // Parse the JSON array into a list of maps
                            Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
                            List<Map<String, Object>> carList = gson.fromJson(jsonClientDataRequest, listType);

                            for (Map<String, Object> request : carList) {
                                List<Map<String, Object>> singleRequestList = new ArrayList<>();
                                singleRequestList.add(request);
                                registersList = chooseProduct.chooseProduct(singleRequestList);
                                System.out.println("Retrieved Cars: " + registersList);
                            }
                        }
                        case "ChooseRoom" -> {
                            System.out.println("\n");
                            System.out.println("Request: " + jsonClientRequest);
                            System.out.println("_".repeat(10) + message + "_".repeat(10));
                            ChooseProduct chooseProduct = new ChooseProduct();
                            String jsonClientDataRequest = reader.readLine();
                            System.out.println("Json Request: " + jsonClientDataRequest);
                            // Parse the JSON array into a list of maps
                            Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
                            List<Map<String, Object>> carList = gson.fromJson(jsonClientDataRequest, listType);

                            for (Map<String, Object> request : carList) {
                                List<Map<String, Object>> singleRequestList = new ArrayList<>();
                                singleRequestList.add(request);
                                registersList = chooseProduct.chooseProduct(singleRequestList);
                                System.out.println("Retrieved Rooms: " + registersList);
                            }
                        }
                        case "PrintRecords" -> {
                            System.out.println("\n");
                            System.out.println("Request: " + jsonClientRequest);
                            System.out.println("_".repeat(10) + message + "_".repeat(10));
                            CreateRental createRental = new CreateRental();
                            if(totalCostChoice == 1){
                                System.out.println("_".repeat(10) + "Print Room Records" + "_".repeat(10)) ;
                                List<Map<String, Object>> rooms = createRental.printRecordHandler(type, totalCostChoice);
                                if (rooms != null) {
                                    sendJsonResponseToClient(writer, rooms);
                                } else {
                                    System.out.println("No Rooms Records...");
                                }
                            }else if (totalCostChoice == 2){
                                System.out.println("_".repeat(10) + "Print Car Records" + "_".repeat(10)) ;
                                List<Map<String, Object>> cars = createRental.printRecordHandler(type, totalCostChoice);
                                if (cars != null) {
                                    sendJsonResponseToClient(writer, cars);
                                } else {
                                    System.out.println("No Cars Records...");
                                }
                            }else{
                                 System.out.println("Unknown choice...");
                            }
                        }
                         case "PrintRentals" -> {
                             System.out.println("\n");
                             System.out.println("Request: " + jsonClientRequest);
                             System.out.println("_".repeat(10) + message + "_".repeat(10));
                             CreateRental createRental = new CreateRental();
                             if(printRentalChoice == 1){
                                 System.out.println("_".repeat(10) + "Print Room Rentals" + "_".repeat(10)) ;
                                 List<Map<String, Object>> rooms = createRental.roomTotalCost();
                                 if (rooms != null) {
                                     sendJsonResponseToClient(writer, rooms);
                                 } else {
                                     System.out.println("No Rooms Records...");
                                 }
                             }else if (printRentalChoice == 2){
                                 System.out.println("_".repeat(10) + "Print Car Rentals" + "_".repeat(10)) ;
                                 List<Map<String, Object>> cars = createRental.carTotalCost();
                                 System.out.println("Car lists(else if): " + cars);
                                 if (cars != null) {
                                     sendJsonResponseToClient(writer, cars);
                                 } else {
                                     System.out.println("No Cars Records...");
                                 }
                             }else{
                                 System.out.println("Unknown choice...");
                             }
                         }
                        case "Delete" -> {
                            System.out.println("\n");
                            System.out.println("Request: " + jsonClientRequest);
                            System.out.println("_".repeat(10) + message + "_".repeat(10));
                            System.out.println("Delete Register ");
                            CreateRental createRental = new CreateRental();
                            createRental.DeleteRental(deleteChoice, deleteID);
                        }
                        case "TotalCost" -> {
                            System.out.println("\n");
                            System.out.println("Request: " + jsonClientRequest);
                            System.out.println("_".repeat(10) + message + "_".repeat(10));
                            CreateRental createRental = new CreateRental();
                            if(totalCostChoice == 1){
                                List<Map<String, Object>> rooms = createRental.roomTotalCost();
                                sendJsonResponseToClient(writer, rooms);
                            }else if (totalCostChoice == 2){
                                List<Map<String, Object>> cars = createRental.carTotalCost();
                                sendJsonResponseToClient(writer, cars);
                            }else{
                                System.out.println("Unknown choice...");
                            }
                        }
                        case "PrintPayments" -> {
                            System.out.println("\n");
                            System.out.println("Request: " + jsonClientRequest);
                            System.out.println("_".repeat(10) + message + "_".repeat(10));
                            CreateRental createRental = new CreateRental();
                            if(totalCostChoice == 1){
                                List<Map<String, Object>> rooms = createRental.printRecordHandler(type, totalCostChoice);
                                sendJsonResponseToClient(writer, rooms);
                            }else if (totalCostChoice == 2){
                                List<Map<String, Object>> cars = createRental.printRecordHandler(type, totalCostChoice);
                                sendJsonResponseToClient(writer, cars);
                            }else{
                                System.out.println("Unknown choice...");
                            }
                        }
                        default -> {
                            System.out.println("Unknown Request...");
                        }
                     }
                 }
             } else {
                 response.setStatus(false);
                 response.setMessage("User already exists!");
                 // Send JSON response to client
                 sendJsonResponseToClient(writer, response);
             }
         } catch (IOException | JsonSyntaxException e) {
             System.err.println("Error..." + e.getMessage());
             e.printStackTrace();
         }
    }
}
