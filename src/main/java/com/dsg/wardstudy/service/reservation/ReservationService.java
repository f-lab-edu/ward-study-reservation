package com.dsg.wardstudy.service.reservation;

import com.dsg.wardstudy.domain.reservation.Reservation;
import com.dsg.wardstudy.dto.reservation.ReservationCreateRequest;
import com.dsg.wardstudy.dto.reservation.ReservationDetails;
import com.dsg.wardstudy.dto.reservation.ReservationUpdateRequest;

import java.util.List;

public interface ReservationService {

    ReservationDetails create(Long studyGroupId, Long roomId, ReservationCreateRequest reservationRequest);

    List<ReservationDetails> getAllByUserId(Long userId);

    List<ReservationDetails> getByRoomIdAndTimePeriod(Long roomId, String startTime, String endTime);

    List<ReservationDetails> getByRoomId(Long roomId);

    ReservationDetails getByRoomIdAndReservationId(Long roomId, String reservationId);

    String updateById(Long roomId, String reservationId, ReservationUpdateRequest reservationRequest);

    void deleteById(String reservationId);

    void changeIsEmailSent(Reservation reservation);


}
