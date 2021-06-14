package com.isamrs.onlinehealth.repository;

import com.isamrs.onlinehealth.model.Examination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ExaminationsRepository extends JpaRepository<Examination, Long> {

    List<Examination> findByPharmacyId(Long id);


    Optional<Examination> findById(Long id);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query(value = "select e from Examination e where (e.dermatologist is null or e.dermatologist.username=?1) and e.patient is null and e.start > ?2 and e.pharmacy.id = ?3 ")
    Set<Examination> findExaminationForScheduling(String username, LocalDateTime now, Long pharmacyId);

    @Query(value = "select e from Examination e where e.patient.id = ?1")
    Set<Examination> findExaminationsForPatient(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select e from Examination e where e.id = ?1")
    Optional<Examination> findByIdAndLock(Long examinationId);

    @Query(value = "select * from examinations where pharmacy_id= ?1 and start_time between ?2 and ?3 and finished",nativeQuery = true)
    Set<Examination> getBetweenDatesForPharmacy(long id, LocalDateTime dt1, LocalDateTime dt2);

    @Query(value = "select count(*) from examinations where pharmacy_id= ?1 and start_time between ?2 and ?3 and finished",nativeQuery = true)
    int countBetweenDatesForPharmacy(long id, LocalDateTime dt1, LocalDateTime dt2);

}
