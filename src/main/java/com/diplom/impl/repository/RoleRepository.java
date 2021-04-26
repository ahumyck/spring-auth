package com.diplom.impl.repository;

import com.diplom.impl.model.entity.Attribute;
import com.diplom.impl.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByRoleName(String roleName);
}
