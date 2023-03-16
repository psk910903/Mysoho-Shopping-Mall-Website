package com.study.springboot.entity.repository;

import com.study.springboot.entity.SnsUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SnsUserRepository extends JpaRepository<SnsUser, Long> {
    Optional<SnsUser> findByEmail(String email);
}