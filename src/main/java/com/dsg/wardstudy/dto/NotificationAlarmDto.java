package com.dsg.wardstudy.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class NotificationAlarmDto {

    private String id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    private Long userId;

    private Long studyGroupId;

    private Long roomId;

    @Builder
    public NotificationAlarmDto(String id, LocalDateTime startTime, LocalDateTime endTime, Long userId, Long studyGroupId, Long roomId) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userId = userId;
        this.studyGroupId = studyGroupId;
        this.roomId = roomId;
    }

    public String toMessage() {
        return String.format("%s님 예약룸 알림\n" +
                "[%s]-[%s] 예약시간이 잡혔습니다.\n","dsg", startTime, endTime);
//                +
//                String.format("스터디그룹: %s\n" +
//                        "룸: %s\n", studyGroup.getTitle(), room.getName());
    }

}
