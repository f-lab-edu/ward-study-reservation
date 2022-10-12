package com.dsg.wardstudy.dto.studyGroup;

import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
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

    public static StudyGroupResponse mapToDto(StudyGroup savedGroup) {
        return StudyGroupResponse.builder()
                .studyGroupId(savedGroup.getId())
                .title(savedGroup.getTitle())
                .content(savedGroup.getContent())
                .build();
    }

    public static StudyGroupResponse mapToDto(UserGroup savedUserGroup) {
        return StudyGroupResponse.builder()
                .studyGroupId(savedUserGroup.getStudyGroup().getId())
                .title(savedUserGroup.getStudyGroup().getTitle())
                .content(savedUserGroup.getStudyGroup().getContent())
                .build();
    }


}
