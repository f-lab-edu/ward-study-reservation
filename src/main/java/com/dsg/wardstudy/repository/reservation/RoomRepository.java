package com.dsg.wardstudy.repository.reservation;

import com.dsg.wardstudy.domain.reservation.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
