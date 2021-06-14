package com.isamrs.onlinehealth.repository;

import com.isamrs.onlinehealth.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {


    List<Medicine> findByNameContainingIgnoreCase(String name);

    List<Medicine> findByIdentifierContaining(String identifier);

    Medicine findByIdentifierEquals(String identifier);

    boolean existsByIdentifierEquals(String identifier);

    Medicine findOneByIdentifier(String identifier);

    Medicine findOneById(Long id);

    @Query(value = "select * from medicines where medicine_id in ?1", nativeQuery = true)
    List<Medicine> findByListOfIds(List<Long> medicineIds);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select m from Medicine m where m.id = :id")
    Medicine findOneByIdForRating(@Param("id") Long id);

}
