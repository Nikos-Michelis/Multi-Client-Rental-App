package com.nick.server.rental_crud_utilities;

import java.io.*;
import java.util.List;
import java.util.Map;

public class FileHandler{

    public List<Map<String, Object>> loadFromFile(List<Map<String, Object>> loadedRentalArrayList, File file, String message){
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                loadedRentalArrayList = (List<Map<String, Object>>) ois.readObject();
                System.out.println(message + ": " + loadedRentalArrayList);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            System.out.println("File not exists...");
        }
        return loadedRentalArrayList;
    }
    public void saveToFile(List<Map<String, Object>> loadedRentalArrayList, File file, String message){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(loadedRentalArrayList);
            System.out.println(message + ": " + loadedRentalArrayList);
        } catch (IOException e) {
                e.printStackTrace();
        }
    }
}
