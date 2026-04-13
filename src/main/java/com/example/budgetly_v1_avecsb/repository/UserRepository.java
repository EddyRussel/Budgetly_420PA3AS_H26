package com.example.budgetly_v1_avecsb.repository;

import com.example.budgetly_v1_avecsb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//Spring Data JPA génère le SQL automatiquement à partir du nom de la méthode

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // SELECT * FROM users WHERE email = ?
    Optional<User> findByEmail(String email);

    // SELECT COUNT(*) FROM users WHERE email = ?
    boolean existsByEmail(String email);
}