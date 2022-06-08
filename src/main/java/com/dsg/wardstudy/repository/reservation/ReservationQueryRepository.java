package com.dsg.wardstudy.repository.reservation;

import com.dsg.wardstudy.domain.reservation.Reservation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.dsg.wardstudy.domain.reservation.QReservation.reservation;

@Repository
public class ReservationQueryRepository {

    private final JPAQueryFactory queryFactory;

    public ReservationQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Reservation> findByStartTimeAfterNowAndIsSentFalse(List<Long> sgIds) {
        return queryFactory
                .selectFrom(reservation)
                .where(reservation.startTime.after(LocalDateTime.now())
                        .and(reservation.studyGroup.id.in(sgIds))
                        .and(reservation.isEmailSent.eq(false))
                )
                .fetch();
    }

    public Optional<Reservation> findByUserIdAndStudyGroupId(Long userId, Long studyGroupId) {
        return queryFactory
                .selectFrom(reservation)
                .where(reservation.user.id.eq(userId)
                        .and(reservation.studyGroup.id.eq(studyGroupId))
                ).stream().findFirst();

    }
}
