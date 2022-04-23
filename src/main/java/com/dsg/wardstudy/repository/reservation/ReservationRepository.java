package com.dsg.wardstudy.repository.reservation;

import com.dsg.wardstudy.domain.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);

    @Query("select r from Reservation r where r.room.id = :roomId " +
            "and r.startTime = :sTime and r.endTime = :eTime")
    List<Reservation> findByRoomIdAndTime(@Param("roomId") Long roomId,
                                              @Param("sTime") LocalDateTime sTime,
                                              @Param("eTime") LocalDateTime eTime);
}
