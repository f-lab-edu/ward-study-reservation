package com.dsg.wardstudy.dto.studyGroup;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudyGroupResponse {

    private String title;
    private String content;

    @Builder
    public StudyGroupResponse(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
