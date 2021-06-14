package com.isamrs.onlinehealth.repository;


import com.isamrs.onlinehealth.model.Medicine;
import com.isamrs.onlinehealth.model.Pharmacist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.List;

public interface PharmacistRepository extends JpaRepository<Pharmacist, Long> {
    @Query(value = "select d from Pharmacist d join fetch d.consultations where d.id= :id")
    Pharmacist findByUsernameAndFetchConsultationsEagerly(@Param("id") Long id);

    Pharmacist findOneByUsername(String username);

    @Query(value = "select * from pharmacists d where lower(d.first_name) like '%' || lower(:name) || '%' or lower(d.last_name) like '%' || lower(:surname) || '%'", nativeQuery = true)
    List<Pharmacist> findByFirst_nameContainingAndLast_nameContaining(@Param("name") String first_name, @Param("surname") String last_name);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select p from Pharmacist p join fetch p.consultations where p.username = ?1")
    Pharmacist findOneByUsernameAndLock(String pharmacistUsername);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Pharmacist p where p.username = :username")
    Pharmacist findOneByUsernameForRating(@Param("username") String username);

}
