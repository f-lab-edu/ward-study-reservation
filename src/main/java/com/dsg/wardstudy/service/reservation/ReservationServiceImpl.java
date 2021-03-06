package com.dsg.wardstudy.service.reservation;

import com.dsg.wardstudy.domain.reservation.Reservation;
import com.dsg.wardstudy.domain.reservation.Room;
import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.domain.user.User;
import com.dsg.wardstudy.dto.reservation.ReservationCreateRequest;
import com.dsg.wardstudy.dto.reservation.ReservationDetails;
import com.dsg.wardstudy.dto.reservation.ReservationUpdateRequest;
import com.dsg.wardstudy.dto.reservation.ValidateFindByIdDto;
import com.dsg.wardstudy.exception.ErrorCode;
import com.dsg.wardstudy.exception.WSApiException;
import com.dsg.wardstudy.repository.reservation.ReservationRepository;
import com.dsg.wardstudy.repository.reservation.RoomRepository;
import com.dsg.wardstudy.repository.studyGroup.StudyGroupRepository;
import com.dsg.wardstudy.repository.user.UserGroupRepository;
import com.dsg.wardstudy.repository.user.UserRepository;
import com.dsg.wardstudy.type.UserType;
import com.dsg.wardstudy.utils.TimeParsingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.dsg.wardstudy.config.redis.RedisCacheKey.RESERVATION_LIST;


@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService{

    private final StudyGroupRepository studyGroupRepository;
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    private final TimeParsingUtils timeParsingUtils;

    @CacheEvict(key = "#roomId", value = RESERVATION_LIST, cacheManager = "redisCacheManager")
    @Transactional
    @Override
    public ReservationDetails create(Long studyGroupId, Long roomId, ReservationCreateRequest reservationRequest) {

        Reservation reservation = validateCreateRequest(studyGroupId, roomId, reservationRequest);
        Reservation saveReservation = reservationRepository.save(reservation);
        return mapToDto(saveReservation);

    }

    private Reservation validateCreateRequest(
            Long studyGroupId,
            Long roomId,
            ReservationCreateRequest reservationRequest) {

        ValidateFindByIdDto validateFindByIdDto = validateFindById(reservationRequest.getUserId(), studyGroupId, roomId);

        userGroupRepository.findUserTypeByUserIdAndSGId(
                reservationRequest.getUserId(), studyGroupId)
                .ifPresent(userType -> {
                    if (!userType.equals(UserType.L)) {
                        log.error("userType??? Leader??? ????????????.");
                        throw new WSApiException(ErrorCode.INVALID_REQUEST, "Reservation registration is possible only if the user is the leader.");
                    }
                });
        // ?????? reservation ??????
        Reservation reservation = mapToEntity(
                reservationRequest
                , validateFindByIdDto.getUser()
                , validateFindByIdDto.getStudyGroup()
                , validateFindByIdDto.getRoom()
        );

        reservationRepository.findByIdLock(reservation.getId()).ifPresent( r -> {
            log.error("Same reservationId is existing, can't make the reservation");
            throw new WSApiException(ErrorCode.DUPLICATED_ENTITY, "The same reservation exists.");
        });

        return reservation;
    }

    private ValidateFindByIdDto validateFindById(Long userId, Long studyGroupId, Long roomId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("user ????????? ????????????. userId: {}", userId);
                    throw new WSApiException(ErrorCode.NO_FOUND_ENTITY, "can't find a User by " +
                            " userId: " + userId);
                });
        StudyGroup studyGroup = studyGroupRepository.findById(studyGroupId)
                .orElseThrow(() -> {
                    log.error("studyGroup ????????? ????????????. studyGroupId: {}", studyGroupId);
                    throw new WSApiException(ErrorCode.NO_FOUND_ENTITY, "can't find a StudyGroup by " +
                            " studyGroupId: " + studyGroupId);
                });
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> {
                    log.error("room ????????? ????????????. roomId: {}", roomId);
                    throw new WSApiException(ErrorCode.NO_FOUND_ENTITY, "can't find a Room by " +
                            " roomId: " + roomId);
                });

        return ValidateFindByIdDto.builder()
                .user(user)
                .studyGroup(studyGroup)
                .room(room)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ReservationDetails> getAllByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("user ????????? ????????????. userId: {}", userId);
                    throw new WSApiException(ErrorCode.NO_FOUND_ENTITY, "can't find a User by " +
                            " userId: " + userId);
                });
        List<Long> sgIds = userGroupRepository.findSgIdsByUserId(user.getId());

        return reservationRepository.findByStudyGroupIds(sgIds).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    @Override
    public List<ReservationDetails> getByRoomIdAndTimePeriod(Long roomId, String startTime, String endTime) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> {
                    log.error("room ????????? ????????????. roomId: {}", roomId);
                    throw new WSApiException(ErrorCode.NO_FOUND_ENTITY, "can't find a Room by " +
                            " roomId: " + roomId);
                });

        LocalDateTime sTime = timeParsingUtils.formatterLocalDateTime(startTime);
        LocalDateTime eTime = timeParsingUtils.formatterLocalDateTime(endTime);

        return reservationRepository.findByRoomIdAndTimePeriod(room.getId(), sTime, eTime).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

    }

    @Cacheable(key = "#roomId", value = RESERVATION_LIST, cacheManager = "redisCacheManager")
    @Transactional(readOnly = true)
    @Override
    public List<ReservationDetails> getByRoomId(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> {
                    log.error("room ????????? ????????????. roomId: {}", roomId);
                    throw new WSApiException(ErrorCode.NO_FOUND_ENTITY, "can't find a Room by " +
                            " roomId: " + roomId);
                });

        return reservationRepository.findByRoomId(room.getId()).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public ReservationDetails getByRoomIdAndReservationId(Long roomId, String reservationId) {
        Reservation reservation = reservationRepository.findByRoomIdAndId(roomId, reservationId)
                .orElseThrow(() -> {
                    log.error("reservation ????????? ????????????. roomId: {}, reservationId: {}", roomId, reservationId);
                    throw new WSApiException(
                            ErrorCode.NO_FOUND_ENTITY,  "can't find a reservation by " + "roomId: " +  roomId +
                            " and reservationId: " + reservationId);
                });
        return mapToDto(reservation);
    }

    @Transactional
    @Override
    public String updateById(Long roomId, String reservationId, ReservationUpdateRequest reservationRequest) {
        // update ?????? ?????? : find old -> save new -> delete old
        Reservation oldReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> {
                    log.error("reservation ????????? ????????????. reservationId: {}", reservationId);
                    throw new WSApiException(ErrorCode.NO_FOUND_ENTITY, "can't find a reservation by " +
                            " reservation id: " + reservationId);
                });

        Reservation newReservation = validateUpdateRequest(roomId, reservationRequest);
        Reservation updatedReservation = reservationRepository.save(newReservation);
        reservationRepository.delete(oldReservation);

        return updatedReservation.getId();
    }

    private String genReservationId(Room room, String startTime) {
        return room.getId() + "||" + startTime;
    }

    private Reservation validateUpdateRequest(Long roomId, ReservationUpdateRequest reservationRequest) {

        ValidateFindByIdDto validateFindByIdDto = validateFindById(reservationRequest.getUserId(),
                reservationRequest.getStudyGroupId(),
                roomId);

        userGroupRepository.findUserTypeByUserIdAndSGId(
                reservationRequest.getUserId(), reservationRequest.getStudyGroupId())
                .ifPresent(userType -> {
                    if (!userType.equals(UserType.L)) {
                        log.error("userType??? Leader??? ????????????.");
                        throw new WSApiException(ErrorCode.INVALID_REQUEST, "Reservation modification is possible only if the user is the leader.");
                    }
                });

        Reservation newReservation = Reservation.builder()
                .id(genReservationId(validateFindByIdDto.getRoom(), reservationRequest.getStartTime()))
                .startTime(timeParsingUtils.formatterLocalDateTime(reservationRequest.getStartTime()))
                .endTime(timeParsingUtils.formatterLocalDateTime(reservationRequest.getEndTime()))
                .user(validateFindByIdDto.getUser())
                .studyGroup(validateFindByIdDto.getStudyGroup())
                .room(validateFindByIdDto.getRoom())
                .build();

        reservationRepository.findById(newReservation.getId()).ifPresent( r -> {
            log.error("Same reservationId is existing, can't make the reservation");
            throw new WSApiException(ErrorCode.DUPLICATED_ENTITY, "The same reservation exists.");
        });

        return newReservation;
    }

    @Transactional
    @Override
    public void deleteById(Long userId, String reservationId) {
        userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("user ????????? ????????????. userId: {}", userId);
                    throw new WSApiException(ErrorCode.NO_FOUND_ENTITY, "can't find a User by userId: " + userId);
                });
        // ?????? register ?????? validate
        reservationRepository.findById(reservationId)
                .ifPresent(reservation -> {
                    log.info("?????? reservation register: {}", reservation.getUser().getId());
                    if (reservation.getUser().getId().equals(userId)) {
                        reservationRepository.delete(reservation);
                    } else {
                        log.error("?????? reservation register??? ????????????.");
                        throw new WSApiException(ErrorCode.INVALID_REQUEST, "Reservation modification is possible only if the user is the register.");
                    }
                });
    }

    @Transactional
    @Override
    public void changeIsEmailSent(Reservation reservation) {
        reservation.changeIsEmailSent(true);
        log.info("reservation.changeIsEmailSent(true); ??????");
    }

    private ReservationDetails mapToDto(Reservation reservation) {
        return ReservationDetails.builder()
                .id(reservation.getId())
                .startTime(timeParsingUtils.formatterString(reservation.getStartTime()))
                .endTime(timeParsingUtils.formatterString(reservation.getEndTime()))
                .registerId(reservation.getUser().getId())
                .registerEmail(reservation.getUser().getEmail())
                .studyGroupId(reservation.getStudyGroup().getId())
                .studyGroupTitle(reservation.getStudyGroup().getTitle())
                .roomId(reservation.getRoom().getId())
                .roomName(reservation.getRoom().getName())
                .build();
    }

    private Reservation mapToEntity(ReservationCreateRequest reservationRequest,
                                    User user,
                                    StudyGroup studyGroup,
                                    Room room) {

        return Reservation.builder()
                .id(genReservationId(room, reservationRequest.getStartTime()))
                .startTime(timeParsingUtils.formatterLocalDateTime(reservationRequest.getStartTime()))
                .endTime(timeParsingUtils.formatterLocalDateTime(reservationRequest.getEndTime()))
                .user(user)
                .studyGroup(studyGroup)
                .room(room)
                .build();

    }

}
