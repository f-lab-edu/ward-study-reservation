package com.dsg.wardstudy.domain.reservation.dto;

import com.dsg.wardstudy.domain.reservation.Reservation;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class NotificationAlarmDto {

    private String id;
    private String email;
    private String userName;
    private List<Reservation> reservations;



    @Builder
    public NotificationAlarmDto(String id, String email, String userName, List<Reservation> reservations) {
        this.id = id;
        this.email = email;
        this.userName = userName;
        this.reservations = reservations;
    }

}
