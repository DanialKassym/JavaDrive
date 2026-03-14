package com.example.JavaDrive.domain.repository;

import com.example.JavaDrive.domain.entity.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {
    List<UploadFile> findAllByOwner_id(Long owner_id);
}
