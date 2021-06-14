package com.isamrs.onlinehealth.repository;

import com.isamrs.onlinehealth.model.SystemAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemAdminRepository extends JpaRepository<SystemAdmin, Long> {
    public SystemAdmin findOneByUsernameAndPassword(String username, String password);
    public SystemAdmin findOneByEmail(String email);
    public SystemAdmin findOneByUsername(String username);
}
