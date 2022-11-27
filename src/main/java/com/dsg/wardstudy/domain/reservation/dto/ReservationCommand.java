package com.dsg.wardstudy.domain.reservation.dto;

import com.dsg.wardstudy.common.utils.TimeParsingUtils;
import com.dsg.wardstudy.domain.reservation.Reservation;
import com.dsg.wardstudy.domain.reservation.Room;
import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.domain.user.User;
import lombok.*;

public class ReservationCommand {

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class RegisterReservation {

        private Long userId;
        private String startTime;
        private String endTime;


        @Builder
        public RegisterReservation(Long userId, String startTime, String endTime) {
            this.userId = userId;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public Reservation mapToEntity(User user, StudyGroup studyGroup, Room room) {

            return Reservation.builder()
                    .startTime(TimeParsingUtils.formatterLocalDateTime(startTime))
                    .endTime(TimeParsingUtils.formatterLocalDateTime(endTime))
                    .user(user)
                    .studyGroup(studyGroup)
                    .room(room)
                    .build();

        }

    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class UpdateReservation {

        private Long userId;

        private Long studyGroupId;

        private String startTime;

        private String endTime;


        @Builder
        public UpdateReservation(Long userId, Long studyGroupId, String startTime, String endTime) {
            this.userId = userId;
            this.studyGroupId = studyGroupId;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public Reservation mapToEntity(ValidateFindByIdDto validateFindByIdDto) {

            return Reservation.builder()
                    .startTime(TimeParsingUtils.formatterLocalDateTime(startTime))
                    .endTime(TimeParsingUtils.formatterLocalDateTime(endTime))
                    .user(validateFindByIdDto.getUser())
                    .studyGroup(validateFindByIdDto.getStudyGroup())
                    .room(validateFindByIdDto.getRoom())
                    .build();

        }
    }

}
