package com.nick.server.Validation;

import com.google.gson.internal.LinkedTreeMap;
import com.nick.server.domain.Car;
import com.nick.server.domain.Member;
import com.nick.server.domain.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RentalDeserializer {
    private final List<Object> objectList = new ArrayList<>();
    private Integer getIntegerFromMap(Map<String, Object> map, String key, Integer defaultValue) {
        if (map.containsKey(key)) {
            Object value = map.get(key);
            if (value instanceof Double) {
                return ((Double) value).intValue();
            } else if (value instanceof Integer) {
                return (Integer) value;
            }
        }
        return defaultValue;
    }
    private Double getDoubleFromMap(Map<String, Object> map, String key, Double defaultValue) {
        if (map.containsKey(key)) {
            Object value = map.get(key);
            if (value instanceof Double) {
                return (Double) value;
            }
        }
        return defaultValue;
    }

    private Boolean getBooleanFromMap(Map<String, Object> map, String key, Boolean defaultValue) {
        if (map.containsKey(key)) {
            Object value = map.get(key);
            if (value instanceof Boolean) {
                return (Boolean) value;
            }
        }
        return defaultValue;
    }
    private String getStringFromMap(LinkedTreeMap<String, Object> map, String key, String defaultValue) {
        if (map.containsKey(key)) {
            Object value = map.get(key);
            if (value instanceof String) {
                return (String) value;
            }
        }
        return defaultValue;
    }
    public List<Object> objectsDeserializer(List<Map<String, Object>> rentalArrayList) {

        for (Map<String, Object> item : rentalArrayList) {
            try {
                Map<String, Object> roomData = (Map<String, Object>) item.get("Room");
                Map<String, Object> carData = (Map<String, Object>) item.get("Car");
                if(roomData != null) {
                    Room roomObject = new Room();

                    roomObject.setRentalId(getIntegerFromMap(roomData, "rentalId", 0));
                    roomObject.setHotelName(getStringFromMap((LinkedTreeMap<String, Object>) roomData, "hotelName", ""));
                    roomObject.setRoomId(getIntegerFromMap(roomData, "roomId", 0));
                    roomObject.setRoomNumber(getIntegerFromMap(roomData, "roomNumber", 0));
                    roomObject.setRoomType(getStringFromMap((LinkedTreeMap<String, Object>)roomData, "roomType", ""));
                    roomObject.setDuration(getIntegerFromMap(roomData, "duration", 0));
                    roomObject.setPersons(getIntegerFromMap(roomData, "persons", 0));
                    roomObject.setParking(getBooleanFromMap(roomData, "parking", false));
                    roomObject.setTotalCost(getDoubleFromMap(roomData, "totalCost", 0.0));
                    roomObject.setDiscount(getDoubleFromMap(roomData, "discount", 0.0));

                    if (roomData.containsKey("Member")) {
                        Object memberObject = roomData.get("Member");
                        if (memberObject instanceof LinkedTreeMap) {
                            LinkedTreeMap<String, Object> memberMap = (LinkedTreeMap<String, Object>) memberObject;

                            String name = getStringFromMap(memberMap, "name", "");
                            String surName = getStringFromMap(memberMap, "surName", "");
                            String email = getStringFromMap(memberMap, "email", "");
                            String phone = getStringFromMap(memberMap, "phone", "");
                            String password = getStringFromMap(memberMap, "password", "");
                            String driverLicense = getStringFromMap(memberMap, "driverLicense", "");
                            String role = getStringFromMap(memberMap, "role", "");
                            int memberId = getIntegerFromMap(memberMap, "memberId", 0);

                            Member member = new Member(memberId,name, surName, email, phone, password, driverLicense, role);

                            roomObject.setMember(member);
                        } else {
                            roomObject.setMember(null);
                        }
                    } else {
                        roomObject.setMember(null);
                    }
                    objectList.add(roomObject);
                } else if(carData != null) {
                    Car carObject = new Car();
                    carObject.setRentalId(getIntegerFromMap(carData, "rentalID", 0));
                    carObject.setVehicleId(getIntegerFromMap(carData, "vehicleID", 0));
                    carObject.setDuration(getIntegerFromMap(carData, "duration", 0));
                    carObject.setConstructor(getStringFromMap((LinkedTreeMap<String, Object>)carData, "constructor", ""));
                    carObject.setModel(getStringFromMap((LinkedTreeMap<String, Object>)carData, "model", ""));
                    carObject.setColor(getStringFromMap((LinkedTreeMap<String, Object>)carData, "color", ""));
                    carObject.setHorsePower(getIntegerFromMap(carData, "horsePower", 0));
                    carObject.setEngineSize(getIntegerFromMap(carData, "engineSize", 0));
                    carObject.setFuelType(getStringFromMap((LinkedTreeMap<String, Object>) carData, "fuelType", ""));
                    carObject.setCarType(getStringFromMap((LinkedTreeMap<String, Object>)carData, "carType", ""));
                    carObject.setInsurance(getIntegerFromMap(carData, "insurance", 0));
                    carObject.setStockQuantity(getIntegerFromMap(carData, "stockQuantity", 0));
                    carObject.setOrderQuantity(getIntegerFromMap(carData, "orderQuantity", 0));
                    carObject.setTotalCost(getDoubleFromMap(carData, "totalCost", 0.0));
                    carObject.setDiscount(getDoubleFromMap(carData, "discount", 0.0));

                    if (carData.containsKey("Member")) {
                        Object memberObject = carData.get("Member");
                        if (memberObject instanceof LinkedTreeMap) {
                            LinkedTreeMap<String, Object> memberMap = (LinkedTreeMap<String, Object>) memberObject;

                            String name = getStringFromMap(memberMap, "name", "");
                            String surName = getStringFromMap(memberMap, "surName", "");
                            String email = getStringFromMap(memberMap, "email", "");
                            String phone = getStringFromMap(memberMap, "phone", "");
                            String password = getStringFromMap(memberMap, "password", "");
                            String driverLicense = getStringFromMap(memberMap, "driverLicense", "");
                            String role = getStringFromMap(memberMap, "role", "");
                            int memberId = getIntegerFromMap(memberMap, "memberId", 0);

                            Member member = new Member(memberId, name, surName, email, phone, password, driverLicense, role);

                            carObject.setMember(member);
                        } else {
                            carObject.setMember(null);
                        }
                    } else {
                        carObject.setMember(null);
                    }
                    objectList.add(carObject);
                }
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return objectList;
    }
}

