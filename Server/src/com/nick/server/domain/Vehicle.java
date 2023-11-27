package com.nick.server.domain;

import java.io.Serial;
import java.io.Serializable;

public abstract class Vehicle extends Rental implements Serializable {
    private String constructor;
    private String model;
    private String fuelType;
    private String color;
    private int orderQuantity;
    private int stockQuantity;
    private int horsePower;
    private int engineSize;
    private int insurance;
    private int vehicleId;
    private boolean isRented;
    @Serial
    private static final long serialVersionUID = 1L; // Serializable UID

    Vehicle(){

    }
    protected Vehicle(Member member, int rentalId, int duration, String constructor, String model, String color,
                      int horsePower, int engineSize, String fuelType, int insurance) {
        super(member, duration, rentalId);
        this.vehicleId = 0;
        this.constructor = constructor;
        this.model = model;
        this.color = color;
        this.horsePower = horsePower;
        this.engineSize = engineSize;
        this.fuelType = fuelType;
        this.insurance = insurance;
        this.orderQuantity = 0;
        this.stockQuantity = 0;
        this.isRented = false;
    }

    public String getConstructor() {
        return constructor;
    }

    public void setConstructor(String constructor) {
        this.constructor = constructor;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getHorsePower() {
        return horsePower;
    }

    public void setHorsePower(int horsePower) {
        this.horsePower = horsePower;
    }
    public int getEngineSize() {
        return engineSize;
    }

    public void setEngineSize(int engineSize) {
        this.engineSize = engineSize;
    }

    public int getInsurance() {
        return insurance;
    }

    public void setInsurance(int insurance) {
        this.insurance = insurance;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public boolean isRented() {
        return isRented;
    }

    public void setRented(boolean rented) {
        isRented = rented;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleID) {
        this.vehicleId = vehicleID;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "constructor='" + constructor + '\'' +
                ", model='" + model + '\'' +
                ", fuelType='" + fuelType + '\'' +
                ", color='" + color + '\'' +
                ", orderQuantity=" + orderQuantity +
                ", stockQuantity=" + stockQuantity +
                ", horsePower=" + horsePower +
                ", engineSize=" + engineSize +
                ", insurance=" + insurance +
                ", isRented=" + isRented +
                '}';
    }
}
