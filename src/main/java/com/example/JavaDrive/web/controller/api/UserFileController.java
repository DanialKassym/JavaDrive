package com.example.JavaDrive.web.controller.api;

import com.example.JavaDrive.web.dto.FileDownloadDetails;
import com.example.JavaDrive.web.dto.UploadFileDto;
import com.example.JavaDrive.web.service.upload.FileUploadService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RequestMapping("/api/v1/files")
@RestController
@RequiredArgsConstructor
public class UserFileController {
    private final FileUploadService fileUploadService;

    @GetMapping("/dashboard")
    @Transactional
    public ResponseEntity<List<UploadFileDto>> listUserFiles(@CookieValue(name = "JWT") String cookie, HttpServletResponse response){
        List<UploadFileDto> userDtoFiles = fileUploadService.getUserFiles(cookie);
        return ResponseEntity.ok(userDtoFiles);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file,
                                                   @CookieValue(name = "JWT") String cookie) {
        if (!fileUploadService.UploadUserFile(file,cookie)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> returnUserFile(@PathVariable Long id,@CookieValue(name = "JWT") String cookie,HttpServletResponse response) {
        fileUploadService.validateCookie(id,cookie);
        FileDownloadDetails details = fileUploadService.getFileForDownload(id);
        Resource resource = details.getResource();

        String contentType = MediaTypeFactory.getMediaType(resource)
                .orElse(MediaType.APPLICATION_OCTET_STREAM)
                .toString();

        String encodedFilename = ContentDisposition.inline()
                .filename(details.getOriginal_name(), StandardCharsets.UTF_8)
                .build()
                .toString();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .header(HttpHeaders.CONTENT_DISPOSITION, encodedFilename)
                .body(resource);

    }
}
