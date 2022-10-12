package com.dsg.wardstudy.dto.studyGroup;

import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudyGroupRequest {

    private String title;
    private String content;

    @Builder
    public StudyGroupRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static StudyGroup mapToEntity(StudyGroupRequest studyGroupRequest) {
        return StudyGroup.builder()
                .title(studyGroupRequest.getTitle())
                .content(studyGroupRequest.getContent())
                .build();
    }
}
