package com.dsg.wardstudy.dto.reservation;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservationUpdateRequest {

    private Long userId;

    private Long studyGroupId;

    private String startTime;

    private String endTime;


    @Builder
    public ReservationUpdateRequest(Long userId, Long studyGroupId, String startTime, String endTime) {
        this.userId = userId;
        this.studyGroupId = studyGroupId;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
