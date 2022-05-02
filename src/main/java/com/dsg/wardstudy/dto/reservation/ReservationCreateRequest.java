package com.dsg.wardstudy.dto.reservation;

import com.dsg.wardstudy.type.UserType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservationCreateRequest {

    private UserType userType;
    private String startTime;
    private String endTime;


    @Builder
    public ReservationCreateRequest(UserType userType, String startTime, String endTime) {
        this.userType = userType;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
