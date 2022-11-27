package com.dsg.wardstudy.domain.attach.dto;

import com.dsg.wardstudy.domain.attach.Attach;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AttachDTO {

    private String uuid;
    private String uploadPath;
    private String fileName;
    private boolean image;

    private Long sgId;

    @Builder
    public AttachDTO(String uuid, String uploadPath, String fileName, boolean image, Long sgId) {
        this.uuid = uuid;
        this.uploadPath = uploadPath;
        this.fileName = fileName;
        this.image = image;
        this.sgId = sgId;
    }

    public static AttachDTO toDTO(Attach attach) {
        return AttachDTO.builder()
                .uuid(attach.getUuid())
                .uploadPath(attach.getUploadPath())
                .fileName(attach.getFileName())
                .image(attach.isImage())
                .sgId(attach.getStudyGroup().getId())
                .build();
    }

}
