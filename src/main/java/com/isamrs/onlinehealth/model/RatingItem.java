package com.isamrs.onlinehealth.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "rating_items")
public class RatingItem implements Serializable {
    @Column(
            name = "rating_item_id",
            unique = true,
            nullable = false
    )
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Patient patient;
    @Column(
            name = "rating"
    )
    private Double rating;
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

    public RatingItem() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RatingItem(Long id, Patient patient, Double rating) {
        this.id = id;
        this.patient = patient;
        this.rating = rating;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RatingItem ratingItem = (RatingItem) o;

        return Objects.equals(id, ratingItem.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
