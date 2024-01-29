package com.hg.bethunger.repository;

import com.hg.bethunger.model.User;
import com.hg.bethunger.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findAllByRole(UserRole role);
}