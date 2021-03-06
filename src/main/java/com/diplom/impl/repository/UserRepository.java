package com.diplom.impl.repository;

import com.diplom.impl.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

    User findByEmail(String email);

    User findByUsernameAndEmail(String username, String email);
}
