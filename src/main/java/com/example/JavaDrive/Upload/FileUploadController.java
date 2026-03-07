package com.example.JavaDrive.Upload;

import com.example.JavaDrive.Users.UserRepository;
import com.example.JavaDrive.Users.Users;
import com.example.JavaDrive.utils.JWTTokenUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;

@RestController
public class FileUploadController { private final StorageService storageService;
    private final JWTTokenUtils jwtTokenUtils;
    private final UploadFileRepository uploadFileRepository;
    private final UserRepository userRepository;

    @Autowired
    public FileUploadController(StorageService storageService, JWTTokenUtils jwtTokenUtils, UploadFileRepository uploadFileRepository, UserRepository userRepository) {
        this.storageService = storageService;
        this.jwtTokenUtils = jwtTokenUtils;
        this.uploadFileRepository = uploadFileRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/dashboard")
    @Transactional
    public ResponseEntity<Resource> listUploadedFiles(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, "JWT");
        /* TODO to be implemented */
        /*if (cookie != null) {
            String token = cookie.getValue();

            if (jwtTokenUtils.validateToken(token)) {
                String id = jwtTokenUtils.getID(token);
                List<String> files = uploadFileRepository.findAllByowner_id(id);
                if (!files.isEmpty()){
                    ArrayList<Resource> loadedFiles = new ArrayList<>(Arrays.asList());
                    for (int i = 0; i < files.size(); i++) {
                        loadedFiles.add(storageService.loadAsResource(files.get(i)));
                    }
                    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + file.getFilename() + "\"").body(file);
                }
            }
        }*/
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file,
                                                   HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, "JWT");

        if (cookie != null) {
            String token = cookie.getValue();
            if (jwtTokenUtils.validateToken(token)) {
                String id = jwtTokenUtils.getID(token);
                Users user = userRepository.findByid(Long.valueOf(id));
                storageService.store(file);
                uploadFileRepository.save(new UploadFile(file.getOriginalFilename(), user));
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
