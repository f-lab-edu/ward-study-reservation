package com.dsg.wardstudy.service.reservation;

import com.dsg.wardstudy.domain.reservation.Reservation;
import com.dsg.wardstudy.dto.reservation.ReservationCommand;
import com.dsg.wardstudy.dto.reservation.ReservationDetails;

import java.util.List;

public interface ReservationService {

    ReservationDetails register(Long studyGroupId, Long roomId, ReservationCommand.RegisterReservation registerReservation);

    List<ReservationDetails> getAllByUserId(Long userId);

    List<ReservationDetails> getByRoomIdAndTimePeriod(Long roomId, String startTime, String endTime);

    List<ReservationDetails> getByRoomId(Long roomId);

    ReservationDetails getByRoomIdAndReservationId(Long roomId, String reservationId);

    String updateById(Long roomId, String reservationId, ReservationCommand.UpdateReservation updateReservation);

    void deleteById(Long userId, String reservationId);

    void changeIsEmailSent(Reservation reservation);


}
