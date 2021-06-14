package com.isamrs.onlinehealth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.springframework.data.repository.cdi.Eager;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "medicines")
public class Medicine implements Serializable {
    @Column(
            name = "medicine_id",
            unique = true,
            nullable = false
    )
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "medicine_identifier",
            nullable = false
    )
    private String identifier;
    @Column(
            name = "medicine_name"
    )
    private String name;
    @Column(
            name = "manufacturer"
    )
    private String manufacturer;
    @Column(
            name = "description"
    )
    private String description;
    @Column(
            name = "medicine_type",
            nullable = false
    )
    private String type;
    @ManyToMany(fetch=FetchType.EAGER)
    private Set<Medicine> replacementMedicines = new HashSet<>();
    @Column(
            name = "daily_dose"
    )
    private String dailyDose;
    @Column(
            name = "is_prescription",
            nullable = false
    )
    private boolean isPrescription;
    @Column(
            name = "remarks"
    )
    private String remarks;
    @Column(
            name = "deleted"
    )
    private Boolean deleted = false;

    @Column(name="rating")
    private double rating;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)  @JsonIgnore
    private Set<RatingItem> ratings = new HashSet<>();

    public Medicine(Long id, String identifier, String name, String manufacturer, String description, String type, Set<Medicine> replacementMedicines, String dailyDose, boolean isPrescription, String remarks) {
        this.id = id;
        this.identifier = identifier;
        this.name = name;
        this.manufacturer = manufacturer;
        this.description = description;
        this.type = type;
        this.replacementMedicines = replacementMedicines;
        this.dailyDose = dailyDose;
        this.isPrescription = isPrescription;
        this.remarks = remarks;
    }

    public Medicine(Long id, String identifier, String name, String manufacturer,
                    String description, String type, Set<Medicine> replacementMedicines, String dailyDose,
                    boolean isPrescription, String remarks, Boolean deleted, double rating, Set<RatingItem> ratings) {
        this.id = id;
        this.identifier = identifier;
        this.name = name;
        this.manufacturer = manufacturer;
        this.description = description;
        this.type = type;
        this.replacementMedicines = replacementMedicines;
        this.dailyDose = dailyDose;
        this.isPrescription = isPrescription;
        this.remarks = remarks;
        this.deleted = deleted;
        this.rating = rating;
        this.ratings = ratings;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPrescription() {
        return isPrescription;
    }

    public void setPrescription(boolean prescription) {
        isPrescription = prescription;
    }

    public Medicine() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Medicine> getReplacementMedicines() {
        return replacementMedicines;
    }

    public void setReplacementMedicines(Set<Medicine> replacementMedicines) {
        this.replacementMedicines = replacementMedicines;
    }

    public String getDailyDose() {
        return dailyDose;
    }

    public void setDailyDose(String dailyDose) {
        this.dailyDose = dailyDose;
    }

    public boolean isIsPrescription() {
        return isPrescription;
    }

    public void setIsPrescription(boolean prescription) {
        this.isPrescription = prescription;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Medicine medicine = (Medicine) o;

        return Objects.equals(id, medicine.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Set<RatingItem> getRatings() {
        return ratings;
    }

    public void setRatings(Set<RatingItem> ratings) {
        this.ratings = ratings;
    }
}
