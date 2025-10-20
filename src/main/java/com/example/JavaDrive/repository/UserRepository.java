package com.example.JavaDrive.repository;

import com.example.JavaDrive.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

}
