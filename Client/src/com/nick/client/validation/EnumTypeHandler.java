package com.nick.client.validation;

import com.nick.client.enums.CarTypes;
import com.nick.client.enums.FuelTypes;
import com.nick.client.enums.RoomType;

import java.util.ArrayList;
import java.util.List;

public class EnumTypeHandler {
    private final List<String> enumTypesList = new ArrayList<>();
    public String printAllCarTypes() {
        enumTypesList.clear();
        CarTypes[] carTypes = CarTypes.values();
        System.out.println("Available Car Types:");
        for (CarTypes type : carTypes) {
            enumTypesList.add(String.valueOf(type).trim());
        }
        return (enumTypesList + "\nYour Choice is");
    }

    public String printAllFuelTypes() {
        enumTypesList.clear();
        FuelTypes[] fuelTypes = FuelTypes.values();
        System.out.println("Available Fuel Types:");
        for (FuelTypes type : fuelTypes) {
            enumTypesList.add(String.valueOf(type).trim());
        }
        return (enumTypesList + "\nYour Choice is");
    }

    public String printAllRoomTypes() {
        enumTypesList.clear();
        RoomType[] roomTypes = RoomType.values();
        System.out.println("Available Room Types:");
        for (RoomType type : roomTypes) {
            enumTypesList.add(String.valueOf(type).trim());
        }
        return (enumTypesList + "\nYour Choice is");
    }
    public boolean isValidEnumType(String enumType) {
        System.out.println("enumTypesList: " + enumTypesList);
        return enumTypesList.contains(enumType);
    }
}

