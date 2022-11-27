package com.dsg.wardstudy.domain.attach;

import com.dsg.wardstudy.domain.BaseTimeEntity;
import com.dsg.wardstudy.domain.attach.dto.AttachDTO;
import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "studyGroup")
@Table(name = "attach")
public class Attach extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uuid;
    private String uploadPath;
    private String fileName;
    private boolean image;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_group_id")
    private StudyGroup studyGroup;

    @Builder
    public Attach(Long id, String uuid, String uploadPath, String fileName, boolean image, StudyGroup studyGroup) {
        this.id = id;
        this.uuid = uuid;
        this.uploadPath = uploadPath;
        this.fileName = fileName;
        this.image = image;
        this.studyGroup = studyGroup;
    }

    public static Attach of(AttachDTO attachDTO, StudyGroup studyGroup) {
        return Attach.builder()
                .uuid(attachDTO.getUuid())
                .uploadPath(attachDTO.getUploadPath())
                .fileName(attachDTO.getFileName())
                .image(attachDTO.isImage())
                .studyGroup(studyGroup)
                .build();
    }

}
