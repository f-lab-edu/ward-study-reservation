package com.dsg.wardstudy.repository.reservation;

import com.dsg.wardstudy.domain.reservation.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from Reservation r where r.reservationToken = :reservationToken")
    Optional<Reservation> findByTokenLock(@Param("reservationToken") String reservationToken);

    Page<Reservation> findBy(Pageable pageable);

    @Query("select r from Reservation r left join fetch r.room where r.room.id = :roomId")
    List<Reservation> findByRoomId(@Param("roomId") Long roomId);

    @Query("select r from Reservation r left join fetch r.room where r.room.id = :roomId " +
            "and r.startTime >= :sTime and r.endTime <= :eTime")
    List<Reservation> findByRoomIdAndTimePeriod(@Param("roomId") Long roomId,
                                                @Param("sTime") LocalDateTime sTime,
                                                @Param("eTime") LocalDateTime eTime);

    @Query("select r from Reservation r left join fetch r.room where r.room.id = :roomId " +
    "or r.startTime = :sTime or r.endTime = :eTime")
    List<Reservation> findByRoomIdAndTime(@Param("roomId") Long roomId,
                                              @Param("sTime") LocalDateTime sTime,
                                              @Param("eTime") LocalDateTime eTime);


    @Query("select r from Reservation r left join fetch r.room where r.room.id = :roomId " +
            "and r.reservationToken = :reservationToken")
    Optional<Reservation> findByRoomIdAndToken(@Param("roomId") Long roomId, @Param("reservationToken") String reservationToken);

    @Query("select r from Reservation r left join fetch r.studyGroup where r.studyGroup.id in :sgIds")
    List<Reservation> findByStudyGroupIds(@Param("sgIds") List<Long> sgIds);

}
