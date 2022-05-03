package com.dsg.wardstudy.dto.reservation;

import com.dsg.wardstudy.domain.reservation.Room;
import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.domain.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ReservationDetails {

    private String id;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    private User user;

    private StudyGroup studyGroup;

    private Room room;

    @Builder
    public ReservationDetails(String id, LocalDateTime startTime, LocalDateTime endTime, User user, StudyGroup studyGroup, Room room) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.user = user;
        this.studyGroup = studyGroup;
        this.room = room;
    }
}
