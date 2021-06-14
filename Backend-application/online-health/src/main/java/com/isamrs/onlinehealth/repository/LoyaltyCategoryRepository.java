package com.isamrs.onlinehealth.repository;

import com.isamrs.onlinehealth.model.LoyaltyCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoyaltyCategoryRepository extends JpaRepository<LoyaltyCategory, Long> {
    LoyaltyCategory findOneById(Long id);
}
