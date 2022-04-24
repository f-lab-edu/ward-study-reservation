package com.dsg.wardstudy.dto.reservation;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservationUpdateRequest {

    private String id;

    private int status;

    private String startTime;

    private String endTime;


    @Builder
    public ReservationUpdateRequest(String id, int status, String startTime, String endTime) {
        this.id = id;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
