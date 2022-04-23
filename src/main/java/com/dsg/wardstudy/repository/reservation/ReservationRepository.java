package com.dsg.wardstudy.repository.reservation;

import com.dsg.wardstudy.domain.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
