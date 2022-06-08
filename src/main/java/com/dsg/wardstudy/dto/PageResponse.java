package com.dsg.wardstudy.dto;

import com.dsg.wardstudy.dto.studyGroup.StudyGroupResponse;
import lombok.*;

import java.util.List;


public class PageResponse {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class StudyGroup {
        private List<StudyGroupResponse> content;
        private int pageNo;
        private int pageSize;
        private long totalElements;
        private int totalPages;
        private boolean last;
    }

}
