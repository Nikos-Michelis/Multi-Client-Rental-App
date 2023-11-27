package com.nick.server.rental_crud;

import com.nick.server.Validation.RentalDeserializer;
import com.nick.server.domain.Car;
import com.nick.server.domain.Room;
import com.nick.server.rental_crud_utilities.FileHandler;
import com.nick.server.rental_crud_utilities.IDRecordHandler;

import java.io.File;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiConsumer;

public class CreateRental implements IDRecordHandler {
    private final Path rentedVehiclesPath = Path.of("Your-Path\\rentedVehicles.dat");
    private final Path rentedRoomsPath = Path.of("Your-Path\\rentedRooms.dat");
    private final Path apartmentsPath = Path.of("Your-Path\\rooms.dat");
    private final Path vehiclePath = Path.of("Your-Path\\vehicles.dat");
    private final Path roomPayments = Path.of("Your-Path\\roomPayments.dat");
    private final Path vehiclePayments = Path.of("Your-Path\\vehiclePayments.dat");
    private final ReadWriteLock MemberLock = new ReentrantReadWriteLock();
    // Insert operation
    public List<Map<String, Object>> saveRental(List<Map<String, Object>> rentalList) {
        Room roomObject = null;
        Car carObject = null;
        List<Map<String, Object>> loadedRentalArrayList = new ArrayList<>();
        RentalDeserializer rentalDeserializer = new RentalDeserializer();
        FileHandler fileHandler = new FileHandler();
        synchronized (MemberLock) {
            MemberLock.readLock().lock();
            try {
                // convert LinkedTreeMap to Room and Car Objects
                List<Object> objectsList = rentalDeserializer.objectsDeserializer(rentalList);
                for (var item : objectsList) {
                    System.out.println("before instances: " + item.getClass());
                    if(item instanceof Room) {
                        roomObject = (Room) item;
                    }else if(item instanceof Car) {
                        carObject = (Car) item;
                    }else{
                        System.err.println("Error... Unknown object type ");
                    }
                }
                if (roomObject != null) {
                    System.out.println("file exists: " + apartmentsPath.toFile().exists());
                    if (!apartmentsPath.toFile().exists()) {
                        fileHandler.saveToFile(loadedRentalArrayList, apartmentsPath.toFile(), "- create file 'rooms.dat'");
                    } else {
                        loadedRentalArrayList = fileHandler.loadFromFile(loadedRentalArrayList, apartmentsPath.toFile(), "- Load file 'rooms.dat'");
                    }
                    // Combine loaded data with new data
                    Room maxRoomObject = findMaxRecordIDDefault(loadedRentalArrayList, roomObject, Room::setRoomId, "getRoomId");
                    System.out.println("- Room Object current max ID: " + maxRoomObject);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("Room", maxRoomObject);
                    loadedRentalArrayList.add(map);
                    System.out.println("loadedRentalArrayList: " + loadedRentalArrayList);
                    // Write the combined data back to the file
                    fileHandler.saveToFile(loadedRentalArrayList, apartmentsPath.toFile(), "- Save to 'room.dat'");
                } else if (carObject != null) {
                    if (!vehiclePath.toFile().exists()) {
                        fileHandler.saveToFile(loadedRentalArrayList, vehiclePath.toFile(), "- create file 'vehicles.dat'");
                    } else {
                        loadedRentalArrayList = fileHandler.loadFromFile(loadedRentalArrayList, vehiclePath.toFile(), "- Load file 'vehicles.dat'");
                    }
                    Car maxCarObject = findMaxRecordIDDefault(loadedRentalArrayList, carObject,  Car::setVehicleId, "getVehicleId");
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("Car", maxCarObject);
                    loadedRentalArrayList.add(map);
                    fileHandler.saveToFile(loadedRentalArrayList, vehiclePath.toFile(), "- Save to 'vehicles.dat'");
                } else {
                    System.err.println("Error: Null object...");
                }
            } finally {
                MemberLock.readLock().unlock();
            }
            return loadedRentalArrayList;
        }
    }
    // Delete operation
    public synchronized void DeleteRental(int menuChoice, int deleteID) {
        Room room;
        Car car;
        List<Map<String, Object>> loadedList = new ArrayList<>();
        FileHandler fileHandler = new FileHandler();
        synchronized (MemberLock) {
            MemberLock.readLock().lock();
            try {
                System.out.println("DeleteChoice: " + menuChoice + " DeleteID: " + deleteID);
                switch (menuChoice) {
                    case 1 -> {
                        loadedList = fileHandler.loadFromFile(loadedList, apartmentsPath.toFile(), "- Load file 'room.dat'");
                        boolean recordFound = false;
                        Iterator<Map<String, Object>> roomIterator = loadedList.iterator();
                        while (roomIterator.hasNext()) {
                            Map<String, Object> roomInfo = roomIterator.next();
                            room = (Room) roomInfo.get("Room");
                            if (deleteID == room.getRoomId()) {
                                roomIterator.remove();
                                System.out.println("Deleted record: " + room);
                                recordFound = true;
                                break;
                            }
                        }
                        System.out.println("- remaining items in loaded List: " + loadedList);
                        if (!recordFound) {
                            System.out.println("Record ID " + deleteID + " not found...");
                        } else {
                            fileHandler.saveToFile(loadedList, apartmentsPath.toFile(), "- Save to 'room.dat'");
                        }
                    }
                    case 2 -> {
                        loadedList = fileHandler.loadFromFile(loadedList, vehiclePath.toFile(), "- Load file 'vehicles.dat'");
                        boolean recordFound = false;
                        Iterator<Map<String, Object>> carIterator = loadedList.iterator();
                        while (carIterator.hasNext()) {
                            Map<String, Object> carInfo = carIterator.next();
                            car = (Car) carInfo.get("Car");
                            if (deleteID == car.getVehicleId()) {
                                carIterator.remove();
                                System.out.println("Deleted record: " + car);
                                recordFound = true;
                                break;
                            }
                        }
                        System.out.println("- Remaining items in loaded List: " + loadedList);
                        if (!recordFound) {
                            System.out.println("Record ID " + deleteID + " not found...");
                        } else {
                            fileHandler.saveToFile(loadedList, vehiclePath.toFile(), "- Save to 'vehicles.dat'");
                        }
                    }
                }
            } finally {
                MemberLock.readLock().unlock();
            }
        }
    }
    // Update operation
    public synchronized List<Map<String, Object>> roomTotalCost() {
        Room room;
        List<Map<String, Object>> loadedPaymentsList = new ArrayList<>();
        List<Map<String, Object>> loadedRoomList = new ArrayList<>();
        FileHandler fileHandler = new FileHandler();
        synchronized (MemberLock) {
            MemberLock.readLock().lock();
            try {
                loadedRoomList = fileHandler.loadFromFile(loadedRoomList, rentedRoomsPath.toFile(), "- Load file 'room.dat'");

                for (Map<String, Object> roomInfo : loadedRoomList) {
                    room = (Room) roomInfo.get("Room");
                    room.setTotalCost(room.totalCost());
                    room.setDiscount(room.discount());
                    HashMap<String, Object> roomMap = new HashMap<>();
                    roomMap.put("Room", room);
                    loadedPaymentsList.add(roomMap);
                }
                fileHandler.saveToFile(loadedPaymentsList, roomPayments.toFile(), "Save Room Payments to 'roomPayments.dat'");
            } finally {
                MemberLock.readLock().unlock();
            }
        }
        return loadedPaymentsList;
    }
    public synchronized List<Map<String, Object>> carTotalCost() {
        Car car;
        List<Map<String, Object>> loadedPaymentsList = new ArrayList<>();
        List<Map<String, Object>> loadedCarsList = new ArrayList<>();
        FileHandler fileHandler = new FileHandler();
        synchronized (MemberLock) {
            MemberLock.readLock().lock();
            try {
                loadedCarsList = fileHandler.loadFromFile(loadedCarsList, rentedVehiclesPath.toFile(), "- Load file 'rentedVehicles.dat'");
                for (Map<String, Object> carInfo : loadedCarsList) {
                    car = (Car) carInfo.get("Car");
                    car.setTotalCost(car.totalCost());
                    car.setDiscount(car.discount());
                    System.out.println("- Car object after calculations: " + car);
                    HashMap<String, Object> carMap = new HashMap<>();
                    carMap.put("Car", car);
                    loadedPaymentsList.add(carMap);
                }
                fileHandler.saveToFile(loadedPaymentsList, vehiclePayments.toFile(), "- Save Cars Payments to 'vehiclePayments.dat'");
            } finally {
                MemberLock.readLock().unlock();
            }
        }
        return loadedPaymentsList;
    }
    // Read Operation
    public List<Map<String, Object>> printRecords(File file, String message){
        FileHandler fileHandler = new FileHandler();
        List<Map<String, Object>>  roomPaymentsList = new ArrayList<>();
        roomPaymentsList = fileHandler.loadFromFile(roomPaymentsList, file, message);
        return roomPaymentsList;
    }
   public List<Map<String, Object>> printRecordHandler(String request, int choice){
        List<Map<String, Object>>  printList = new ArrayList<>();
       synchronized (MemberLock) {
           MemberLock.readLock().lock();
           try {
                if(request.equals("PrintRecords")) {
                    switch (choice) {
                        case 1 -> {
                            printList =  printRecords(apartmentsPath.toFile(), "- Load file 'rooms.dat'");
                        }
                        case 2 -> {
                            printList =  printRecords(vehiclePath.toFile(), "- Load file 'vehicle.dat'");
                        }
                    }
                }else if(request.equals("PrintPayments")){
                    switch (choice) {
                        case 1 -> {
                            printList =  printRecords(roomPayments.toFile(), "- Load Room Payments 'roomPayments.dat'");
                        }
                        case 2 -> {
                            printList =  printRecords(vehiclePayments.toFile(), "- Load Cars Payments 'vehiclePayments.dat'");
                        }
                    }
                }
           } finally {
               MemberLock.readLock().unlock();
           }
       }
        return printList;
    }
    @Override
    public <T> T findMaxRecordID(List<Map<String, Object>> loadedRentalArrayList, T inputObject, BiConsumer<T, Integer> setter, String getterMethodName) {
        return null;
    }
}
