package com.isamrs.onlinehealth.model;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "loyalty_categories")
public class LoyaltyCategory implements Serializable {
    @Column(
            name = "loyalty_category_id",
            unique = true,
            nullable = false
    )
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(
            name = "low_limit",
            nullable = false
    )
    private Double lowLimit;
    @Column(
            name = "high_limit",
            nullable = false
    )
    private Double highLimit;
    @Column(
            name = "category_name",
            nullable = false
    )
    private String name;
    @Column(
            name = "discount_rate",
            nullable = false
    )
    @Min(value = 0)
    @Max(value = 1)
    private Double discountRate;
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


    public LoyaltyCategory(Long id, Double lowLimit, Double highLimit, String name, Double discountRate) {
        this.id = id;
        this.lowLimit = lowLimit;
        this.highLimit = highLimit;
        this.name = name;
        this.discountRate = discountRate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LoyaltyCategory() {
    }

    public Double getLowLimit() {
        return lowLimit;
    }

    public void setLowLimit(Double lowLimit) {
        this.lowLimit = lowLimit;
    }

    public Double getHighLimit() {
        return highLimit;
    }

    public void setHighLimit(Double highLimit) {
        this.highLimit = highLimit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Double discountRate) {
        this.discountRate = discountRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoyaltyCategory that = (LoyaltyCategory) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
