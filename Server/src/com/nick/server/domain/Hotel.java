package com.nick.server.domain;

import java.io.Serial;
import java.io.Serializable;

public abstract class Hotel extends Rental implements Serializable {
    private String hotelName;
    private String hotelType;
    @Serial
    private static final long serialVersionUID = 1L; // Serializable UID

    public Hotel(){}
    public Hotel(Member member, int duration, int rentalId, String hotelName, String hotelType){
        super(member, duration, rentalId);
        this.hotelName = hotelName;
        this.hotelType = hotelType;
    }
    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                ", hotelName='" + hotelName + '\'' +
                ", hotelType='" + hotelType + '\'' +
                '}';
    }
}
