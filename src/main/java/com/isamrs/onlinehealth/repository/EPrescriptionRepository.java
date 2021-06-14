package com.isamrs.onlinehealth.repository;

import com.isamrs.onlinehealth.model.EPrescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EPrescriptionRepository extends JpaRepository<EPrescription, Long> {

    List<EPrescription> findAllByPatientId(Long id);
}
