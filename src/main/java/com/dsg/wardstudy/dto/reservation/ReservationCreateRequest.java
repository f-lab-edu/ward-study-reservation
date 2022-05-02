package com.dsg.wardstudy.dto.reservation;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservationCreateRequest {

    private String startTime;

    private String endTime;


    @Builder
    public ReservationCreateRequest(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
