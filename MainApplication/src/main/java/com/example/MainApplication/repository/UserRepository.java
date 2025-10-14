package com.example.MainApplication.repository;

import com.example.MainApplication.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

}
