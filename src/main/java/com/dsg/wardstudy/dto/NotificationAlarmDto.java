package com.dsg.wardstudy.dto;

import com.dsg.wardstudy.domain.reservation.Room;
import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@NoArgsConstructor
public class NotificationAlarmDto {

    private String id;
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private List<UserDto> userDtos;         // user name, email List

    private StudyGroup studyGroup;

    private Room room;

    @Builder
    public NotificationAlarmDto(String id, LocalDateTime startTime, LocalDateTime endTime, List<UserDto> userDtos, StudyGroup studyGroup, Room room) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userDtos = userDtos;
        this.studyGroup = studyGroup;
        this.room = room;
    }

    public String toMessage() {
        return String.format("ward-study 예약룸 알림\n" +
                        String.format("스터디그룹: %s\n" +
                        "룸: %s\n", studyGroup.getTitle(), room.getName()) +
                        "예약시간: [%s]-[%s]\n", formatterLocalDateTimeToString(startTime), formatterLocalDateTimeToString(endTime));

    }

    private String formatterLocalDateTimeToString(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return time.format(formatter);
    }


}
