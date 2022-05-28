package com.dsg.wardstudy.repository.reservation;

import com.dsg.wardstudy.domain.reservation.ReservationDeal;
import com.dsg.wardstudy.type.Status;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static com.dsg.wardstudy.domain.reservation.QReservation.reservation;
import static com.dsg.wardstudy.domain.reservation.QReservationDeal.reservationDeal;

@Repository
public class ReservationQueryRepository {

    private final JPAQueryFactory queryFactory;

    public ReservationQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<ReservationDeal> findByStatusIsEnabledAndStartTimeBeforeNow(List<Long> sgIds) {
        return queryFactory
                .selectFrom(reservationDeal)
                .join(reservationDeal.reservation, reservation)
                .where(reservationDeal.status.eq(Status.ENABLED)
                        .and(reservation.startTime.after(LocalDateTime.now()))
                        .and(reservation.studyGroup.id.in(sgIds)))
                .fetch();
    }

}
