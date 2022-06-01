package com.dsg.wardstudy.dto.studyGroup;

import com.dsg.wardstudy.domain.user.UserGroup;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudyGroupResponse {

    private String title;
    private String content;

    private UserGroup userGroup;

    @Builder
    public StudyGroupResponse(String title, String content, UserGroup userGroup) {
        this.title = title;
        this.content = content;
        this.userGroup = userGroup;
    }
}
