package com.isamrs.onlinehealth.repository;

import com.isamrs.onlinehealth.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<VerificationToken, Long> {
    public VerificationToken findOneByToken(String token);
}
