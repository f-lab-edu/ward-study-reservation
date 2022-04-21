package com.dsg.wardstudy.dto.studyGroup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyGroupResponse {

    private String title;
    private String content;
}
