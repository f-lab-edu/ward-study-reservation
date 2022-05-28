package com.dsg.wardstudy.adapter;

import com.dsg.wardstudy.domain.reservation.Reservation;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MailMessageGenerator {

    public String toMessage(String userName, List<Reservation> reservations) {
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

    private String formatterString(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return time.format(formatter);
    }
}
