package com.spring_security.Spring_Security_Demo.repository;

import com.spring_security.Spring_Security_Demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
}
