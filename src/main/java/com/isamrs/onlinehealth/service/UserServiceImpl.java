package com.isamrs.onlinehealth.service;

import com.isamrs.onlinehealth.model.Complaint;
import com.isamrs.onlinehealth.model.User;
import com.isamrs.onlinehealth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User findByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public List<User> findAll() throws AccessDeniedException {
        return userRepository.findAll();
    }

    @Override
    public void changePassword(String username, String password) {
        User u = userRepository.findByUsername(username);
        u.setPasswordChanged(true);
        u.setPassword(passwordEncoder.encode(password));
        userRepository.save(u);
    }

    @Override
    public void updateProfile(String username, String name, String lastName, String phone, String address, String city, String state) {
        User u = userRepository.findByUsername(username);
        u.setFirstName(name);
        u.setLastName(lastName);
        u.setPhoneNumber(phone);
        u.setAddress(address);
        u.setCity(city);
        u.setCountry(state);
        userRepository.save(u);
    }

}
