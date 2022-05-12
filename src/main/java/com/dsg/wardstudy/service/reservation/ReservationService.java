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
import com.dsg.wardstudy.exception.WSApiException;
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
                .orElseThrow(() -> {
                    log.error("user 대상이 없습니다. userId: {}", reservationRequest.getUserId());
                    throw new ResourceNotFoundException(ErrorCode.NO_TARGET);
                });
        StudyGroup studyGroup = studyGroupRepository.findById(studyGroupId)
                .orElseThrow(() -> {
                    log.error("studyGroup 대상이 없습니다. studyGroupId: {}", studyGroupId);
                    throw new ResourceNotFoundException(ErrorCode.NO_TARGET);
                });
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> {
                    log.error("room 대상이 없습니다. roomId: {}", roomId);
                    throw new ResourceNotFoundException(ErrorCode.NO_TARGET);
                });

        Reservation reservation = mapToEntity(reservationRequest, user, studyGroup, room);
        Reservation saveReservation = reservationRepository.save(reservation);

        return mapToDto(saveReservation);

    }

    private void validateCreateRequest(ReservationCreateRequest reservationRequest, Long studyGroupId) {

        UserType userType = userGroupRepository.findUserTypeByUserIdAndSGId(
                reservationRequest.getUserId(), studyGroupId).get();

        if (!userType.equals(UserType.L)) {
            log.error("userType이 Leader가 아닙니다.");
            throw new WSApiException(ErrorCode.INVALID_REQUEST, "user가 리더인 분만 예약등록이 가능합니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<ReservationDetails> getAllByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("user 대상이 없습니다. userId: {}", userId);
                    throw new ResourceNotFoundException(ErrorCode.NO_TARGET);
                });
        List<Long> sgIds = userGroupRepository.findSgIdsByUserId(user.getId());

        return reservationRepository.findByStudyGroupIds(sgIds).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<ReservationDetails> getByRoomIdAndTimePeriod(Long roomId, String startTime, String endTime) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> {
                    log.error("room 대상이 없습니다. roomId: {}", roomId);
                    throw new ResourceNotFoundException(ErrorCode.NO_TARGET);
                });

        LocalDateTime sTime = formatterLocalDateTime(startTime);
        LocalDateTime eTime = formatterLocalDateTime(endTime);

        return reservationRepository.findByRoomIdAndTimePeriod(room.getId(), sTime, eTime).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<ReservationDetails> getByRoomId(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> {
                    log.error("room 대상이 없습니다. roomId: {}", roomId);
                    throw new ResourceNotFoundException(ErrorCode.NO_TARGET);
                });

        return reservationRepository.findByRoomId(room.getId()).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReservationDetails getByRoomIdAndReservationId(Long roomId, String reservationId) {
        Reservation reservation = reservationRepository.findByRoomIdAndId(roomId, reservationId)
                .orElseThrow(() -> {
                    log.error("reservation 대상이 없습니다. roomId: {}, reservationId: {}", roomId, reservationId);
                    throw new ResourceNotFoundException(ErrorCode.NO_TARGET);
                });
        return mapToDto(reservation);
    }

    @Transactional
    public String updateById(Long roomId, String reservationId, ReservationUpdateRequest reservationRequest) {
        // update 로직 변경 : find -> new save -> old delete
        validateUpdateRequest(reservationRequest);

        User user = userRepository.findById(reservationRequest.getUserId())
                .orElseThrow(() -> {
                    log.error("user 대상이 없습니다. userId: {}", reservationRequest.getUserId());
                    throw new ResourceNotFoundException(ErrorCode.NO_TARGET);
                });
        Reservation oldReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> {
                    log.error("reservation 대상이 없습니다. reservationId: {}", reservationId);
                    throw new ResourceNotFoundException(ErrorCode.NO_TARGET);
                });
        StudyGroup studyGroup = oldReservation.getStudyGroup();
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> {
                    log.error("room 대상이 없습니다. roomId: {}", roomId);
                    throw new ResourceNotFoundException(ErrorCode.NO_TARGET);
                });

        Reservation newReservation = Reservation.builder()
                .id(genReservationId(room, reservationRequest.getStartTime()))
                .startTime(formatterLocalDateTime(reservationRequest.getStartTime()))
                .endTime(formatterLocalDateTime(reservationRequest.getEndTime()))
                .user(user)
                .studyGroup(studyGroup)
                .room(room)
                .build();

        Reservation updatedReservation = reservationRepository.save(newReservation);
        reservationRepository.delete(oldReservation);

        return updatedReservation.getId();
    }

    private String genReservationId(Room room, String startTime) {
        return room.getId() + "||" + startTime;
    }

    private void validateUpdateRequest(ReservationUpdateRequest reservationRequest) {

        StudyGroup studyGroup = studyGroupRepository.findById(reservationRequest.getStudyGroupId())
                .orElseThrow(() -> {
                    log.error("studyGroup 대상이 없습니다. studyGroupId: {}", reservationRequest.getStudyGroupId());
                    throw new ResourceNotFoundException(ErrorCode.NO_TARGET);
                });

        UserType userType = userGroupRepository.findUserTypeByUserIdAndSGId(
                reservationRequest.getUserId(), studyGroup.getId()).get();
        if (!userType.equals(UserType.L)) {
            log.error("userType이 Leader가 아닙니다.");
            throw new WSApiException(ErrorCode.INVALID_REQUEST, "user가 리더인 분만 예약수정이 가능합니다.");
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

    private LocalDateTime formatterLocalDateTime(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(time, formatter);
    }

    private Reservation mapToEntity(ReservationCreateRequest reservationRequest,
                                    User user,
                                    StudyGroup studyGroup,
                                    Room room) {

        return Reservation.builder()
                .id(genReservationId(room, reservationRequest.getStartTime()))
                .startTime(formatterLocalDateTime(reservationRequest.getStartTime()))
                .endTime(formatterLocalDateTime(reservationRequest.getEndTime()))
                .user(user)
                .studyGroup(studyGroup)
                .room(room)
                .build();

    }

}
