package com.dsg.wardstudy.domain.reservation;

import com.dsg.wardstudy.dto.BaseTimeEntity;
import com.dsg.wardstudy.type.Status;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reservation_deal")
@ToString(of = {"id", "dealDate", "status"})
public class ReservationDeal extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_deal_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    private LocalDateTime dealDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public ReservationDeal(Long id, Reservation reservation, LocalDateTime dealDate, Status status) {
        this.id = id;
        this.reservation = reservation;
        this.dealDate = dealDate;
        this.status = status;
    }

    public void changeStatus(Status status) {
        this.status = status;
    }

}
