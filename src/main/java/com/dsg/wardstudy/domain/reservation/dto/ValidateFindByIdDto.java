package com.dsg.wardstudy.domain.reservation.dto;

import com.dsg.wardstudy.domain.reservation.Reservation;
import com.dsg.wardstudy.domain.reservation.Room;
import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.domain.user.User;
import lombok.Builder;
import lombok.Data;

@Data
public class ValidateFindByIdDto {

    private User user;
    private StudyGroup studyGroup;
    private Room room;
    private Reservation reservation;

    @Builder
    public ValidateFindByIdDto(User user, StudyGroup studyGroup, Room room, Reservation reservation) {
        this.user = user;
        this.studyGroup = studyGroup;
        this.room = room;
        this.reservation = reservation;
    }

}
