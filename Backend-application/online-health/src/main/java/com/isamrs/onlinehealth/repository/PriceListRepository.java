package com.isamrs.onlinehealth.repository;

import com.isamrs.onlinehealth.model.PriceList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface PriceListRepository extends JpaRepository<PriceList, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from PriceList p where p.id = :id")
    PriceList findOneById(@Param("id") Long id);
}
