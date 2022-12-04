package com.dsg.wardstudy.domain.reservation.service;

import com.dsg.wardstudy.domain.reservation.entity.Reservation;
import com.dsg.wardstudy.domain.reservation.dto.ReservationCommand;
import com.dsg.wardstudy.domain.reservation.dto.ReservationDetails;

import java.util.List;

public interface ReservationService {

    ReservationDetails register(Long studyGroupId, Long roomId, ReservationCommand.RegisterReservation registerReservation);

    List<ReservationDetails> getAllByUserId(Long userId);

    List<ReservationDetails> getByRoomIdAndTimePeriod(Long roomId, String startTime, String endTime);

    List<ReservationDetails> getByRoomId(Long roomId);

    ReservationDetails getByRoomIdAndReservationToken(Long roomId, String reservationToken);

    String updateByToken(Long roomId, String reservationToken, ReservationCommand.UpdateReservation updateReservation);

    void deleteByToken(Long userId, String reservationToken);

    void changeIsEmailSent(Reservation reservation);


}
