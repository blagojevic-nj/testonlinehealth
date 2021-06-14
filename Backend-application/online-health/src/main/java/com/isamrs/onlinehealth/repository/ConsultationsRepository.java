package com.isamrs.onlinehealth.repository;

import com.isamrs.onlinehealth.model.Consultation;
import com.isamrs.onlinehealth.model.Examination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ConsultationsRepository extends JpaRepository<Consultation, Long> {

    List<Consultation> findByPatient_username(String username);

    List<Consultation> findByPharmacyId(Long id);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query(value = "select c from Consultation c where (c.pharmacist is null or c.pharmacist.username=?1) and c.patient is null and c.start > ?2 and c.pharmacy.id = ?3 ")
    Set<Consultation> findConsultationsForScheduling(String username, LocalDateTime now, Long pharmacyId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select c from Consultation c where c.id = ?1")
    Optional<Consultation> findByIdAndLock(Long consultationId);

    @Query(value = "select c from Consultation c where c.patient.id = ?1")
    Set<Consultation> findConsultationsForPatient(Long id);

    @Query(value = "select * from consultations where pharmacy_id= ?1 and start_time between ?2 and ?3 and finished",nativeQuery = true)
    Set<Consultation> getBetweenDatesForPharmacy(long id, LocalDateTime dt1, LocalDateTime dt2);

    @Query(value = "select count(*) from consultations where pharmacy_id= ?1 and start_time between ?2 and ?3 and finished",nativeQuery = true)
    int countBetweenDatesForPharmacy(long id, LocalDateTime dt1, LocalDateTime dt2);

    //@Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select * from consultations where pharmacy_id= ?1 and pharmacist = ?2 and ?3 between start_time and end_time and ?4 between start_time and end_time", nativeQuery = true)
    Set<Consultation> getAllByPharmacyAndPharmacist(long pharmacyId, long pharmacistId, LocalDateTime start, LocalDateTime end);
}
