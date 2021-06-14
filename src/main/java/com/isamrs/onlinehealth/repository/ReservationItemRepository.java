package com.isamrs.onlinehealth.repository;

import com.isamrs.onlinehealth.model.ReservationItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationItemRepository extends JpaRepository<ReservationItem, Long> {
}
