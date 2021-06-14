package com.isamrs.onlinehealth.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "discount_items")
public class DiscountItem implements Serializable {

    @Column(
            name = "discount_item_id",
            unique = true,
            nullable = false
    )
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Medicine medicine;
    @Column(
            name = "price"
    )
    private Double price;
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


    public DiscountItem(Long id, Medicine medicine, Double price) {
        this.id = id;
        this.medicine = medicine;
        this.price = price;
    }

    public DiscountItem() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Medicine getLek() {
        return medicine;
    }

    public void setLek(Medicine medicine) {
        this.medicine = medicine;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DiscountItem that = (DiscountItem) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
