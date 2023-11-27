package com.nick.server.domain;

import java.io.Serial;
import java.io.Serializable;

public class Room extends Hotel implements Pricing, Serializable {
    private int persons;
    private int roomId;
    private int roomNumber;
    private String roomType;
    private double totalCost;
    private double discount;
    private boolean parking;
    @Serial
    private static final long serialVersionUID = 1L; // Serializable UID

    public Room(){}
    public Room(Member member, int duration, int rentalId, String hotelName, String hotelType, int roomId, int roomNumber, String roomType, int persons, boolean parking){
        super(member, duration, rentalId, hotelName, hotelType);
        this.persons = persons;
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.parking = parking;
        this.totalCost = 0.0;
        this.discount = 0.0;
    }

    public int getPersons() {
        return persons;
    }

    public void setPersons(int persons) {
        this.persons = persons;
    }

    public boolean isParking() {
        return parking;
    }

    public void setParking(boolean parking) {
        this.parking = parking;
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

    public int getRoomId() { return roomId; }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public double semiCost() {
        if(parking){
            return (persons * 25 + 12) + getDuration();
        }
        return (persons * 25) * getDuration();
    }
    @Override
    public double totalCost() {
      return semiCost() - discount();
    }

    @Override
    public double discount() {
        if(persons > 5){
            return 0.05 * semiCost();
        }else {
            return 0;
        }
    }
    @Override
    public String toString() {
        return "Room{" +
                "rentalId=" + getRentalId() +
                ", roomId=" + roomId +
                ", roomNumber="+ roomNumber +
                ", roomType="+ roomType +
                ", persons=" + persons +
                ", parking=" + parking +
                ", duration=" + getDuration() +
                ", totalCost=" + getTotalCost() +
                ", Discount=" + getDiscount() +
                ", " + getMember() +
                '}';
    }
}
