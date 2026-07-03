package com.example.JavaDrive.web.controller.api;

import com.example.JavaDrive.exception.StorageFileNotFoundException;
import com.example.JavaDrive.web.service.upload.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/v1/files")
@RestController
@RequiredArgsConstructor
public class UserFileController {
    private final FileUploadService fileUploadService;
    @GetMapping("/dashboard")
    @Transactional
    public ResponseEntity<Resource> listUploadedFiles(@CookieValue(name = "JWT") String cookie) {
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
                                                   @CookieValue(name = "JWT") String cookie) {
        if (!fileUploadService.UploadUserFile(file,cookie)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long fileId,@CookieValue(name = "JWT") String cookie) {
        return ResponseEntity.noContent().build();
    }
    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
