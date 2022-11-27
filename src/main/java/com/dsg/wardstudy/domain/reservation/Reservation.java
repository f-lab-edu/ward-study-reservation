package com.dsg.wardstudy.domain.reservation;

import com.dsg.wardstudy.common.utils.TimeParsingUtils;
import com.dsg.wardstudy.common.utils.TokenGenerator;
import com.dsg.wardstudy.domain.BaseTimeEntity;
import com.dsg.wardstudy.domain.reservation.dto.ReservationCommand;
import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.domain.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@ToString(of = {"reservationToken", "startTime", "endTime", "isEmailSent"})
@Table(name = "reservation")
public class Reservation extends BaseTimeEntity {

    private static final String RESERVATION_PREFIX = "reserv_";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @Column(name = "reservation_token", unique = true)
    private String reservationToken;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "start_time")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "end_time")
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "register_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_group_id")
    private StudyGroup studyGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "is_email_sent")
    private boolean isEmailSent;

    @Builder
    public Reservation(LocalDateTime startTime, LocalDateTime endTime, User user, StudyGroup studyGroup, Room room, boolean isEmailSent) {
        this.reservationToken = TokenGenerator.randomCharacterWithPrefix(RESERVATION_PREFIX);
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

    public void update(ReservationCommand.UpdateReservation updateReservation) {
        this.startTime = TimeParsingUtils.formatterLocalDateTime(updateReservation.getStartTime());
        this.endTime = TimeParsingUtils.formatterLocalDateTime(updateReservation.getEndTime());
    }
}
