package com.isamrs.onlinehealth.dto;

import com.isamrs.onlinehealth.model.Dermatologist;
import com.isamrs.onlinehealth.model.Patient;

public class PatientDTO {
    private String username;
    private String email;
    private String password;
    private String matchingPassword;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String state;
    private String phoneNumber;

    public PatientDTO(String username, String email, String password, String matchingPassword, String firstName, String lastName, String address, String city, String state, String phoneNumber) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.matchingPassword = matchingPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.state = state;
        this.phoneNumber = phoneNumber;
    }

    public PatientDTO() {
    }

    public PatientDTO(Patient patient) {
        this.username=patient.getUsername();
        this.email=patient.getEmail();
        this.password = patient.getPassword();
        this.matchingPassword = patient.getPassword();
        this.firstName=patient.getFirstName();
        this.lastName=patient.getLastName();
        this.address=patient.getAddress();
        this.city=patient.getCity();
        this.state=patient.getCountry();
        this.phoneNumber=patient.getPhoneNumber();
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
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

