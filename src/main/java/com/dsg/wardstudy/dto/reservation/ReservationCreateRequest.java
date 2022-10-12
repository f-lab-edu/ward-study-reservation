package com.dsg.wardstudy.dto.reservation;

import com.dsg.wardstudy.domain.reservation.Reservation;
import com.dsg.wardstudy.domain.reservation.Room;
import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.domain.user.User;
import com.dsg.wardstudy.utils.TimeParsingUtils;
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

    public static Reservation mapToEntity(ReservationCreateRequest reservationRequest,
                                    User user,
                                    StudyGroup studyGroup,
                                    Room room) {

        return Reservation.builder()
                .startTime(TimeParsingUtils.formatterLocalDateTime(reservationRequest.getStartTime()))
                .endTime(TimeParsingUtils.formatterLocalDateTime(reservationRequest.getEndTime()))
                .user(user)
                .studyGroup(studyGroup)
                .room(room)
                .build();

    }

}
