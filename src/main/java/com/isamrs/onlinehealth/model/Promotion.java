package com.isamrs.onlinehealth.model;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "promotions")
public class Promotion implements Serializable {

        @Column(
                name = "promotion_id",
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

        public Promotion() {
        }

        public Promotion(Long id, LocalDateTime start, LocalDateTime end, Double discountRate) {
                this.id = id;
                this.start = start;
                this.end = end;
                this.discountRate = discountRate;
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

                Promotion promotion = (Promotion) o;

                return Objects.equals(id, promotion.id);
        }

        @Override
        public int hashCode() {
                return id != null ? id.hashCode() : 0;
        }
}
