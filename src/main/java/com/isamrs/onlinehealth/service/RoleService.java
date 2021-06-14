package com.isamrs.onlinehealth.service;

import com.isamrs.onlinehealth.model.Role;

import java.util.List;

public interface RoleService {
    Role findById(Long id);
    List<Role> findByName(String name);
}
