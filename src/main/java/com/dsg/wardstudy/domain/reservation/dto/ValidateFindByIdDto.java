package com.dsg.wardstudy.domain.reservation.dto;

import com.dsg.wardstudy.domain.reservation.entity.Reservation;
import com.dsg.wardstudy.domain.reservation.entity.Room;
import com.dsg.wardstudy.domain.studyGroup.entity.StudyGroup;
import com.dsg.wardstudy.domain.user.entity.User;
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
