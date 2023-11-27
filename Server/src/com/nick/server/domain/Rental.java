package com.nick.server.domain;

import java.io.Serial;
import java.io.Serializable;

public abstract class Rental implements Serializable {
    private int rentalId;
    private Member member;
    private int duration;
    @Serial
    private static final long serialVersionUID = 1L;// Serializable UID

    Rental(){

    }
    Rental(Member member, int duration, int rentalId){
        this.rentalId = rentalId;
        this.member = member;
        this.duration = duration;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
    public int getDuration() {
        return duration;
    }

    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalID) {
        this.rentalId = rentalID;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
    public int addDays(int days){
        return duration += days;
    }
    @Override
    public String toString() {
        return "Rental{" +
                "Member=" + member +
                ", duration=" + duration +
                '}';
    }
}
