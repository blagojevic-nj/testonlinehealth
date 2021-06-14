package com.isamrs.onlinehealth.dto;

import com.isamrs.onlinehealth.model.Pharmacist;
import com.isamrs.onlinehealth.model.Pharmacy;
import com.isamrs.onlinehealth.model.RatingItem;
import com.isamrs.onlinehealth.model.WorkHours;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PharmacistDTO {

    private long id;
    private double rating;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String state;
    private String phoneNumber;
    private String zaposlen;
    private String pharmacy;


    public PharmacistDTO() {
    }

    public PharmacistDTO(Pharmacist p) {
        this.id = p.getId();
        this.username = p.getUsername();
        this.email = p.getEmail();
        this.firstName=p.getFirstName();
        this.lastName = p.getLastName();
        this.address = p.getAddress();
        this.city = p.getCity();
        this.state = p.getCountry();
        this.phoneNumber=p.getPhoneNumber();
        this.zaposlen="false";
        this.initRating(p.getRatings());
    }

    public String getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(String pharmacy) {
        this.pharmacy = pharmacy;
    }

    private void initRating(Set<RatingItem> ratings) {
        DecimalFormat df2 = new DecimalFormat("#.##");
        double ocena =0, counter =0;
        for (RatingItem r : ratings)
        {
            ocena+=r.getRating();
            counter++;
        }
        this.rating = Double.parseDouble(df2.format(ocena/counter));
        if(Double.isNaN(this.rating))
            this.rating=0;


    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
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

    public String getZaposlen() {
        return zaposlen;
    }

    public void setZaposlen(String zaposlen) {
        this.zaposlen = zaposlen;
    }
}
