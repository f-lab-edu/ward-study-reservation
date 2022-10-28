package com.dsg.wardstudy.domain.file;

import com.dsg.wardstudy.common.exception.ErrorCode;
import com.dsg.wardstudy.common.exception.WSApiException;
import com.dsg.wardstudy.common.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


@Log4j2
@Service
@RequiredArgsConstructor
public class FileService {

    private final String FOLDER_PATH = "C:\\WebStudy\\f-lab\\ward-study-reservation\\Files-Upload\\";
    private final FileRepository storageRepository;

    // DB에 저장
    public FileResponse upload(MultipartFile file) throws IOException {
        log.info("upload file: {}", file);
        FileData fileData = storageRepository.save(
                FileData.builder()
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .size(file.getSize())
                        .imageData(FileUtils.compressImage(file.getBytes()))
                        .build());

        return FileResponse.of(fileData);
    }

    public byte[] downloadImage(String fileName) {
        FileData fileData = storageRepository.findByName(fileName)
                .orElseThrow(() -> new WSApiException(ErrorCode.NO_FOUND_ENTITY));

        log.info("download fileData: {}", fileData);

        return FileUtils.decompressImage(fileData.getImageData());
    }

    // 파일경로에 저장
    // 업로드
    public FileResponse uploadImageToFileSystem(MultipartFile file) throws IOException {
        String fileCodeName = FileUtils.getFileCodeName(file);
        log.info("upload fileCodeName: {}", fileCodeName);
        String filePath = FOLDER_PATH + fileCodeName;

        FileData fileData = storageRepository.save(
                FileData.builder()
                        .name(fileCodeName)
                        .type(file.getContentType())
                        .size(file.getSize())
                        .filePath(filePath)
                        .build()
        );
        file.transferTo(new File(filePath));
        return FileResponse.of(fileData);
    }

    // 다운로드
    public byte[] downloadImageFromFileSystem(String fileCodeName) throws IOException {
        FileData fileData = storageRepository.findByName(fileCodeName)
                .orElseThrow(() -> new WSApiException(ErrorCode.NO_FOUND_ENTITY));

        String filePath = fileData.getFilePath();
        log.info("download filePath: {}", filePath);
        return Files.readAllBytes(new File(filePath).toPath());
    }

}
