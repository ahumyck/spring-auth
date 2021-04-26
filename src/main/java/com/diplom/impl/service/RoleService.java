package com.diplom.impl.service;

import com.diplom.impl.model.entity.Role;
import com.diplom.impl.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public boolean createRole(String roleName) {
        if (findRole(roleName) == null) {
            roleRepository.save(new Role(roleName));
            return true;
        }
        return false;
    }

    public Role findRole(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }
}
