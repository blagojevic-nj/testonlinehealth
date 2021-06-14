package com.isamrs.onlinehealth.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "pharmacies")
public class Pharmacy implements Serializable {
    @Column(
            name = "pharmacy_id",
            unique = true,
            nullable = false
    )
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(
            name = "name",
            nullable = false
    )
    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pharmacy_location")
    private Location location;
    @Column(
            name = "description",
            nullable = false
    )
    private String description;

    @Column(name="consultation_price")
    private double consultation_price;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "pharmacy")
    private Set<Examination> examinations = new HashSet<>();
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Consultation> consultations = new HashSet<>();
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Dermatologist> dermatologists = new HashSet<>();
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Pharmacist> pharmacists = new HashSet<>();
    @OneToOne
    private PriceList pricelist;
    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<Discount> discounts = new HashSet<>();
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Promotion> promotions = new HashSet<>();
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Patient> subscribers = new HashSet<>();
    @OneToMany(fetch = FetchType.LAZY)
    private Set<RatingItem> ratings = new HashSet<>();
    @Column (name = "rating")
    private double rating;
    @OneToMany(fetch = FetchType.LAZY)
    private Set<EPrescription> EPrescriptions = new HashSet<>();
    @Column(
            name = "deleted"
    )
    private Boolean deleted = false;

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Pharmacy() {
    }

    public Pharmacy(Long id, String name, Location location, String description, Set<Examination> examinations, Set<Consultation> consultations, Set<Dermatologist> dermatologists, Set<Pharmacist> pharmacists, PriceList pricelist, Set<Discount> discounts, Set<Promotion> promotions, Set<Patient> subscribers, Set<RatingItem> ratings, double rating, Set<EPrescription> EPrescriptions) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.description = description;
        this.examinations = examinations;
        this.consultations = consultations;
        this.dermatologists = dermatologists;
        this.pharmacists = pharmacists;
        this.pricelist = pricelist;
        this.discounts = discounts;
        this.promotions = promotions;
        this.subscribers = subscribers;
        this.ratings = ratings;
        this.rating = rating;
        this.EPrescriptions = EPrescriptions;
    }

    public Pharmacy(Long id, String name, Location location, String description, Set<Examination> examinations, Set<Consultation> consultations, Set<Dermatologist> dermatologists, Set<Pharmacist> pharmacists, PriceList pricelist, Set<Discount> discounts, Set<Promotion> promotions, Set<Patient> subscribers, Set<RatingItem> ratings, double rating, Set<EPrescription> EPrescriptions, double price) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.description = description;
        this.examinations = examinations;
        this.consultations = consultations;
        this.dermatologists = dermatologists;
        this.pharmacists = pharmacists;
        this.pricelist = pricelist;
        this.discounts = discounts;
        this.promotions = promotions;
        this.subscribers = subscribers;
        this.ratings = ratings;
        this.rating = rating;
        this.EPrescriptions = EPrescriptions;
        this.consultation_price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Examination> getExaminations() {
        return examinations;
    }

    public void setExaminations(Set<Examination> examinations) {
        this.examinations = examinations;
    }

    public Set<Consultation> getConsultations() {
        return consultations;
    }

    public void setConsultations(Set<Consultation> consultations) {
        this.consultations = consultations;
    }

    public Set<Dermatologist> getDermatologists() {
        return dermatologists;
    }

    public void setDermatologists(Set<Dermatologist> dermatologists) {
        this.dermatologists = dermatologists;
    }

    public Set<Pharmacist> getPharmacists() {
        return pharmacists;
    }

    public void setPharmacists(Set<Pharmacist> pharmacists) {
        this.pharmacists = pharmacists;
    }

    public PriceList getPricelist() {
        return pricelist;
    }

    public void setPricelist(PriceList pricelist) {
        this.pricelist = pricelist;
    }

    public Set<Discount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(Set<Discount> discounts) {
        this.discounts = discounts;
    }

    public Set<Promotion> getPromotions() {
        return promotions;
    }

    public void setPromotions(Set<Promotion> promotions) {
        this.promotions = promotions;
    }

    public Set<Patient> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Set<Patient> subscribers) {
        this.subscribers = subscribers;
    }

    public Set<RatingItem> getRatings() {
        return ratings;
    }

    public void setRatings(Set<RatingItem> ratings) {
        this.ratings = ratings;
    }

    public double getRating() {return rating;}

    public void setRating(double rating) {this.rating = rating;}

    public Set<EPrescription> getePrescriptions() {
        return EPrescriptions;
    }

    public void setePrescriptions(Set<EPrescription> EPrescriptions) {
        this.EPrescriptions = EPrescriptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pharmacy pharmacy = (Pharmacy) o;

        return Objects.equals(id, pharmacy.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public double getConsultation_price() {
        return consultation_price;
    }

    public void setConsultation_price(double consultation_price) {
        this.consultation_price = consultation_price;
    }

    public Set<EPrescription> getEPrescriptions() {
        return EPrescriptions;
    }

    public void setEPrescriptions(Set<EPrescription> EPrescriptions) {
        this.EPrescriptions = EPrescriptions;
    }
}
