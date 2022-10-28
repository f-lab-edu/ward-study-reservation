package com.dsg.wardstudy.domain.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

    final private FileService storageService;

    // 업로드
    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("file upload : {}", file);
        return ResponseEntity.status(HttpStatus.OK).body(storageService.uploadImageToFileSystem(file));
    }

    // 다운로드
    @GetMapping("/download/{fileCodeName}")
    public ResponseEntity<?> downloadImage(@PathVariable("fileCodeName") String fileCodeName) throws IOException {
        log.info("file download fileCodeName: {}", fileCodeName);
//        byte[] downloadImage = storageService.downloadImage(fileName);
        byte[] downloadImage = storageService.downloadImageFromFileSystem(fileCodeName);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(downloadImage);
    }
}
