package com.dsg.wardstudy.repository.reservation;

import com.dsg.wardstudy.domain.reservation.ReservationDeal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationDealRepository extends JpaRepository<ReservationDeal, Long> {

    Optional<ReservationDeal> findByReservationId(String reservation_id);

}
