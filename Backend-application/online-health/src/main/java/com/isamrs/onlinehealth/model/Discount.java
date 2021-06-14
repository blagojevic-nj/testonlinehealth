package com.isamrs.onlinehealth.model;

import org.hibernate.annotations.Fetch;
import org.springframework.data.repository.cdi.Eager;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "discounts")
public class Discount implements Serializable {

    @Column(
            name = "discount_id",
            unique = true,
            nullable = false
    )
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(
            name = "start_date"
    )
    private LocalDate start;
    @Column(
            name = "end_date"
    )
    private LocalDate end;
    @OneToMany(fetch = FetchType.EAGER)
    private Set<DiscountItem> discountItems = new HashSet<>();
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


    public Discount(Long id, LocalDate start, LocalDate end, Set<DiscountItem> discountItems) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.discountItems = discountItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Discount() {
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public Set<DiscountItem> getDiscountItems() {
        return discountItems;
    }

    public void setDiscountItems(Set<DiscountItem> discountItems) {
        this.discountItems = discountItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Discount discount = (Discount) o;

        return id.equals(discount.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
