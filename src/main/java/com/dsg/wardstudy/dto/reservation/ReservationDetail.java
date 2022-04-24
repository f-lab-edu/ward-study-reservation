package com.dsg.wardstudy.dto.reservation;

import com.dsg.wardstudy.domain.reservation.Room;
import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
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

    private StudyGroup studyGroup;

    private Room room;

    @Builder
    public ReservationDetail(int status, LocalDateTime startTime, LocalDateTime endTime, StudyGroup studyGroup, Room room) {
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.studyGroup = studyGroup;
        this.room = room;
    }
}
