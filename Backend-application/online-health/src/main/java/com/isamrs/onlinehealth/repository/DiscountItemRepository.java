package com.isamrs.onlinehealth.repository;

import com.isamrs.onlinehealth.model.DiscountItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountItemRepository extends JpaRepository<DiscountItem, Long> {
}
