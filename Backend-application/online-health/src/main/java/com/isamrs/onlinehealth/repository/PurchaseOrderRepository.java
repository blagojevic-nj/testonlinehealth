package com.isamrs.onlinehealth.repository;

import com.isamrs.onlinehealth.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder,Long> {
}
