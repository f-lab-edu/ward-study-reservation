package com.dsg.wardstudy.service.reservation;

import com.dsg.wardstudy.domain.reservation.Reservation;
import com.dsg.wardstudy.domain.reservation.Room;
import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.dto.reservation.ReservationDetail;
import com.dsg.wardstudy.dto.reservation.ReservationRequest;
import com.dsg.wardstudy.dto.reservation.ReservationUpdateRequest;
import com.dsg.wardstudy.repository.reservation.ReservationRepository;
import com.dsg.wardstudy.repository.reservation.RoomRepository;
import com.dsg.wardstudy.repository.studyGroup.StudyGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final StudyGroupRepository studyGroupRepository;
    private final RoomRepository roomRepository;

//    @Transactional
//    public ReservationDetail create(ReservationRequest reservationRequest) {
//
//        Reservation reservation = mapToEntity(reservationRequest);
//        Reservation saveReservation = reservationRepository.save(reservation);
//
//        return mapToDto(saveReservation);
//    }

    @Transactional
    public ReservationDetail create(ReservationRequest reservationRequest, Long studyGroupId, Long roomId) {
        StudyGroup studyGroup = studyGroupRepository.findById(studyGroupId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Reservation reservation = mapToEntityCreate(reservationRequest, studyGroup, room);
        Reservation saveReservation = reservationRepository.save(reservation);

        return mapToDto(saveReservation);

    }

    @Transactional(readOnly = true)
    public List<ReservationDetail> getAllByUserId(Long userId) {
        return reservationRepository.findByUserId(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReservationDetail> getByRoomIdAndTime(Long roomId, String startTime, String endTime) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime sTime = LocalDateTime.parse(startTime, formatter);
        LocalDateTime eTime = LocalDateTime.parse(endTime, formatter);

        return reservationRepository.findByRoomIdAndTime(roomId, sTime, eTime).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public ReservationDetail getById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return mapToDto(reservation);
    }

//    @Transactional(readOnly = true)
//    public List<ReservationDetail> getAll() {
//        return reservationRepository.findAll()
//                .stream().map(this::mapToDto)
//                .collect(Collectors.toList());
//    }

    @Transactional
    public Long updateById(Long reservationId, ReservationUpdateRequest reservationRequest) {
        Reservation findReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        findReservation.update(
                reservationRequest.getStatus(),
                reservationRequest.getStartTime(),
                reservationRequest.getEndTime()
        );

        return findReservation.getId();
    }

    @Transactional
    public void deleteById(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }


    private Reservation mapToEntity(ReservationRequest reservationRequest) {
        return Reservation.builder()
                .status(1)
                .startTime(reservationRequest.getStartTime())
                .endTime(reservationRequest.getEndTime())
                .build();

    }

    private ReservationDetail mapToDto(Reservation saveReservation) {
        return ReservationDetail.builder()
                .status(saveReservation.getStatus())
                .startTime(saveReservation.getStartTime())
                .endTime(saveReservation.getEndTime())
                .studyGroup(saveReservation.getStudyGroup())
                .room(saveReservation.getRoom())
                .build();
    }

    private Reservation mapToEntityCreate(ReservationRequest reservationRequest, StudyGroup studyGroup, Room room) {
        return Reservation.builder()
                .status(1)
                .startTime(reservationRequest.getStartTime())
                .endTime(reservationRequest.getEndTime())
                .studyGroup(studyGroup)
                .room(room)
                .build();

    }

}
