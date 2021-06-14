package com.isamrs.onlinehealth.repository;

import com.isamrs.onlinehealth.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount,Long> {
}
