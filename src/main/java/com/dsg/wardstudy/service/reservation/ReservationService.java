package com.dsg.wardstudy.service.reservation;

import com.dsg.wardstudy.domain.reservation.Reservation;
import com.dsg.wardstudy.dto.reservation.ReservationDetail;
import com.dsg.wardstudy.dto.reservation.ReservationRequest;
import com.dsg.wardstudy.repository.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    @Transactional
    public ReservationDetail create(ReservationRequest reservationRequest) {

        Reservation reservation = mapToEntity(reservationRequest);
        Reservation saveReservation = reservationRepository.save(reservation);

        return mapToDto(saveReservation);
    }

    private ReservationDetail mapToDto(Reservation saveReservation) {
        return ReservationDetail.builder()
                .status(saveReservation.getStatus())
                .startTime(saveReservation.getStartTime())
                .endTime(saveReservation.getEndTime())
                .build();
    }

    private Reservation mapToEntity(ReservationRequest reservationRequest) {
        return Reservation.builder()
                .status(reservationRequest.getStatus())
                .startTime(reservationRequest.getStartTime())
                .endTime(reservationRequest.getEndTime())
                .build();

    }
    @Transactional(readOnly = true)
    public ReservationDetail getById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return mapToDto(reservation);
    }
    @Transactional(readOnly = true)
    public List<ReservationDetail> getAll() {
        return reservationRepository.findAll()
                .stream().map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long updateById(Long reservationId, ReservationRequest reservationRequest) {
        Reservation findReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        findReservation.update(
                reservationRequest.getStatus(),
                reservationRequest.getStartTime(),
                reservationRequest.getEndTime());

        return findReservation.getId();
    }

    @Transactional
    public void deleteById(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }

    public List<ReservationDetail> getAllByUserId(Long userId) {
        return reservationRepository.findByUserId(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
