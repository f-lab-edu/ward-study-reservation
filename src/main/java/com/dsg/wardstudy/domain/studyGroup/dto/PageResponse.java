package com.dsg.wardstudy.domain.studyGroup.dto;

import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public class PageResponse {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class StudyGroupDetail {
        private List<StudyGroupResponse> content;
        private int pageNo;
        private int pageSize;
        private long totalElements;
        private int totalPages;
        private boolean last;
    }

    public static StudyGroupDetail of(Pageable pageable, Page<StudyGroupResponse> studyGroupResponsePage) {
        return PageResponse.StudyGroupDetail.builder()
                .content(studyGroupResponsePage.getContent())
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalElements(studyGroupResponsePage.getTotalElements())
                .totalPages(studyGroupResponsePage.getTotalPages())
                .last(studyGroupResponsePage.isLast())
                .build();
    }

}
