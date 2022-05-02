package com.dsg.wardstudy.repository.reservation;

import com.dsg.wardstudy.domain.reservation.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
