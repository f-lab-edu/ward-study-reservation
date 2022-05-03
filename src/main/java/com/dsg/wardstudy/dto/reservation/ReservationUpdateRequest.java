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
    public ReservationUpdateRequest(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
