package com.diplom.impl.repository;

import com.diplom.impl.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByRoleName(String roleName);
}
