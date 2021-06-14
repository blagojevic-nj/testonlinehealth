package com.isamrs.onlinehealth.dto;

import com.isamrs.onlinehealth.model.Dermatologist;

public class DermatologistDTO {
    private long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String state;
    private String phoneNumber;
    private boolean zaposlen = false;
    private double rating;

    public DermatologistDTO(Dermatologist dermatologist) {
        this.id=dermatologist.getId();
        this.username=dermatologist.getUsername();
        this.email=dermatologist.getEmail();
        this.firstName=dermatologist.getFirstName();
        this.lastName=dermatologist.getLastName();
        this.address=dermatologist.getAddress();
        this.city=dermatologist.getCity();
        this.state=dermatologist.getCountry();
        this.phoneNumber=dermatologist.getPhoneNumber();
        this.rating = dermatologist.getRating();

    }

    public DermatologistDTO() {
    }

    public DermatologistDTO(long id, String username, String email, String firstName, String lastName, String address, String city, String state, String phoneNumber) {
        this.id=id;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isZaposlen() {
        return zaposlen;
    }

    public void setZaposlen(boolean zaposlen) {
        this.zaposlen = zaposlen;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

}

