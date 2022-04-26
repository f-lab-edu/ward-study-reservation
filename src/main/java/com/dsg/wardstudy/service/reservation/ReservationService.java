package com.dsg.wardstudy.service.reservation;

import com.dsg.wardstudy.domain.reservation.Reservation;
import com.dsg.wardstudy.domain.reservation.Room;
import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.dto.reservation.ReservationDetail;
import com.dsg.wardstudy.dto.reservation.ReservationCreateRequest;
import com.dsg.wardstudy.dto.reservation.ReservationUpdateRequest;
import com.dsg.wardstudy.repository.reservation.ReservationRepository;
import com.dsg.wardstudy.repository.reservation.RoomRepository;
import com.dsg.wardstudy.repository.studyGroup.StudyGroupRepository;
import com.dsg.wardstudy.repository.user.UserGroupRepository;
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

    private final StudyGroupRepository studyGroupRepository;
    private final UserGroupRepository userGroupRepository;
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public ReservationDetail create(ReservationCreateRequest reservationRequest, Long studyGroupId, String roomId) {
        StudyGroup studyGroup = studyGroupRepository.findById(studyGroupId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Reservation reservation = mapToEntity(reservationRequest, studyGroup, room);
        Reservation saveReservation = reservationRepository.save(reservation);

        return mapToDto(saveReservation);

    }

    @Transactional(readOnly = true)
    public List<ReservationDetail> getAllByUserId(Long userId) {
        List<Long> sgIds = userGroupRepository.findsgIdsByUserId(userId);

        return reservationRepository.findBySgIds(sgIds).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<ReservationDetail> getByRoomIdAndTime(String roomId, String startTime, String endTime) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime sTime = LocalDateTime.parse(startTime, formatter);
        LocalDateTime eTime = LocalDateTime.parse(endTime, formatter);

        return reservationRepository.findByRoomIdAndTime(roomId, sTime, eTime).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<ReservationDetail> getByRoomId(String roomId) {
        return reservationRepository.findByRoomId(roomId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReservationDetail getByIds(String roomId, String reservationId) {
        Reservation reservation = reservationRepository.findByIds(roomId, reservationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return mapToDto(reservation);
    }

    @Transactional
    public String updateById(String roomId, String reservationId, ReservationUpdateRequest reservationRequest) {
        Reservation findReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime sTime = LocalDateTime.parse(reservationRequest.getStartTime(), formatter);
        LocalDateTime eTime = LocalDateTime.parse(reservationRequest.getEndTime(), formatter);

        String updateId = room.getId() + "||" + reservationRequest.getStartTime();

         findReservation.update(
                 // Todo: pk 수정 updateId로
                reservationRequest.getStatus(),
                sTime,
                eTime
        );

        return updateId;
    }

    @Transactional
    public void deleteById(String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        reservationRepository.delete(reservation);
    }

    private ReservationDetail mapToDto(Reservation saveReservation) {

        return ReservationDetail.builder()
                .id(saveReservation.getId())
                .status(saveReservation.getStatus())
                .startTime(saveReservation.getStartTime())
                .endTime(saveReservation.getEndTime())
                .studyGroup(saveReservation.getStudyGroup())
                .room(saveReservation.getRoom())
                .build();
    }

    private Reservation mapToEntity(ReservationCreateRequest reservationRequest, StudyGroup studyGroup, Room room) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime sTime = LocalDateTime.parse(reservationRequest.getStartTime(), formatter);
        LocalDateTime eTime = LocalDateTime.parse(reservationRequest.getEndTime(), formatter);

        return Reservation.builder()
                .id(room.getId() + "||" +reservationRequest.getStartTime())
                .status(1)
                .startTime(sTime)
                .endTime(eTime)
                .studyGroup(studyGroup)
                .room(room)
                .build();

    }

}
