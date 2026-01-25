package com.example.JavaDrive.Users;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailTokenRepository extends JpaRepository<EmailToken, Long> {
    EmailToken findBytoken(String token);
    EmailToken findByEmail(String email);
}
