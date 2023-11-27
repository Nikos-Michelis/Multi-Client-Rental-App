package com.nick.server.domain;

import java.io.Serial;
import java.io.Serializable;
public class Member implements Serializable {
    private String surName;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String driverLicense;
    private String role;
    private int memberId;
    @Serial
    private static final long serialVersionUID = 1L; // Serializable UID

    public Member(){}

    public Member(int memberId, String surName, String name, String email, String phone, String password, String driverLicence, String role){
        this.memberId = memberId;
        this.surName = surName;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.driverLicense = driverLicence;
        this.role = role;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverLicense() {
        return driverLicense;
    }

    public void setDriverLicense(String driverLicense) {
        this.driverLicense = driverLicense;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberId='" + memberId + '\'' +
                ", surName='" + surName + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", driverLicence='" + driverLicense + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
