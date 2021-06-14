package com.isamrs.onlinehealth.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "price_lists")
public class PriceList implements Serializable {
    @Column(
            name = "price_list_id",
            unique = true,
            nullable = false
    )
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<PriceListItem> priceListItems = new HashSet<>();
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

    public PriceList(Long id, Set<PriceListItem> priceListItems) {
        this.id = id;
        this.priceListItems = priceListItems;
    }

    public PriceList() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<PriceListItem> getPriceListItems() {
        return priceListItems;
    }

    public void setPriceListItems(Set<PriceListItem> priceListItems) {
        this.priceListItems = priceListItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PriceList pricelist = (PriceList) o;

        return Objects.equals(id, pricelist.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}
