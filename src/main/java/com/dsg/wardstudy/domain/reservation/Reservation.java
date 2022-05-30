package com.dsg.wardstudy.domain.reservation;

import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.domain.user.User;
import com.dsg.wardstudy.dto.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "startTime", "endTime", "isEmailSent"})
public class Reservation extends BaseTimeEntity {

    @Id
    @Column(name = "reservation_id")
    private String id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "register_id")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_group_id")
    @JsonIgnore
    private StudyGroup studyGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    @JsonIgnore
    private Room room;

    private boolean isEmailSent;

    @Builder
    public Reservation(String id, LocalDateTime startTime, LocalDateTime endTime, User user, StudyGroup studyGroup, Room room, boolean isEmailSent) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.user = user;
        this.studyGroup = studyGroup;
        this.room = room;
        this.isEmailSent = isEmailSent;
    }

    public void changeIsEmailSent(boolean isEmailSent) {
        this.isEmailSent = isEmailSent;
    }

}
