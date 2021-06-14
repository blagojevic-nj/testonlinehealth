package com.isamrs.onlinehealth.service;

import com.isamrs.onlinehealth.model.Complaint;
import com.isamrs.onlinehealth.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User findByUsername(String username);
    Optional<User> findById(Long id);
    User findByEmail(String email);
    List<User> findAll ();
    void changePassword(String username, String password);
    void updateProfile(String username, String name, String lastName, String phone, String address, String city, String state);
}
