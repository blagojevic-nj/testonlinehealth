package com.isamrs.onlinehealth.repository;

import com.isamrs.onlinehealth.model.PharmacyAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PharmacyAdminRepository extends JpaRepository<PharmacyAdmin, Long> {
    PharmacyAdmin findOneByUsername(String username);

    PharmacyAdmin findOneByEmail(String email);

    PharmacyAdmin findOneByUsernameAndPassword(String username, String password);

    PharmacyAdmin findPharmacyAdminByPharmacyId(Long pharmacyId);


    @Query(value = "select * from pharmacy_admins where pharmacy_pharmacy_id = ?1", nativeQuery = true)
    List<PharmacyAdmin> findAllByPharmacyId(Long pharmacyId);
}
