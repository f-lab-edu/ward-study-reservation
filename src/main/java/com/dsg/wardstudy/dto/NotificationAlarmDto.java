package com.dsg.wardstudy.dto;

import com.dsg.wardstudy.domain.reservation.Reservation;
import com.dsg.wardstudy.utils.TimeParsingUtils;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static com.dsg.wardstudy.utils.TimeParsingUtils.formatterString;

@Data
@NoArgsConstructor
public class NotificationAlarmDto {

    private TimeParsingUtils utils;

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


    public String toMessage() {
        return String.format(
                "%s 님, ward-study 예약룸 알림 전달드립니다.\n", userName)
                +
                reservations.stream()
                        .map(r -> String.format("스터디그룹: %s, 스터디리더: %s 님,\n" +
                                "룸: %s, 예약시간: [%s]-[%s]\n",
                                r.getStudyGroup().getTitle(), r.getUser().getName(),
                                r.getRoom().getName(), formatterString(r.getStartTime()), formatterString(r.getEndTime())))
                        .collect(Collectors.joining());
    }


}
