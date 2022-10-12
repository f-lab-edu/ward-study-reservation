package com.dsg.wardstudy.domain.reservation.dto;


import com.dsg.wardstudy.common.utils.TimeParsingUtils;
import com.dsg.wardstudy.domain.reservation.Reservation;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservationDetails {

    private String id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String endTime;


    private Long registerId;

    private String registerEmail;

    private Long studyGroupId;

    private String studyGroupTitle;

    private Long roomId;
    private String roomName;

    @Builder
    public ReservationDetails(String id, String startTime, String endTime, Long registerId, String registerEmail, Long studyGroupId, String studyGroupTitle, Long roomId, String roomName) {
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

    public static ReservationDetails mapToDto(Reservation reservation) {
        return ReservationDetails.builder()
                .id(reservation.getId())
                .startTime(TimeParsingUtils.formatterString(reservation.getStartTime()))
                .endTime(TimeParsingUtils.formatterString(reservation.getEndTime()))
                .registerId(reservation.getUser().getId())
                .registerEmail(reservation.getUser().getEmail())
                .studyGroupId(reservation.getStudyGroup().getId())
                .studyGroupTitle(reservation.getStudyGroup().getTitle())
                .roomId(reservation.getRoom().getId())
                .roomName(reservation.getRoom().getName())
                .build();
    }
}
