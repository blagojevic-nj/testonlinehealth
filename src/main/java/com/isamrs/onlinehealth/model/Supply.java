package com.isamrs.onlinehealth.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "supplies")
public class Supply implements Serializable {
    @Column(
            name = "supply_id",
            unique = true,
            nullable = false
    )
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(
            name = "total_price",
            nullable = false
    )
    private Double totalPrice;
    @Column(
            name = "due_date",
            nullable = false
    )
    private LocalDateTime dueDate;
    @OneToOne(fetch = FetchType.LAZY)
    private PurchaseOrder purchaseOrder;

    public Supply() {
    }

    @Column(
            name = "deleted"
    )
    private Boolean deleted = false;

    public Supply(Long id, Double totalPrice, LocalDateTime dueDate, PurchaseOrder purchaseOrder) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.dueDate = dueDate;
        this.purchaseOrder = purchaseOrder;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Supply supply = (Supply) o;

        return Objects.equals(id, supply.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
