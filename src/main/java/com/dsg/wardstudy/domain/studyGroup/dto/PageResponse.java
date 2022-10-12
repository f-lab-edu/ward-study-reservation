package com.dsg.wardstudy.domain.studyGroup.dto;

import lombok.*;

import java.util.List;


public class PageResponse {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class StudyGroup {
        private List<StudyGroupResponse> content;
        private int pageNo;
        private int pageSize;
        private long totalElements;
        private int totalPages;
        private boolean last;
    }

}
