package com.nick.server.domain;

import java.io.Serial;
import java.io.Serializable;

public class Car extends Vehicle implements Pricing, Serializable{
    private double totalCost;
    private double discount;
    private String carType;
    @Serial
    private static final long serialVersionUID = 1L; // Serializable UID
    public Car(){}

    public Car(Member Member, int duration, int rentalId, String constructor, String model, String color,
               int horsePower, int engineSize, String fuelType, String carType, int insurance){
        super(Member, duration, rentalId, constructor, model, color, horsePower, engineSize, fuelType, insurance);
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

    public double semiCost(){
        double semiTotalCost = 0.0, finalCost = 0.0;
        if(getEngineSize() >= 0 && getEngineSize() <= 1100){
            semiTotalCost = getDuration() * 30;
        }else if (getEngineSize() >= 1101 && getEngineSize() <= 1600) {
            semiTotalCost = getDuration() * 40;
        }else{
            semiTotalCost = getDuration() * 50;
        }
        switch (getInsurance()){
            case 1:
                finalCost = (semiTotalCost + 50);
                break;
            case 2:
                finalCost = (semiTotalCost + 100);
                break;
        }
        return finalCost;
    }
    @Override
    public double totalCost() {
        return semiCost() - discount();
    }
    @Override
    public double discount() {
        if(getDuration() > 10){
            return 0.1 * semiCost();
        }else{
            return 0;
        }
    }
    @Override
    public String toString() {
        return "Car{" +
                "rentalId=" + getRentalId() +
                ", vehicleId=" + getVehicleId() +
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
                ", totalCost= " + getTotalCost() +
                ", Discount=" + getDiscount() +
                '}';
    }
}
