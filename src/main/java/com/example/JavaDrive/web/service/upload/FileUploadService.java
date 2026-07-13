package com.example.JavaDrive.web.service.upload;

import com.example.JavaDrive.domain.entity.UploadFile;
import com.example.JavaDrive.domain.entity.Users;
import com.example.JavaDrive.domain.repository.UploadFileRepository;
import com.example.JavaDrive.domain.repository.UserRepository;
import com.example.JavaDrive.exception.FileNotFoundOnDiskException;
import com.example.JavaDrive.exception.ForbbidenException;
import com.example.JavaDrive.exception.ResourceNotFoundException;
import com.example.JavaDrive.utils.JWTTokenUtils;
import com.example.JavaDrive.web.dto.FileDownloadDetails;
import com.example.JavaDrive.web.dto.FileMetadata;
import com.example.JavaDrive.web.dto.UploadFileDto;
import com.example.JavaDrive.web.service.infrastructure.FileSystemStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileUploadService {
    private final FileSystemStorageService storageService;
    private final UserRepository userRepository;
    private final UploadFileRepository uploadFileRepository;
    private final JWTTokenUtils jwtTokenUtils;

    public boolean UploadUserFile(MultipartFile file, String cookie) {

        if (cookie != null && jwtTokenUtils.validateToken(cookie)) {
            String id = jwtTokenUtils.getID(cookie);
            Users user = userRepository.findByid(Long.valueOf(id));
            FileMetadata fileMetadata = generateMetadata(file);
            storageService.store(file, fileMetadata.safeInternalName(), id);
            uploadFileRepository.save(new UploadFile(user, fileMetadata.originalName(), fileMetadata.safeInternalName()));
            return true;
        }
        return false;
    }

    public List<UploadFileDto> getUserFiles(String cookie) throws ResourceNotFoundException {
        if (cookie != null && jwtTokenUtils.validateToken(cookie)) {
            String id = jwtTokenUtils.getID(cookie);

            List<UploadFile> files = uploadFileRepository.findAllByOwner_id(Long.valueOf(id));
            if (files == null || files.isEmpty()) {
                throw new ResourceNotFoundException("No files found for this user");
            }

            return files.stream()
                    .map(entity -> new UploadFileDto(
                            entity.getId(),
                            entity.getOriginal_name(),
                            "file"
                    ))
                    .collect(Collectors.toList());
        }
        return java.util.Collections.emptyList();
    }

    public void validateCookie(Long owner_id, String cookie) {

        if (cookie == null || !jwtTokenUtils.validateToken(cookie)) {
            throw new ForbbidenException("Invalid or missing token");
        }

        String tokenIdStr = jwtTokenUtils.getID(cookie);
        if (tokenIdStr == null) {
            throw new ForbbidenException("Token does not contain a valid ID");
        }
        Long id = Long.valueOf(tokenIdStr);

        if (!Objects.equals(owner_id, id)) {
            throw new ForbbidenException("User requested file doesn't match owner id");
        }
    }

    public FileDownloadDetails getFileForDownload(Long id) {
        UploadFile fileMeta = uploadFileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("File record not found"));

        Long ownerId = fileMeta.getOwner().getId();
        Path absolutePath = Paths.get(
                storageService.getRootLocation(),
                "user_" + ownerId,
                fileMeta.getStorage_name()
        );

        Resource resource = new FileSystemResource(absolutePath);
        if (!resource.exists()) {
            throw new FileNotFoundOnDiskException("Physical file missing on storage disk");
        }

        return new FileDownloadDetails(resource, fileMeta.getOriginal_name());
    }

    public FileMetadata generateMetadata(MultipartFile file) {
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileExtension = StringUtils.getFilenameExtension(originalFilename);

        String safeInternalName = UUID.randomUUID() + (fileExtension != null ? "." + fileExtension : "");

        return new FileMetadata(originalFilename, safeInternalName, fileExtension);
    }

}
