package com.isamrs.onlinehealth.repository;

import com.isamrs.onlinehealth.model.EPrescriptionItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EPrescriptionItemRepository extends JpaRepository<EPrescriptionItem, Long> {
}
