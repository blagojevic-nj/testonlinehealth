package com.isamrs.onlinehealth.repository;

import com.isamrs.onlinehealth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    public User findByUsername(String username);

    public User findByEmail(String email);

}
