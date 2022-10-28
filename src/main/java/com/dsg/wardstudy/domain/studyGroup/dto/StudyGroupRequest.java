package com.dsg.wardstudy.domain.studyGroup.dto;

import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class StudyGroupRequest {

    private String title;
    private String content;

    private MultipartFile file;

    @Builder
    public StudyGroupRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Builder
    public StudyGroupRequest(String title, String content, MultipartFile file) {
        this.title = title;
        this.content = content;
        this.file = file;
    }

    public static StudyGroup mapToEntity(StudyGroupRequest studyGroupRequest) {
        return StudyGroup.builder()
                .title(studyGroupRequest.getTitle())
                .content(studyGroupRequest.getContent())
                .build();
    }
}
