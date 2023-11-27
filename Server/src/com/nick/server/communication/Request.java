package com.nick.server.communication;

import java.io.Serializable;

public class Request implements Serializable {
    private String type;
    private String message;
    private String role;
    private int deleteChoice;
    private int totalCostChoice;
    private int registrationChoice;
    private int printRentalChoice;
    private int deleteID;
    public Request(){

    }
    public Request(String type, String message, int deleteID, int deleteChoice, int totalCostChoice, int registrationChoice, int printRentalChoice, String role) {
        this.type = type;
        this.message = message;
        this.deleteID = deleteID;
        this.deleteChoice = deleteChoice;
        this.totalCostChoice = totalCostChoice;
        this.registrationChoice = registrationChoice;
        this.printRentalChoice = printRentalChoice;
        this.role = role;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {

        this.type = type;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getDeleteID() {return deleteID;}

    public void setDeleteID(int deleteID) {
        this.deleteID = deleteID;
    }

    public int getDeleteChoice() {
        return deleteChoice;
    }

    public void setDeleteChoice(int deleteChoice) {
        this.deleteChoice = deleteChoice;
    }

    public int getTotalCostChoice() {
        return totalCostChoice;
    }

    public void setTotalCostChoice(int totalCostChoice) {
        this.totalCostChoice = totalCostChoice;
    }
    public int getRegistrationChoice() {
        return registrationChoice;
    }

    public void setRegistrationChoice(int registrationChoice) {
        this.registrationChoice = registrationChoice;
    }

    public int getPrintRentalChoice() {
        return printRentalChoice;
    }

    public void setPrintRentalChoice(int printRentalChoice) {
        this.printRentalChoice = printRentalChoice;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}