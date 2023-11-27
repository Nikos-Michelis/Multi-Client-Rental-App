package com.nick.client;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.nick.client.communication.Request;
import com.nick.client.communication.Response;
import com.nick.client.entry.LoginManager;
import com.nick.client.entry.RegisterManager;
import com.nick.client.domain.*;
import com.nick.client.validation.InputRentalChoice;
import com.nick.client.validation.InputValidation;
import com.nick.client.validation.RentalDeserializer;

import java.io.*;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void printPayments(List<Object> objectsList){
        for (var item : objectsList) {
            if(item instanceof Room) {
                Room roomObject = (Room) item;
                if (roomObject.getDiscount() != 0) {
                    System.out.print(roomObject + "\nTotal cost for your Room is: " +
                            new BigDecimal(roomObject.getTotalCost()).setScale(2, RoundingMode.HALF_UP) + ", after 5/100 discount: " +
                            new BigDecimal(roomObject.getDiscount()).setScale(2, RoundingMode.HALF_UP) + "\n");
                } else {
                    System.out.println(roomObject + "\nTotal cost for your Room is: " + new BigDecimal(roomObject.getTotalCost()).setScale(2, RoundingMode.HALF_UP));
                }
            }else if(item instanceof Car) {
                Car carObject = (Car) item;
                if (carObject.getDiscount() != 0) {
                    System.out.print(carObject + "\nTotal cost for your Room is: " +
                            new BigDecimal(carObject.getTotalCost()).setScale(2, RoundingMode.HALF_UP) + ", after 5/100 discount: " +
                            new BigDecimal(carObject.getDiscount()).setScale(2, RoundingMode.HALF_UP) + "\n");
                } else {
                    System.out.println(carObject + "\nTotal cost for your Room is: " + new BigDecimal(carObject.getTotalCost()).setScale(2, RoundingMode.HALF_UP));
                }
            }else{
                System.err.println("Error... Unknown object type ");
            }
        }
        objectsList.clear();
    }
    public static void printServerResponseData(List<Object> objectsList, String errorMessage){
        if (!objectsList.isEmpty()) {
            for (var item : objectsList) {
                System.out.println("before instances: " + item.getClass());
                if(item instanceof Room) {
                    System.out.println("- " + item);
                }else if(item instanceof Car) {
                    System.out.println("- " + item);
                }else{
                    System.err.println("Error... Unknown object type ");
                }
            }
            objectsList.clear();
        }else{
            System.out.println("Error..." + errorMessage);
        }
    }
    public static String sendJsonRequestToServer(BufferedWriter writer, Object object) throws IOException {
        Gson gson = new Gson();
        String jsonRequest = gson.toJson(object);
        writer.write(jsonRequest);
        writer.newLine();
        writer.flush();
        System.out.println("Sent Request: " + jsonRequest);
        return jsonRequest;
    }

    public static int centralMenu(Scanner sc, InputValidation inputValidation, String role){
        System.out.println("_".repeat(10) + "Menu" + "_".repeat(10));
        int choice = 0;
        switch (role){
            case "user"-> {
                System.out.println("1 - Choose car:");
                System.out.println("2 - Choose room:");
                System.out.println("3 - Print Rooms or Cars: ");
                System.out.println("4 - Print Rentals:");
                System.out.println("5 - Print Payments:");
                System.out.println("0 - Exit...");
                choice = inputValidation.readIntMenu(sc,0,4);
            }case "employee"-> {
                System.out.println("1 - Add Room: ");
                System.out.println("2 - Add Car: ");
                System.out.println("3 - Print Rooms or Cars: ");
                System.out.println("4 - Delete Rooms or Cars: ");
                //System.out.println("5 - Total income from orders:");
                //System.out.println("6 - Print Payments:");
                System.out.println("0 - Exit...");
                choice = inputValidation.readIntMenu(sc,0,4);
            }default -> {
                System.err.println("Error...Unknown choice...");
            }
        }
        return choice;
    }

    public static void main(String[] args) {
        final int THREAD_POOL_SIZE = 10;

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        executorService.submit(()-> {
            try (Socket socket = new Socket("localhost", 12346);
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                try (Scanner sc = new Scanner(System.in)) {
                    RegisterManager registerManager = new RegisterManager(sc);
                    LoginManager loginManager = new LoginManager(sc);
                    InputValidation inputValidation = new InputValidation();
                    RentalDeserializer rentalDeserializer = new RentalDeserializer();
                    InputRentalChoice inputRentalChoice = new InputRentalChoice();
                    Request request = new Request();
                    Gson gson = new Gson();

                    // user login or register
                    System.out.println("_".repeat(10) + "Welcome" + "_".repeat(10));
                    System.out.println("1 - Login-Customer ");
                    System.out.println("2 - Register-Customer ");
                    System.out.println("3 - Login-Employee ");
                    System.out.println("4 - Register-Employee ");
                    int registrationChoice = inputValidation.readIntMenu(sc, 1, 4);
                    switch (registrationChoice) {
                        case 1 -> {
                            request.setType("Login");
                            request.setMessage("Sign-in");
                            request.setRole("user");
                            sendJsonRequestToServer(writer, request);
                            List<Map<String, Object>> loginCustomersList = loginManager.initializeCustomerLogin();
                            System.out.println("customerList class: " + loginCustomersList.getClass());
                            sendJsonRequestToServer(writer, loginCustomersList);
                        }
                        case 2 -> {
                            request.setType("Register");
                            request.setMessage("Sign-up");
                            request.setRole("user");
                            sendJsonRequestToServer(writer, request);
                            List<Map<String, Object>> registerCustomersList = registerManager.initializeCustomerRegister();
                            System.out.println("customerList class: " + registerCustomersList.getClass());
                            sendJsonRequestToServer(writer, registerCustomersList);
                        }
                        case 3 -> {
                            request.setType("Login");
                            request.setMessage("Sign-in");
                            request.setRole("employee");
                            sendJsonRequestToServer(writer, request);
                            List<Map<String, Object>> loginEmployeesList = loginManager.initializeEmployeeLogin();
                            System.out.println("employeeList class: " + loginEmployeesList.getClass());
                            sendJsonRequestToServer(writer, loginEmployeesList);
                        }
                        case 4 -> {
                            request.setType("Register");
                            request.setMessage("Sign-up");
                            request.setRole("employee");
                            sendJsonRequestToServer(writer, request);
                            List<Map<String, Object>> loginEmployeesList = registerManager.initializeEmployeeRegister();
                            System.out.println("employeeList class: " + loginEmployeesList.getClass());
                            sendJsonRequestToServer(writer, loginEmployeesList);
                        }
                    }
                    // Parse JSON response
                    String jsonServerResponse = reader.readLine();
                    Response serverResponse = gson.fromJson(jsonServerResponse, Response.class);
                    Boolean status = serverResponse.getStatus();
                    String message = serverResponse.getMessage();
                    Member user = serverResponse.getUser();
                    String role = serverResponse.getRole();

                    System.out.println("Server response status: " + status);
                    System.out.println("Server response message: " + message);
                    System.out.println("Server response user: " + user);
                    System.out.println("Server response role: " + role);

                    int choice;
                    if (status) {
                        choice = centralMenu(sc, inputValidation, role);
                        while (choice != 0) {
                            switch (role) {
                                case "user" -> {
                                    switch (choice) {
                                        case 1 -> {
                                            request.setType("ChooseCar");
                                            request.setMessage("Choose your Car");
                                            sendJsonRequestToServer(writer, request);
                                            List<Map<String, Object>> carArrayList = inputRentalChoice.chooseCar(sc, user);
                                            sendJsonRequestToServer(writer, carArrayList);
                                        }
                                        case 2 -> {
                                            request.setType("ChooseRoom");
                                            request.setMessage("Choose your Room");
                                            sendJsonRequestToServer(writer, request);
                                            List<Map<String, Object>> carArrayList = inputRentalChoice.chooseRoom(sc, user);
                                            sendJsonRequestToServer(writer, carArrayList);
                                        }
                                        case 3 -> {
                                            int printChoice;
                                            System.out.println("_".repeat(10) + "Print Menu" + "_".repeat(10));
                                            System.out.println("1 - Room: ");
                                            System.out.println("2 - Car: ");
                                            System.out.print("Your Choice: ");
                                            printChoice = inputValidation.readIntMenu(sc, 1, 2);
                                            // send json request
                                            request.setType("PrintRecords");
                                            request.setMessage("Print Rooms or Cars Records");
                                            request.setTotalCostChoice(printChoice);
                                            sendJsonRequestToServer(writer, request);
                                            // get json response from server
                                            String jsonResponse = reader.readLine();
                                            Type listType = new TypeToken<List<Map<String, Object>>>() {
                                            }.getType();
                                            List<Map<String, Object>> responseList = gson.fromJson(jsonResponse, listType);
                                            //print response
                                            List<Object> objectList = rentalDeserializer.objectsDeserializer(responseList);
                                            printServerResponseData(objectList, "You have not rooms rentals...");
                                        }
                                        case 4 -> {
                                            int printTotalCostChoice;
                                            System.out.println("_".repeat(10) + "Print Rentals Menu" + "_".repeat(10));
                                            System.out.println("1 - Room: ");
                                            System.out.println("2 - Car: ");
                                            System.out.print("Your Choice: ");
                                            printTotalCostChoice = inputValidation.readIntMenu(sc, 1, 2);
                                            // send json request
                                            request.setType("PrintRentals");
                                            request.setMessage("Print Rooms or Cars Rentals");
                                            request.setPrintRentalChoice(printTotalCostChoice);
                                            sendJsonRequestToServer(writer, request);
                                            // get json response from server
                                            String jsonResponse = reader.readLine();
                                            List<Map<String, Object>> responseList = gson.fromJson(jsonResponse, List.class);
                                            List<Object> objectList = rentalDeserializer.objectsDeserializer(responseList);
                                            printServerResponseData(objectList, "You have not rentals...");
                                        }
                                        case 5 -> {
                                            int printTotalCostChoice;
                                            System.out.println("_".repeat(10) + "Total Cost Menu" + "_".repeat(10));
                                            System.out.println("1 - Room: ");
                                            System.out.println("2 - Car: ");
                                            System.out.print("Your Choice: ");
                                            printTotalCostChoice = inputValidation.readIntMenu(sc, 1, 2);
                                            // send json request
                                            request.setType("PrintPayments");
                                            request.setMessage("Print Rooms or Cars Payments");
                                            request.setTotalCostChoice(printTotalCostChoice);
                                            sendJsonRequestToServer(writer, request);
                                            // get json response from server
                                            String jsonResponse = reader.readLine();
                                            List<Map<String, Object>> responseList = gson.fromJson(jsonResponse, List.class);
                                            List<Object> objectList = rentalDeserializer.objectsDeserializer(responseList);
                                            printPayments(objectList);
                                        }
                                    }
                                }
                                case "employee" -> {
                                    switch (choice) {
                                        case 1 -> {
                                            request.setType("Room");
                                            request.setMessage("Save room");
                                            sendJsonRequestToServer(writer, request);
                                            List<Map<String, Object>> roomArrayList = inputRentalChoice.addRoom(sc, user);
                                            sendJsonRequestToServer(writer, roomArrayList);
                                        }
                                        case 2 -> {
                                            request.setType("AddCar");
                                            request.setMessage("Add new Cars");
                                            sendJsonRequestToServer(writer, request);
                                            List<Map<String, Object>> carArrayList = inputRentalChoice.addCar(sc);
                                            sendJsonRequestToServer(writer, carArrayList);
                                        }
                                        case 3 -> {
                                            int printChoice;
                                            System.out.println("_".repeat(10) + "Print Menu" + "_".repeat(10));
                                            System.out.println("1 - Room: ");
                                            System.out.println("2 - Car: ");
                                            System.out.print("Your Choice: ");
                                            printChoice = inputValidation.readIntMenu(sc, 1, 2);
                                            // send json request
                                            request.setType("PrintRecords");
                                            request.setMessage("Print Rooms or Cars Records");
                                            request.setTotalCostChoice(printChoice);
                                            sendJsonRequestToServer(writer, request);
                                            // System.out.println("Sent Request: " + jsonRequest);
                                            // get json response from server
                                            String jsonResponse = reader.readLine();
                                            Type listType = new TypeToken<List<Map<String, Object>>>() {
                                            }.getType();
                                            List<Map<String, Object>> responseList = gson.fromJson(jsonResponse, listType);
                                            //print response
                                            List<Object> objectList = rentalDeserializer.objectsDeserializer(responseList);
                                            printServerResponseData(objectList, "You have not rooms rentals...");
                                        }
                                        case 4 -> {
                                            int deleteChoice;
                                            System.out.println("_".repeat(10) + "Delete Menu" + "_".repeat(10));
                                            System.out.println("1 - Room: ");
                                            System.out.println("2 - Car: ");
                                            System.out.print("Your Choice: ");
                                            deleteChoice = inputValidation.readIntMenu(sc, 1, 2);
                                            int deleteID = 0;
                                            switch (deleteChoice) {
                                                case 1 -> {
                                                    try {
                                                        deleteID = inputValidation.readIntData(sc, "Room ID for Delete");
                                                    } catch (InterruptedException | RuntimeException e) {
                                                        System.err.println("Error..." + e);
                                                    }
                                                }
                                                case 2 -> {
                                                    try {
                                                        deleteID = inputValidation.readIntData(sc, "Car ID for Delete");
                                                    } catch (InterruptedException | RuntimeException e) {
                                                        System.err.println("Error..." + e);
                                                    }
                                                }
                                            }
                                            // send json request
                                            request.setType("Delete");
                                            request.setMessage("Delete Register");
                                            request.setDeleteChoice(deleteChoice);
                                            request.setDeleteID(deleteID);
                                            sendJsonRequestToServer(writer, request);
                                        }
                                        /*case 5 -> {
                                            int totalCostChoice;
                                            System.out.println("_".repeat(10) + "Total Cost Menu" + "_".repeat(10));
                                            System.out.println("1 - Room: ");
                                            System.out.println("2 - Car: ");
                                            System.out.print("Your Choice: ");
                                            totalCostChoice = inputValidation.readIntMenu(sc, 1, 2);
                                            // send json request
                                            request.setType("TotalCost");
                                            request.setMessage("Calculate Rooms or Cars Total Cost");
                                            request.setTotalCostChoice(totalCostChoice);
                                            sendJsonRequestToServer(writer, request);
                                            // get json response from server
                                            String jsonResponse = reader.readLine();
                                            Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
                                            List<Map<String, Object>> responseList = gson.fromJson(jsonResponse, listType);
                                            // print response
                                            List<Object> objectList = rentalDeserializer.objectsDeserializer(responseList);
                                            // print response
                                            printServerResponseData(objectList, "You have not rentals...");
                                        }*/
                                        case 6 -> {
                                            int printTotalCostChoice;
                                            System.out.println("_".repeat(10) + "Total Cost Menu" + "_".repeat(10));
                                            System.out.println("1 - Room: ");
                                            System.out.println("2 - Car: ");
                                            System.out.print("Your Choice: ");
                                            printTotalCostChoice = inputValidation.readIntMenu(sc, 1, 2);
                                            // send json request
                                            request.setType("PrintPayments");
                                            request.setMessage("Print Rooms or Cars Payments");
                                            request.setTotalCostChoice(printTotalCostChoice);
                                            sendJsonRequestToServer(writer, request);
                                            // get json response from server
                                            String jsonResponse = reader.readLine();
                                            List<Map<String, Object>> responseList = gson.fromJson(jsonResponse, List.class);
                                            List<Object> objectList = rentalDeserializer.objectsDeserializer(responseList);
                                            printPayments(objectList);
                                        }
                                    }
                                }
                            }
                            choice = centralMenu(sc, inputValidation, role);
                        }
                    } else {
                        System.err.println("Error...Server is not responding");
                    }
                } catch (IOException e) {
                    System.err.println("Server is down: " + e.getMessage());
                }
            } catch (IOException | JsonSyntaxException e) {
                System.err.println("Error..." + e);
                e.printStackTrace();
            }
        });
        executorService.shutdown();
    }
}
