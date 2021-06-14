package com.isamrs.onlinehealth.repository;

import com.isamrs.onlinehealth.model.ReservationList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface ReservationRepository extends JpaRepository<ReservationList, Long> {
    @Query(value = "select * from reservation_lists rl join patients p on rl.user_id = p.user_id where p.username = ?1", nativeQuery = true)
    List<ReservationList> getAllForUser(String username);

    @Query(value = "select * from reservation_lists rl join pharmacies p on rl.pharmacy_pharmacy_id = p.pharmacy_id where p.pharmacy_id = ?1", nativeQuery = true)
    List<ReservationList> getAllForPharmacy(long id);

    @Query(value = "select rl from ReservationList  rl where concat(rl.id,'') like :idReservation and rl.deadline>=:plusDays and rl.pharmacy.id=:id and rl.status='RESERVED' ")
    ArrayList<ReservationList> findForIssuing(Long id, LocalDateTime plusDays, String idReservation);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select rl from ReservationList rl where rl.id = :id")
    ReservationList findOneForIssueById(Long id);

    @Query(value = "select * from reservation_lists rl join pharmacies p on rl.pharmacy_pharmacy_id = p.pharmacy_id where p.pharmacy_id = ?1 and deadline between ?2 and ?3 and rl.status = 'ISSUED' ORDER BY deadline DESC", nativeQuery = true)
    List<ReservationList> getAllForPharmacyBetweenDates(long id, LocalDateTime start, LocalDateTime end);

    @Query(value = "select count(*) from reservation_lists rl join pharmacies p on rl.pharmacy_pharmacy_id = p.pharmacy_id where p.pharmacy_id = ?1 and deadline between ?2 and ?3 and rl.status = 'ISSUED'", nativeQuery = true)
    int countAllForPharmacyBetweenDates(long id, LocalDateTime start, LocalDateTime end);
}
