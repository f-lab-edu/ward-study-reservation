package com.dsg.wardstudy.dto.studyGroup;

import lombok.AllArgsConstructor;
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
}
