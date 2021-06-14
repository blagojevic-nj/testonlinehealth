package com.isamrs.onlinehealth.model;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "price_list_items")
public class PriceListItem implements Serializable {
    @Column(
            name = "price_list_item_id",
            unique = true,
            nullable = false
    )
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(
            name = "start_date",
            nullable = false
    )
    private LocalDateTime start;
    @Column(
            name = "end_date",
            nullable = false
    )
    private LocalDateTime end;
    @ManyToOne(fetch = FetchType.EAGER)
    private Medicine medicine;
    @Column(
            name = "price",
            nullable = false
    )
    private Double price;
    @Column(
            name = "available_quantity",
            nullable = false
    )
    @Min(value = 0)
    private Integer availableQuantity;
    @Column(
            name = "deleted"
    )
    private Boolean deleted = false;

    @Version
    @Column(nullable = false)
    private Long version;

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public PriceListItem() {
    }

    public PriceListItem(Long id, LocalDateTime start, LocalDateTime end, Medicine medicine, Double price, Integer availableQuantity) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.medicine = medicine;
        this.price = price;
        this.availableQuantity = availableQuantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
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

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PriceListItem that = (PriceListItem) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
