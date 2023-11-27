package com.nick.server.rental_crud;

import com.nick.server.Validation.RentalDeserializer;
import com.nick.server.domain.Car;
import com.nick.server.domain.Room;
import com.nick.server.rental_crud_utilities.FileHandler;
import com.nick.server.rental_crud_utilities.IDRecordHandler;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class ChooseProduct implements IDRecordHandler{
    @Override
    public <T> T findMaxRecordID(List<Map<String, Object>> loadedRentalArrayList, T inputObject, BiConsumer<T, Integer> setter, String getterMethodName) {
        return null;
    }

    private final Path rentedVehiclesPath = Path.of("Your-Path\\rentedVehicles.dat");
    private final Path rentedRoomsPath = Path.of("Your-Path\\rentedRooms.dat");
    private final Path vehiclePath = Path.of("Your-Path\\vehicles.dat");
    private final Path roomPath = Path.of("Your-Path\\rooms.dat");

    public List<Map<String, Object>> chooseProduct(List<Map<String, Object>> itemList) {
        Room inputRoomObject = null;
        Car inputCarObject = null;

        FileHandler fileHandler = new FileHandler();
        RentalDeserializer rentalDeserializer = new RentalDeserializer();
        List<Map<String, Object>> loadedList = new ArrayList<>();
        List<Map<String, Object>> rentedList = new ArrayList<>();
        List<Object> objectsList = new ArrayList<>();
        objectsList = rentalDeserializer.objectsDeserializer(itemList);
        for (var item : objectsList) {
            System.out.println("before instances: " + item.getClass());
            if (item instanceof Room) {
                inputRoomObject = (Room) item;
            } else if (item instanceof Car) {
                inputCarObject = (Car) item;
            } else {
                System.err.println("Error... Unknown object type ");
            }
        }
        if (inputCarObject != null) {
            loadedList = fileHandler.loadFromFile(loadedList, vehiclePath.toFile(), "- Load file 'vehicles.dat'");
            for (var carInfo : loadedList) {
                Car loadedCarObject = (Car) carInfo.get("Car");
                if (inputCarObject.getConstructor().equals(loadedCarObject.getConstructor())
                        && inputCarObject.getModel().equals(loadedCarObject.getModel())) {
                    HashMap<String, Object> map = new HashMap<>();
                    if (loadedCarObject.getStockQuantity() > 0) {
                        loadedCarObject.setStockQuantity(loadedCarObject.getStockQuantity() - 1);
                        fileHandler.saveToFile(loadedList, vehiclePath.toFile(), "- Update file " + vehiclePath.toFile().getName());
                        loadedCarObject.setOrderQuantity(1);
                        loadedCarObject.setTotalCost(inputCarObject.getTotalCost());
                        loadedCarObject.setDiscount(inputCarObject.getDiscount());
                        loadedCarObject.setDuration(inputCarObject.getDuration());
                        loadedCarObject.setInsurance(inputCarObject.getInsurance());
                        loadedCarObject.setMember(inputCarObject.getMember());
                        loadedCarObject.setRented(true);
                        if (!rentedVehiclesPath.toFile().exists()) {
                            Car maxCarObject = findMaxRecordIDDefault(rentedList, loadedCarObject, Car::setRentalId, "getRentalId");
                            map.put("Car", maxCarObject);
                            rentedList.add(map);
                            System.out.println("rentedCarList: " + rentedList);
                            fileHandler.saveToFile(rentedList, rentedVehiclesPath.toFile(), "- Create file 'rentedVehicles.dat'");
                        } else {
                            rentedList = fileHandler.loadFromFile(loadedList, rentedVehiclesPath.toFile(), "- load file 'rentedVehicles.dat'");
                            Car maxCarObject = findMaxRecordIDDefault(rentedList, loadedCarObject, Car::setRentalId, "getRentalId");
                            map.put("Car", maxCarObject);
                            rentedList.add(map);
                            System.out.println("rentedCarList: " + rentedList);
                            fileHandler.saveToFile(rentedList, rentedVehiclesPath.toFile(), "- Save to file 'rentedVehicles.dat'");
                        }
                    } else {
                        System.out.println("sorry vehicle" + loadedCarObject.getConstructor() + " " + loadedCarObject.getModel() + " out of stock!");
                    }

                } else {
                    System.out.println("Car not found...");
                }
            }
        } else if (inputRoomObject != null) {
            loadedList = fileHandler.loadFromFile(loadedList, roomPath.toFile(), "- Load file " + roomPath.toFile().getName());
            for (var roomInfo : loadedList) {
                Room loadedRoomObject = (Room) roomInfo.get("Room");
                if (inputRoomObject.getRoomNumber() == loadedRoomObject.getRoomNumber()) {
                    HashMap<String, Object> map = new HashMap<>();
                    fileHandler.saveToFile(loadedList, roomPath.toFile(), "- Update file " + roomPath.toFile().getName());
                    loadedRoomObject.setTotalCost(inputRoomObject.getTotalCost());
                    loadedRoomObject.setDiscount(inputRoomObject.getDiscount());
                    loadedRoomObject.setDuration(inputRoomObject.getDuration());
                    loadedRoomObject.setMember(inputRoomObject.getMember());
                    if (!rentedRoomsPath.toFile().exists()) {
                        Room maxRoomObject = findMaxRecordIDDefault(rentedList, loadedRoomObject, Room::setRentalId, "getRentalId");
                        map.put("Room", maxRoomObject);
                        rentedList.add(map);
                        System.out.println("rentedRoomList: " + rentedList);
                        fileHandler.saveToFile(rentedList, rentedRoomsPath.toFile(), "- Create file 'rentedRooms.dat'");
                    } else {
                        rentedList = fileHandler.loadFromFile(loadedList, rentedRoomsPath.toFile(), "- load file 'rentedRooms.dat'");
                        Room maxRoomObject = findMaxRecordIDDefault(rentedList, loadedRoomObject, Room::setRentalId, "getRentalId");
                        map.put("Room", maxRoomObject);
                        rentedList.add(map);
                        System.out.println("rentedRoomList: " + rentedList);
                        fileHandler.saveToFile(rentedList, rentedRoomsPath.toFile(), "- Save to file 'rentedRooms.dat'");
                    }
                } else {
                    System.out.println("Room not found...");
                }
            }
        }
        return rentedList;
    }

}
