package com.isamrs.onlinehealth.repository;

import com.isamrs.onlinehealth.model.Supply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SupplyRepository extends JpaRepository<Supply,Long> {


    @Query(value = "select * from supplies where purchase_order_purchase_order_id = ?1", nativeQuery = true)
    List<Supply> getAllSuppliesByOrderId(Long Id);
}
