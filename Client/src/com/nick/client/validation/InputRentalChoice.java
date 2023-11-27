package com.nick.client.validation;

import com.nick.client.domain.Member;

import java.util.*;

public class InputRentalChoice {
    public int insuranceChoice(Scanner sc ,InputValidation inputValidation) throws InterruptedException {
        int insuranceChoice = 0;
        String insurance;
        insurance = inputValidation.readAnswer(sc, "Do you want insurance Y/N: ");
        // insurance input
        if (insurance.equalsIgnoreCase("Y")) {
            System.out.println("___Choose Insurance Type___");
            System.out.println("Insurance - 1: Standard Package (Press 1)");
            System.out.println("Insurance - 2: Mixed Package (Press 2)");
            insuranceChoice = inputValidation.readIntMenu(sc, 1, 2);
        } else {
            System.out.println("You chose no insurance.");
        }
        return insuranceChoice;
    }

    public List<Map<String, Object>> addRoom(Scanner sc, Member member) {
        int persons, roomNumber;
        String choice = "", hotelName, roomType;
        boolean parking = false;
        EnumTypeHandler enumTypeHandler = new EnumTypeHandler();
        InputValidation inputValidation = new InputValidation();
        List<Map<String, Object>> roomList = new ArrayList<>();

        while (!choice.equalsIgnoreCase("N")) {
            System.out.println("_".repeat(5) + "Room Rental" + "_".repeat(5));
            try {
                hotelName = inputValidation.readStringData(sc, "Hotel Name");
                roomNumber = inputValidation.readIntData(sc, "Room Number");
                persons = inputValidation.readIntData(sc, "How many Persons");
                choice = inputValidation.readAnswer(sc, "Parking? Y/N");
                while(true) {
                    roomType = inputValidation.readStringEnum(sc, enumTypeHandler.printAllRoomTypes());
                    if (enumTypeHandler.isValidEnumType(roomType)) {
                        System.out.println("Room Type: " + roomType);
                        break;
                    } else {
                        System.out.println("Unknown room type: " + roomType);
                    }
                }
                while (!parking) {
                    if (choice.equals("Y")) {
                        parking = true;
                    } else {
                        System.out.println("Choose no parking");
                    }
                    break;
                }
                choice = inputValidation.readAnswer(sc, "Do you want to add more rooms? Y/N: ");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Map<String, Object> roomData = new HashMap<>();
            roomData.put("hotelName", hotelName);
            roomData.put("roomType", roomType);
            roomData.put("roomNumber", roomNumber);
            roomData.put("persons", persons);
            roomData.put("parking", parking);

            Map<String, Object> membersMap = new HashMap<>();
            membersMap.put("Room", roomData);
            roomList.add(membersMap);
        }
        return roomList;
    }

    public List<Map<String, Object>> addCar(Scanner sc){
        String choice = "";
        String fuelType, carType;
        String constructor, model, color;
        int quantity;
        int horsePower, engineSize;

        EnumTypeHandler enumTypeHandler = new EnumTypeHandler();
        InputValidation inputValidation = new InputValidation();
        List<Map<String, Object>> carList = new ArrayList<>();

        while (!choice.equalsIgnoreCase("N")) {
            System.out.println("_".repeat(5) + "Car Rental" + "_".repeat(5));
            try {
                constructor = inputValidation.readStringData(sc, "Constructor Name");
                model = inputValidation.readStringData(sc, "Car model");
                color = inputValidation.readStringData(sc, "Car color");
                horsePower = inputValidation.readIntData(sc, "Horse Power");
                engineSize = inputValidation.readIntData(sc, "Engine Size");
                quantity = inputValidation.readIntData(sc, "Quantity");
                while(true) {
                    carType = inputValidation.readStringEnum(sc, enumTypeHandler.printAllCarTypes());
                    System.out.println(carType + " " + enumTypeHandler.isValidEnumType(carType));
                    if (enumTypeHandler.isValidEnumType(carType)) {
                        System.out.println("Car Type: " + carType);
                        break;
                    } else {
                        System.out.println("Unknown car type: " + carType);
                    }
                }

                while(true) {
                    fuelType = inputValidation.readStringEnum(sc, enumTypeHandler.printAllFuelTypes());
                    if (enumTypeHandler.isValidEnumType(fuelType)) {
                        System.out.println("fuel Type: " + fuelType);
                        break;
                    } else {
                        System.out.println("Unknown fuel type: " + fuelType);
                    }
                }

                choice = inputValidation.readAnswer(sc, "Do you want to add more cars? Y/N: ");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Map<String, Object> carData = new HashMap<>();
            carData.put("constructor", constructor);
            carData.put("model", model);
            carData.put("color", color);
            carData.put("horsePower", horsePower);
            carData.put("engineSize", engineSize);
            carData.put("fuelType", fuelType);
            carData.put("carType", carType);
            carData.put("stockQuantity", quantity);

            Map<String, Object> carsMap = new HashMap<>();
            carsMap.put("Car", carData);
            carList.add(carsMap);
        }
        return carList;
    }
    public List<Map<String, Object>> chooseRoom(Scanner sc, Member member) {
        int roomDuration, persons, roomNumber;
        String choice = "", hotelName, roomType;
        InputValidation inputValidation = new InputValidation();
        List<Map<String, Object>> roomList = new ArrayList<>();

        while (!choice.equalsIgnoreCase("N")) {
            System.out.println("_".repeat(5) + "Room Rental" + "_".repeat(5));
            try {
                roomDuration = inputValidation.readIntData(sc, "How many days");
                persons = inputValidation.readIntData(sc, "How many Persons");
                roomNumber = inputValidation.readIntData(sc, "Room number");
                choice = inputValidation.readAnswer(sc, "Do you wantn rent more rooms? Y/N");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Map<String, Object> roomData = new HashMap<>();
            roomData.put("Member", member);
            roomData.put("roomNumber", roomNumber);
            roomData.put("duration", roomDuration);
            roomData.put("persons", persons);
            Map<String, Object> membersMap = new HashMap<>();
            membersMap.put("Room", roomData);
            roomList.add(membersMap);
        }
        return roomList;
    }
    public List<Map<String, Object>> chooseCar(Scanner sc, Member member) {
        String choice = "";
        String fuelType;
        String constructor, model;
        int duration;
        int insuranceChoice;

        InputValidation inputValidation = new InputValidation();
        List<Map<String, Object>> carList = new ArrayList<>();

        while (!choice.equalsIgnoreCase("N")) {
            System.out.println("_".repeat(5) + "Car Rental" + "_".repeat(5));
            try {
                duration = inputValidation.readIntData(sc, "How many days");
                constructor = inputValidation.readStringData(sc, "Constructor Name");
                model = inputValidation.readStringData(sc, "Car model");
                insuranceChoice = insuranceChoice(sc , inputValidation);
                choice = inputValidation.readAnswer(sc, "Do you want rent more cars? Y/N: ");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Map<String, Object> carData = new HashMap<>();
            carData.put("Member", member);
            carData.put("duration", duration);
            carData.put("constructor", constructor);
            carData.put("model", model);
            carData.put("insurance", insuranceChoice);

            Map<String, Object> carsMap = new HashMap<>();
            carsMap.put("Car", carData);
            carList.add(carsMap);
        }
        return carList;
    }
}
