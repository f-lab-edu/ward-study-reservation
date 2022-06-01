package com.dsg.wardstudy.dto.studyGroup;

import com.dsg.wardstudy.domain.user.UserGroup;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudyGroupResponse {

    private Long studyGroupId;
    private String title;
    private String content;

    @Builder
    public StudyGroupResponse(Long studyGroupId, String title, String content) {
        this.studyGroupId = studyGroupId;
        this.title = title;
        this.content = content;
    }
}
