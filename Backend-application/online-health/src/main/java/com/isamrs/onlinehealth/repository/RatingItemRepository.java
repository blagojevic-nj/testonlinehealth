package com.isamrs.onlinehealth.repository;

import com.isamrs.onlinehealth.model.RatingItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingItemRepository extends JpaRepository<RatingItem, Long> {
}
