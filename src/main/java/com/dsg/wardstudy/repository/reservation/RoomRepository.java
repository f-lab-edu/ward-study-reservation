package com.dsg.wardstudy.repository.reservation;

import com.dsg.wardstudy.domain.reservation.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("select r from Room r where r.id = :roomId")
    Optional<Room> findById(@Param("roomId") String roomId);
}
