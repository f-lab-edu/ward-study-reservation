package com.dsg.wardstudy.domain.studyGroup.dto;

import com.dsg.wardstudy.domain.attach.dto.AttachDTO;
import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
public class StudyGroupRequest {

    @NotBlank
    @Size(min = 4, max = 99, message = "제목은 4글자 이상, 100글자 미만이어야 합니다.")
    private String title;

    @NotBlank
    @Size(min = 4, max = 1999, message = "내용은 4글자 이상, 2000글자 미만이어야 합니다.")
    private String content;
    private MultipartFile file;

    private List<AttachDTO> attachDTOS;

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
