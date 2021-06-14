package com.isamrs.onlinehealth.repository;

import com.isamrs.onlinehealth.dto.AppointedPatientDTO;
import com.isamrs.onlinehealth.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Patient findOneByUsername(String username);

    Patient findOneByEmail(String email);


    @Query(name = "find_examined_dto", nativeQuery = true)
    List<AppointedPatientDTO> findAllExaminedPatients(@Param("username") String username, @Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query(name = "find_consulted_dto", nativeQuery = true)
    List<AppointedPatientDTO> findAllConsultedPatients(@Param("username") String username, @Param("firstName") String firstName, @Param("lastName") String lastName);

    List<Patient> findByFirstNameIgnoreCaseContainsAndLastNameIgnoreCaseContains(String firstName, String lastName);

    Patient findOneByUsernameAndPassword(String username, String password);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select p from Patient p where p.id=?1")
    Optional<Patient> findByIdAndLock(Long patientId);
}
