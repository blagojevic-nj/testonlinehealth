package com.isamrs.onlinehealth.repository;

import com.isamrs.onlinehealth.model.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    Complaint findOneById(Long id);
}
