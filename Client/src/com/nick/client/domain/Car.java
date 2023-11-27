package com.nick.client.domain;

import java.io.Serial;
import java.io.Serializable;

public class Car extends Vehicle implements Serializable {
    private double totalCost;
    private double discount;
    private String carType;
    @Serial
    private static final long serialVersionUID = 1L; // Serializable UID
    public Car(){}

    public Car(Member member, int duration, int rentalID, String constructor, String model, String color,
               int horsePower, int engineSize, String fuelType, String carType, int insurance){
        super(member, duration, rentalID, constructor, model, color, horsePower, engineSize, fuelType, insurance);
        this.carType = carType;
        this.totalCost = 0.0;
        this.discount = 0.0;
    }
    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    @Override
    public String toString() {
        return "Car{" +
                "rentalID=" + getRentalId() +
                ", vehicleID=" + getVehicleID() +
                ", constructor=" + getConstructor() +
                ", model=" + getModel() +
                ", color=" + getColor() +
                ", horsePower=" + getHorsePower() +
                ", engineSize=" + getEngineSize() +
                ", fuelType=" + getFuelType() +
                ", carType=" + getCarType() +
                ", insurance=" + getInsurance() +
                ", duration=" + getDuration() +
                ", stockQuantity=" + getStockQuantity() +
                ", orderQuantity=" + getOrderQuantity() +
                ", totalCost=" + getTotalCost() +
                ", Discount=" + getDiscount() +
                ", " + getMember() +
                '}';
    }
}
