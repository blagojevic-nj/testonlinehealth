package com.isamrs.onlinehealth.repository;

import com.isamrs.onlinehealth.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest,Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select l from LeaveRequest l where l.id = ?1")
    LeaveRequest findOneByIdAndLock(Long id);
}
