package com.isamrs.onlinehealth.dto;

import com.isamrs.onlinehealth.model.Supplier;

public class SupplierDTO {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String state;
    private String phoneNumber;

    public SupplierDTO(Supplier supplier) {
        this.username=supplier.getUsername();
        this.email=supplier.getEmail();
        this.firstName=supplier.getFirstName();
        this.lastName=supplier.getLastName();
        this.address=supplier.getAddress();
        this.city=supplier.getCity();
        this.state=supplier.getCountry();
        this.phoneNumber=supplier.getPhoneNumber();
    }

    public SupplierDTO() {
    }

    public SupplierDTO(String username, String email, String firstName, String lastName, String address, String city, String state, String phoneNumber) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.state = state;
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
