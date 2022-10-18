package com.dsg.wardstudy.common.adapter.mail;

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
                        .map(r -> String.format("<p>스터디그룹: %s, 스터디리더: %s 님</p>\n" +
                                        "<p>룸: %s, 예약시간: %s [%s]~[%s] 으로 등록되었습니다! 😣</p>\n",
                                r.getStudyGroup().getTitle(), r.getUser().getName(),
                                r.getRoom().getName(),formatterStrYearMonthDay(r.getStartTime()),
                                formatterStrTime(r.getStartTime()), formatterStrTime(r.getEndTime())))
                        .collect(Collectors.joining());
    }

    public String toKafkaMessage(String userName, Reservation reservation) {
        return String.format("%s님, ward-study 예약룸 알림 전달드립니다.\n", userName) +
        String.format("<p>스터디그룹: %s, 스터디리더: %s님</p>\n", reservation.getStudyGroup().getTitle(), reservation.getUser().getName()) +
        String.format("<p>룸: %s, 예약시간: %s [%s]~[%s] 으로 등록되었습니다! 😣<p>\n",
                reservation.getRoom().getName(), formatterStrYearMonthDay(reservation.getStartTime()), 
                formatterStrTime(reservation.getStartTime()), formatterStrTime(reservation.getEndTime()));
    }

    private String formatterStrYearMonthDay(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return time.format(formatter);
    }

    private String formatterStrTime(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter);
    }
}
