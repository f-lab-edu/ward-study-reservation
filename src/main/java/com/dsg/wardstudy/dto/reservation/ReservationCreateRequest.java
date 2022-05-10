package com.dsg.wardstudy.dto.reservation;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservationCreateRequest {

    private Long userId;
    private String startTime;
    private String endTime;


    @Builder
    public ReservationCreateRequest(Long userId, String startTime, String endTime) {
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
