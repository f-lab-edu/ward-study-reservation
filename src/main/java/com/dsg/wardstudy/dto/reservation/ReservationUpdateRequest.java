package com.dsg.wardstudy.dto.reservation;

import com.dsg.wardstudy.type.UserType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservationUpdateRequest {

    private UserType userType;

    private int status;

    private String startTime;

    private String endTime;


    @Builder
    public ReservationUpdateRequest(UserType userType, int status, String startTime, String endTime) {
        this.userType = userType;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
