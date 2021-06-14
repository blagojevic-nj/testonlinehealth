package com.isamrs.onlinehealth.repository;

import com.isamrs.onlinehealth.model.LoyaltyProgram;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoyaltyProgramRepository extends JpaRepository<LoyaltyProgram, Long> {
    LoyaltyProgram findOneById(Long id);
}
