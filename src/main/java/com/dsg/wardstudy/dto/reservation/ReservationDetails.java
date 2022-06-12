package com.dsg.wardstudy.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ReservationDetails {

    private String id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;


    private Long registerId;

    private String registerEmail;

    private Long studyGroupId;

    private String studyGroupTitle;

    private Long roomId;
    private String roomName;

    @Builder
    public ReservationDetails(String id, LocalDateTime startTime, LocalDateTime endTime, Long registerId, String registerEmail, Long studyGroupId, String studyGroupTitle, Long roomId, String roomName) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.registerId = registerId;
        this.registerEmail = registerEmail;
        this.studyGroupId = studyGroupId;
        this.studyGroupTitle = studyGroupTitle;
        this.roomId = roomId;
        this.roomName = roomName;
    }
}
