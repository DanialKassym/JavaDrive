package com.example.JavaDrive.web.controller.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/files")
@RestController
@RequiredArgsConstructor
public class UserFileController {

    @GetMapping("/dashboard")
    @Transactional
    public ResponseEntity<Resource> listUploadedFiles(HttpServletRequest request) {
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

    /*@PostMapping("/upload")
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
    }*/

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long fileId) {
        // Логика удаления файла (с проверкой, принадлежит ли он пользователю)
        return ResponseEntity.noContent().build();
    }
}
