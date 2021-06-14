package com.isamrs.onlinehealth.repository;

import com.isamrs.onlinehealth.model.Dermatologist;
import com.isamrs.onlinehealth.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;

@Repository
public interface DermatologistRepository extends JpaRepository<Dermatologist, Long> {
    Dermatologist findOneByUsername(String username);

    @Query(value = "select d from Dermatologist d join fetch d.examinations where d.id= :id")
    Dermatologist findByUsernameAndFetchExaminationsEagerly(@Param("id") Long id);


    Dermatologist findOneByEmail(String email);

    @Query(value = "select * from dermatologists d where lower(d.first_name) like '%' || lower(:name) || '%' or lower(d.last_name) like '%' || lower(:surname) || '%'", nativeQuery = true)
    List<Dermatologist> findByFirst_nameContainingAndLast_nameContaining(@Param("name") String first_name, @Param("surname") String last_name);

    Dermatologist findOneByUsernameAndPassword(String username, String password);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select d from Dermatologist d join fetch d.examinations where d.username = ?1")
    Dermatologist findOneByUsernameAndLock(String dermatologistUsername);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select d from Dermatologist d where d.username = :username")
    Dermatologist findOneByUsernameForRating(@Param("username") String username);

}
