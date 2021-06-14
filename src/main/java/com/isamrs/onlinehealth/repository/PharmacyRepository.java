package com.isamrs.onlinehealth.repository;

import com.isamrs.onlinehealth.model.Medicine;
import com.isamrs.onlinehealth.model.Pharmacist;
import com.isamrs.onlinehealth.model.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.List;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {

    @Query(value = "select * from pharmacies p join locations l " +
            "on p.pharmacy_location = l.location_id where lower(p.name) " +
            "like lower(concat('%', ?1,'%')) and lower(l.address) like lower(concat('%', ?2, '%')) and p.rating >= ?3 and p.rating <= ?4", nativeQuery = true)
    List<Pharmacy> findByNameContainingIgnoreCaseAndAddressContainingIgnoreCase(String name, String address, double ratingLow, double ratingHigh);

    @Query(value = "select * from pharmacies p join locations l " +
            "on p.pharmacy_location = l.location_id where lower(p.name) " +
            "like lower(concat('%', ?1,'%')) and lower(l.address) like lower(concat('%', ?2, '%')) and p.rating >= ?3 and p.rating <= ?4 order by p.name asc", nativeQuery = true)
    List<Pharmacy> findByNameContainingIgnoreCaseAndAddressContainingOrderByNameAsc(String name, String address, double ratingLow, double ratingHigh);

    @Query(value = "select * from pharmacies p join locations l " +
            "on p.pharmacy_location = l.location_id where lower(p.name) " +
            "like lower(concat('%', ?1,'%')) and lower(l.address) like lower(concat('%', ?2, '%')) and p.rating >= ?3 and p.rating <= ?4 order by p.name desc", nativeQuery = true)
    List<Pharmacy> findByNameContainingIgnoreCaseAndAddressContainingOrderByNameDesc(String name, String address, double ratingLow, double ratingHigh);

    @Query(value = "select * from pharmacies p join locations l " +
            "on p.pharmacy_location = l.location_id where lower(p.name) " +
            "like lower(concat('%', ?1,'%')) and lower(l.address) like lower(concat('%', ?2, '%')) and p.rating >= ?3 and p.rating <= ?4 order by l.address asc", nativeQuery = true)
    List<Pharmacy> findByNameContainingIgnoreCaseAndAddressContainingOrderByAddressAsc(String name, String address, double ratingLow, double ratingHigh);

    @Query(value = "select * from pharmacies p join locations l " +
            "on p.pharmacy_location = l.location_id where lower(p.name) " +
            "like lower(concat('%', ?1,'%')) and lower(l.address) like lower(concat('%', ?2, '%')) and p.rating >= ?3 and p.rating <= ?4 order by l.address desc", nativeQuery = true)
    List<Pharmacy> findByNameContainingIgnoreCaseAndAddressContainingOrderByAddressDesc(String name, String address, double ratingLow, double ratingHigh);

    @Query(value = "select * from pharmacies p join locations l " +
            "on p.pharmacy_location = l.location_id where lower(p.name) " +
            "like lower(concat('%', ?1,'%')) and lower(l.address) like lower(concat('%', ?2, '%')) and p.rating >= ?3 and p.rating <= ?4 order by p.rating asc", nativeQuery = true)
    List<Pharmacy> findByNameContainingIgnoreCaseAndAddressContainingOrderByRatingAsc(String name, String address, double ratingLow, double ratingHigh);

    @Query(value = "select * from pharmacies p join locations l " +
            "on p.pharmacy_location = l.location_id where lower(p.name) " +
            "like lower(concat('%', ?1,'%')) and lower(l.address) like lower(concat('%', ?2, '%')) and p.rating >= ?3 and p.rating <= ?4 order by p.rating desc", nativeQuery = true)
    List<Pharmacy> findByNameContainingIgnoreCaseAndAddressContainingOrderByRatingDesc(String name, String address, double ratingLow, double ratingHigh);

    Pharmacy findOneById(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Pharmacy p where p.id = :id")
    Pharmacy findOneByIdForRating(@Param("id") Long id);
}
