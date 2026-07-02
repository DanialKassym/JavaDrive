package com.example.JavaDrive.domain.repository;

import com.example.JavaDrive.domain.entity.EmailToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailTokenRepository extends JpaRepository<EmailToken, Long> {
    EmailToken findBytoken(String token);
    EmailToken findByEmail(String email);
}
