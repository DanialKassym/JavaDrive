package com.example.JavaDrive.web.service.upload;

import com.example.JavaDrive.domain.entity.UploadFile;
import com.example.JavaDrive.domain.entity.Users;
import com.example.JavaDrive.domain.repository.UploadFileRepository;
import com.example.JavaDrive.domain.repository.UserRepository;
import com.example.JavaDrive.utils.JWTTokenUtils;
import com.example.JavaDrive.web.dto.FileMetadata;
import com.example.JavaDrive.web.service.infrastructure.FileSystemStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadService {
    private final FileSystemStorageService storageService;
    private final UserRepository userRepository;
    private final UploadFileRepository uploadFileRepository;
    private final JWTTokenUtils jwtTokenUtils;

    public boolean UploadUserFile(MultipartFile file,String cookie){

        if (cookie != null) {
            if (jwtTokenUtils.validateToken(cookie)) {
                String id = jwtTokenUtils.getID(cookie);
                Users user = userRepository.findByid(Long.valueOf(id));
                FileMetadata fileMetadata = generateMetadata(file);
                storageService.store(file, fileMetadata.safeInternalName(), id);
                uploadFileRepository.save(new UploadFile(user, fileMetadata.originalName(), fileMetadata.safeInternalName() ));
                return true;
            }
        }
        return false;
    }

    public FileMetadata generateMetadata(MultipartFile file) {
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileExtension = StringUtils.getFilenameExtension(originalFilename);

        String safeInternalName = UUID.randomUUID() + (fileExtension != null ? "." + fileExtension : "");

        return new FileMetadata(originalFilename, safeInternalName, fileExtension);
    }

}
