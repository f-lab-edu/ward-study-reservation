package com.dsg.wardstudy.domain.attach.controller;

import com.dsg.wardstudy.domain.attach.dto.AttachDTO;
import com.dsg.wardstudy.domain.attach.service.AttachService;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@RestController
public class AttachController {

    private final AttachService attachService;

    @Value("${app.firebase-bucket}")
    private String uploadPath;

    /**
     * 스터디그룹 게시글 당 파일첨부 리스트 가져오기
     * @param studyGroupId 스터디그룹 게시글 seq
     * @return ResponseBody in 파일첨부 리스트
     */
    @GetMapping("/attach/list/{studyGroupId}")
    public ResponseEntity<List<AttachDTO>> getAttachList(
            @PathVariable("studyGroupId") Long sgId) {

        log.info("getAttachList sgId: {}", sgId);
        return new ResponseEntity<>(attachService.getAttachList(sgId), HttpStatus.OK);
    }


    /**
     * (다중)파일업로드
     * @param uploadFile (다중)파일
     * @return 첨부파일 리스트
     */
    @PostMapping("/attach/upload")
    public ResponseEntity<List<AttachDTO>> uploadAjaxPost(@RequestParam("file") List<MultipartFile> uploadFile) {

        List<AttachDTO> attachList = new ArrayList<>();

        for (MultipartFile multipartFile : uploadFile) {

            String uploadFileName = multipartFile.getOriginalFilename();
            long size = multipartFile.getSize();
            log.info("-------------------------------------");
            log.info("Upload File Name: " + uploadFileName);
            log.info("Upload File Size: " + size);

            AttachDTO attachDTO = new AttachDTO();

            uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf(File.separator) + 1);
            log.info("only file name: " + uploadFileName);
            attachDTO.setFileName(uploadFileName);

            UUID uuid = UUID.randomUUID();

            uploadFileName = uuid.toString() + "_" + uploadFileName;

            try {
                File saveFile = new File(uploadPath, uploadFileName);
//                multipartFile.transferTo(saveFile);
                Bucket bucket = StorageClient.getInstance().bucket(uploadPath);
                InputStream content = new ByteArrayInputStream(multipartFile.getBytes());
                Blob blob = bucket.create(uploadFileName , content, multipartFile.getContentType());
                log.info("create blob: {}", blob);
                attachDTO.setUuid(uuid.toString());
                attachDTO.setUploadPath(uploadPath);

                if (checkImageType(saveFile)) {
                    attachDTO.setImage(true);
                }

                attachList.add(attachDTO);
            } catch (Exception e) {
                log.error(e.getMessage());
            } // end catch
        } // end for
        return new ResponseEntity<>(attachList, HttpStatus.OK);
    }

    /**
     * 이미지 파일인지 유무 판단 메서드
     * @param file
     * @return boolean
     */
    private boolean checkImageType(File file) {
        try {
            String contentType = Files.probeContentType(file.toPath());
            log.info("checkImageType: {}", contentType);
            return contentType.startsWith("image");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
