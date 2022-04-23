package com.dsg.wardstudy.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ReservationDetail {

    private int status;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @Builder
    public ReservationDetail(int status, LocalDateTime startTime, LocalDateTime endTime) {
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
