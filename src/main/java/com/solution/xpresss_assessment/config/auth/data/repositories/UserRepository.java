package com.solution.xpresss_assessment.config.auth.data.repositories;

import com.solution.xpresss_assessment.config.auth.data.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAddress(String email);
}
