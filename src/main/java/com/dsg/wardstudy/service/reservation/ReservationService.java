package com.dsg.wardstudy.service.reservation;

import com.dsg.wardstudy.domain.reservation.Reservation;
import com.dsg.wardstudy.domain.reservation.Room;
import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.domain.user.User;
import com.dsg.wardstudy.dto.reservation.ReservationCreateRequest;
import com.dsg.wardstudy.dto.reservation.ReservationDetails;
import com.dsg.wardstudy.dto.reservation.ReservationUpdateRequest;
import com.dsg.wardstudy.exception.ErrorCode;
import com.dsg.wardstudy.exception.ResourceNotFoundException;
import com.dsg.wardstudy.repository.reservation.ReservationRepository;
import com.dsg.wardstudy.repository.reservation.RoomRepository;
import com.dsg.wardstudy.repository.studyGroup.StudyGroupRepository;
import com.dsg.wardstudy.repository.user.UserGroupRepository;
import com.dsg.wardstudy.repository.user.UserRepository;
import com.dsg.wardstudy.type.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final StudyGroupRepository studyGroupRepository;
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public ReservationDetails create(ReservationCreateRequest reservationRequest, Long studyGroupId, Long roomId) {

        validateCreateRequest(reservationRequest, studyGroupId);

        User user = userRepository.findById(reservationRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.NO_TARGET));
        StudyGroup studyGroup = studyGroupRepository.findById(studyGroupId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.NO_TARGET));
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.NO_TARGET));

        Reservation reservation = mapToEntity(reservationRequest, user, studyGroup, room);
        Reservation saveReservation = reservationRepository.save(reservation);

        return mapToDto(saveReservation);

    }

    private void validateCreateRequest(ReservationCreateRequest reservationRequest, Long studyGroupId) {

        UserType userType = userGroupRepository.findUserTypeByUserIdAndSGId(
                reservationRequest.getUserId(), studyGroupId).get();

        if (!userType.equals(UserType.L)) {
            throw new IllegalStateException("user가 리더인 분만 예약등록이 가능합니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<ReservationDetails> getAllByUserId(Long userId) {
        List<Long> sgIds = userGroupRepository.findSgIdsByUserId(userId);

        return reservationRepository.findByStudyGroupIds(sgIds).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<ReservationDetails> getByRoomIdAndTimePeriod(Long roomId, String startTime, String endTime) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime sTime = LocalDateTime.parse(startTime, formatter);
        LocalDateTime eTime = LocalDateTime.parse(endTime, formatter);

        return reservationRepository.findByRoomIdAndTimePeriod(roomId, sTime, eTime).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<ReservationDetails> getByRoomId(Long roomId) {
        return reservationRepository.findByRoomId(roomId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReservationDetails getByRoomIdAndReservationId(Long roomId, String reservationId) {
        Reservation reservation = reservationRepository.findByRoomIdAndId(roomId, reservationId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.NO_TARGET));
        return mapToDto(reservation);
    }

    @Transactional
    public String updateById(Long roomId, String reservationId, ReservationUpdateRequest reservationRequest) {
        // update 로직 변경 : find -> new save -> old delete
        validateUpdateRequest(reservationRequest);

        User user = userRepository.findById(reservationRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.NO_TARGET));
        Reservation oldReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.NO_TARGET));
        StudyGroup studyGroup = oldReservation.getStudyGroup();
        Room Room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.NO_TARGET));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime sTime = LocalDateTime.parse(reservationRequest.getStartTime(), formatter);
        LocalDateTime eTime = LocalDateTime.parse(reservationRequest.getEndTime(), formatter);

        Reservation newReservation = Reservation.builder()
                .id(Room.getId() + "||" + reservationRequest.getStartTime())
                .startTime(sTime)
                .endTime(eTime)
                .user(user)
                .studyGroup(studyGroup)
                .room(Room)
                .build();

        Reservation updatedReservation = reservationRepository.save(newReservation);
        reservationRepository.delete(oldReservation);

        return updatedReservation.getId();
    }

    private void validateUpdateRequest(ReservationUpdateRequest reservationRequest) {

        StudyGroup studyGroup = studyGroupRepository.findById(reservationRequest.getStudyGroupId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.NO_TARGET));

        UserType userType = userGroupRepository.findUserTypeByUserIdAndSGId(
                reservationRequest.getUserId(), studyGroup.getId()).get();
        if (!userType.equals(UserType.L)) {
            throw new IllegalStateException("user가 리더인 분만 예약수정이 가능합니다.");
        }
    }

    @Transactional
    public void deleteById(String reservationId) {
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);
        if (reservation.isPresent()) {
            reservationRepository.delete(reservation.get());
        }
    }

    private ReservationDetails mapToDto(Reservation saveReservation) {

        return ReservationDetails.builder()
                .id(saveReservation.getId())
                .startTime(saveReservation.getStartTime())
                .endTime(saveReservation.getEndTime())
                .user(saveReservation.getUser())
                .studyGroup(saveReservation.getStudyGroup())
                .room(saveReservation.getRoom())
                .build();
    }

    private Reservation mapToEntity(ReservationCreateRequest reservationRequest,
                                    User user,
                                    StudyGroup studyGroup,
                                    Room room) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime sTime = LocalDateTime.parse(reservationRequest.getStartTime(), formatter);
        LocalDateTime eTime = LocalDateTime.parse(reservationRequest.getEndTime(), formatter);

        return Reservation.builder()
                .id(room.getId() + "||" +reservationRequest.getStartTime())
                .startTime(sTime)
                .endTime(eTime)
                .user(user)
                .studyGroup(studyGroup)
                .room(room)
                .build();

    }

}
