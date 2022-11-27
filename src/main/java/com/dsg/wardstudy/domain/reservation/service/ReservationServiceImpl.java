package com.dsg.wardstudy.domain.reservation.service;

import com.dsg.wardstudy.common.adapter.kafka.JsonKafkaProducer;
import com.dsg.wardstudy.common.exception.ErrorCode;
import com.dsg.wardstudy.common.exception.WSApiException;
import com.dsg.wardstudy.common.utils.TimeParsingUtils;
import com.dsg.wardstudy.domain.reservation.Reservation;
import com.dsg.wardstudy.domain.reservation.Room;
import com.dsg.wardstudy.domain.reservation.dto.ReservationCommand;
import com.dsg.wardstudy.domain.reservation.dto.ReservationDetails;
import com.dsg.wardstudy.domain.reservation.dto.ValidateFindByIdDto;
import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.domain.user.User;
import com.dsg.wardstudy.repository.reservation.ReservationRepository;
import com.dsg.wardstudy.repository.reservation.RoomRepository;
import com.dsg.wardstudy.repository.studyGroup.StudyGroupRepository;
import com.dsg.wardstudy.repository.user.UserGroupRepository;
import com.dsg.wardstudy.repository.user.UserRepository;
import com.dsg.wardstudy.type.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.dsg.wardstudy.config.redis.RedisCacheKey.RESERVATION_LIST;


@Log4j2
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService{

    private final StudyGroupRepository studyGroupRepository;
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
//    private final JsonKafkaProducer jsonKafkaProducer;

    @CacheEvict(key = "#roomId", value = RESERVATION_LIST, cacheManager = "redisCacheManager")
    @Transactional
    @Override
    public ReservationDetails register(Long studyGroupId, Long roomId,
                                       ReservationCommand.RegisterReservation registerReservation) {

        Reservation reservation = validateCreateRequest(studyGroupId, roomId, registerReservation);
        Reservation saveReservation = reservationRepository.save(reservation);
        ReservationDetails reservationDetails = ReservationDetails.mapToDto(saveReservation);
        log.info("register reservationDetails: {}", reservationDetails);
        // kafka 메시징 publish
//        jsonKafkaProducer.sendMessage(saveReservation);

        return reservationDetails;

    }

    private Reservation validateCreateRequest(
            Long studyGroupId,
            Long roomId,
            ReservationCommand.RegisterReservation registerReservation) {

        // 시간 간격 차이 최소 1시간
        validateDiffTime(registerReservation.getStartTime(), registerReservation.getEndTime());

        ValidateFindByIdDto validateFindByIdDto = validateFindById(registerReservation.getUserId(), studyGroupId, roomId);

        UserType findUserType = userGroupRepository.findUserTypeByUserIdAndSGId(registerReservation.getUserId(), studyGroupId)
                .orElseThrow(() -> new WSApiException(ErrorCode.NOT_FOUND_USER, "studyGroup 등록자가 아닙니다."));

        Optional.of(findUserType).ifPresent(userType -> {
            if (!userType.equals(UserType.LEADER)) {
                log.error("userType이 Leader가 아닙니다.");
                throw new WSApiException(ErrorCode.INVALID_REQUEST,
                        "Reservation registration is possible only if the user is the leader.");
            }
        });
        Reservation reservation = registerReservation.mapToEntity(
                  validateFindByIdDto.getUser()
                , validateFindByIdDto.getStudyGroup()
                , validateFindByIdDto.getRoom()
        );

        log.info("registerReservation: {}", reservation);

        // 중복 reservation 체크
        reservationRepository.findByIdLock(reservation.getId()).ifPresent( r -> {
            log.error("Same reservationId is existing, can't make the reservation");
            throw new WSApiException(ErrorCode.DUPLICATED_ENTITY, "reservation", reservation.getId());
        });

        return reservation;
    }

    private void validateDiffTime(String stateTime, String endTime) {
        SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        Date stateDate = null;
        Date endDate = null;
        try {
            stateDate = sft.parse(stateTime);
            endDate = sft.parse(endTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        long diffTime = (endDate.getTime() - stateDate.getTime())/3600000;
        log.info("diffTime: {}", diffTime);
        if (diffTime < 1 || diffTime > 4) {
            throw new WSApiException(ErrorCode.INVALID_REQUEST, "예약시간은 최소 1시간이상이어야 하고 최대 4시간을 넘길 수 없습니다.");
        }
    }

    private ValidateFindByIdDto validateFindById(Long userId, Long studyGroupId, Long roomId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("user 대상이 없습니다. userId: {}", userId);
                    throw new WSApiException(ErrorCode.NOT_FOUND_USER);
                });
        StudyGroup studyGroup = studyGroupRepository.findById(studyGroupId)
                .orElseThrow(() -> {
                    log.error("studyGroup 대상이 없습니다. studyGroupId: {}", studyGroupId);
                    throw new WSApiException(ErrorCode.NO_FOUND_ENTITY);
                });
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> {
                    log.error("room 대상이 없습니다. roomId: {}", roomId);
                    throw new WSApiException(ErrorCode.NO_FOUND_ENTITY);
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
                    log.error("user 대상이 없습니다. userId: {}", userId);
                    throw new WSApiException(ErrorCode.NO_FOUND_ENTITY);
                });
        List<Long> sgIds = userGroupRepository.findSgIdsByUserId(user.getId());

        return reservationRepository.findByStudyGroupIds(sgIds).stream()
                .map(ReservationDetails::mapToDto)
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    @Override
    public List<ReservationDetails> getByRoomIdAndTimePeriod(Long roomId, String startTime, String endTime) {

        validateDiffTime(startTime, endTime);

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> {
                    log.error("room 대상이 없습니다. roomId: {}", roomId);
                    throw new WSApiException(ErrorCode.NO_FOUND_ENTITY);
                });

        LocalDateTime sTime = TimeParsingUtils.formatterLocalDateTime(startTime);
        LocalDateTime eTime = TimeParsingUtils.formatterLocalDateTime(endTime);

        return reservationRepository.findByRoomIdAndTimePeriod(room.getId(), sTime, eTime).stream()
                .map(ReservationDetails::mapToDto)
                .collect(Collectors.toList());

    }

    @Cacheable(key = "#roomId", value = RESERVATION_LIST, cacheManager = "redisCacheManager")
    @Transactional(readOnly = true)
    @Override
    public List<ReservationDetails> getByRoomId(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> {
                    log.error("room 대상이 없습니다. roomId: {}", roomId);
                    throw new WSApiException(ErrorCode.NO_FOUND_ENTITY);
                });

        return reservationRepository.findByRoomId(room.getId()).stream()
                .map(ReservationDetails::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public ReservationDetails getByRoomIdAndReservationId(Long roomId, String reservationId) {
        Reservation reservation = reservationRepository.findByRoomIdAndId(roomId, reservationId)
                .orElseThrow(() -> {
                    log.error("reservation 대상이 없습니다. roomId: {}, reservationId: {}", roomId, reservationId);
                    throw new WSApiException(ErrorCode.NO_FOUND_ENTITY);
                });
        return ReservationDetails.mapToDto(reservation);
    }

    @Transactional
    @Override
    public String updateById(Long roomId, String reservationId, ReservationCommand.UpdateReservation updateReservation) {
        // update 로직 변경 : find old -> save new -> delete old
        Reservation oldReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> {
                    log.error("reservation 대상이 없습니다. reservationId: {}", reservationId);
                    throw new WSApiException(ErrorCode.NO_FOUND_ENTITY);
                });

        Reservation newReservation = validateUpdateRequest(roomId, updateReservation);
        Reservation updatedReservation = reservationRepository.save(newReservation);
        reservationRepository.delete(oldReservation);

        return updatedReservation.getId();
    }


    private Reservation validateUpdateRequest(Long roomId, ReservationCommand.UpdateReservation updateReservation) {

        validateDiffTime(updateReservation.getStartTime(), updateReservation.getEndTime());

        ValidateFindByIdDto validateFindByIdDto = validateFindById(updateReservation.getUserId(),
                updateReservation.getStudyGroupId(),
                roomId);

        UserType findUserType = userGroupRepository.findUserTypeByUserIdAndSGId(updateReservation.getUserId(), updateReservation.getStudyGroupId())
                .orElseThrow(() -> new WSApiException(ErrorCode.NOT_FOUND_USER, "studyGroup 등록자가 아닙니다."));

        Optional.of(findUserType).ifPresent(userType -> {
            if (!userType.equals(UserType.LEADER)) {
                log.error("userType이 Leader가 아닙니다.");
                throw new WSApiException(ErrorCode.INVALID_REQUEST,
                        "Reservation update is possible only if the user is the leader.");
            }
        });

        Reservation newReservation = updateReservation.mapToEntity(validateFindByIdDto);

        reservationRepository.findById(newReservation.getId()).ifPresent( r -> {
            log.error("Same reservationId is existing, can't make the reservation");
            throw new WSApiException(ErrorCode.DUPLICATED_ENTITY, "reservation", newReservation.getId());
        });
        
        log.info("updateReservation newReservation: {}", newReservation);

        return newReservation;
    }

    @Transactional
    @Override
    public void deleteById(Long userId, String reservationId) {
        userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("user 대상이 없습니다. userId: {}", userId);
                    throw new WSApiException(ErrorCode.NOT_FOUND_USER);
                });
        // 해당 register 인지 validate
        reservationRepository.findById(reservationId)
                .ifPresent(reservation -> {
                    log.info("해당 reservation register: {}", reservation.getUser().getId());

                    UserType findUserType = userGroupRepository.findUserTypeByUserIdAndSGId(reservation.getUser().getId(), reservation.getStudyGroup().getId())
                            .orElseThrow(() -> new WSApiException(ErrorCode.NOT_FOUND_USER, "studyGroup 등록자가 아닙니다."));

                    Optional.of(findUserType).ifPresent(userType -> {
                        if (!userType.equals(UserType.LEADER)) {
                            log.error("userType이 Leader가 아닙니다.");
                            throw new WSApiException(ErrorCode.INVALID_REQUEST,
                                    "Reservation update is possible only if the user is the leader.");
                        }
                        reservationRepository.deleteById(reservationId);
                    });
                });
    }

    @Transactional
    @Override
    public void changeIsEmailSent(Reservation reservation) {
        reservation.changeIsEmailSent(true);
        log.info("reservation.changeIsEmailSent(true); 실행");
    }

}
