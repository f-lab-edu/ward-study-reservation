package com.dsg.wardstudy.repository.reservation;

import com.dsg.wardstudy.domain.reservation.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, String> {

    Page<Reservation> findBy(Pageable pageable);

    @Query("select r from Reservation r left join fetch r.room where r.room.id = :roomId")
    List<Reservation> findByRoomId(@Param("roomId") Long roomId);

    @Query("select r from Reservation r left join fetch r.room where r.room.id = :roomId " +
            "and r.startTime >= :sTime and r.endTime <= :eTime")
    List<Reservation> findByRoomIdAndTimePeriod(@Param("roomId") Long roomId,
                                                @Param("sTime") LocalDateTime sTime,
                                                @Param("eTime") LocalDateTime eTime);

    @Query("select r from Reservation r left join fetch r.room where r.room.id = :roomId " +
            "and r.id = :reservationId")
    Optional<Reservation> findByRoomIdAndId(@Param("roomId") Long roomId, @Param("reservationId") String reservationId);

    @Query("select r from Reservation r left join fetch r.studyGroup where r.studyGroup.id in :sgIds")
    List<Reservation> findByStudyGroupIds(List<Long> sgIds);

}
