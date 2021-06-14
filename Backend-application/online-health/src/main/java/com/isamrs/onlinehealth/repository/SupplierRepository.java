package com.isamrs.onlinehealth.repository;

import com.isamrs.onlinehealth.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Supplier findOneByEmail(String email);
    Supplier findOneByUsername(String username);
    Supplier findOneByUsernameAndPassword(String username, String password);

    @Query(value = "select s from Supplier s join fetch s.supplies sups where sups.purchaseOrder.id = ?1")
    Set<Supplier> findSuppliersByOrderId(Long purchaseId);
}
