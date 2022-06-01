package com.dsg.wardstudy.service.studyGroup;

import com.dsg.wardstudy.domain.reservation.Reservation;
import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.domain.user.User;
import com.dsg.wardstudy.domain.user.UserGroup;
import com.dsg.wardstudy.dto.PageResponse;
import com.dsg.wardstudy.dto.studyGroup.StudyGroupRequest;
import com.dsg.wardstudy.dto.studyGroup.StudyGroupResponse;
import com.dsg.wardstudy.exception.ErrorCode;
import com.dsg.wardstudy.exception.WSApiException;
import com.dsg.wardstudy.repository.reservation.ReservationQueryRepository;
import com.dsg.wardstudy.repository.reservation.ReservationRepository;
import com.dsg.wardstudy.repository.studyGroup.StudyGroupRepository;
import com.dsg.wardstudy.repository.user.UserGroupRepository;
import com.dsg.wardstudy.repository.user.UserRepository;
import com.dsg.wardstudy.type.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.dsg.wardstudy.config.redis.RedisCacheKey.STUDYGROUP_LIST;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudyGroupServiceImpl implements StudyGroupService {

    private final StudyGroupRepository studyGroupRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationQueryRepository reservationQueryRepository;

    @Transactional
    @Override
    public StudyGroupResponse create(Long userId, StudyGroupRequest studyGroupRequest) {

        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("user 대상이 없습니다. userId: {}", userId);
                    throw new WSApiException(ErrorCode.NO_FOUND_ENTITY, "can't find a User by " +
                            " userId: " + userId);
                });

        StudyGroup studyGroup = mapToEntity(studyGroupRequest);
        StudyGroup savedStudyGroup = studyGroupRepository.save(studyGroup);

        // studyGroup 등록시 UserType L(리더)로 등록
        UserGroup userGroup = UserGroup.builder()
                .studyGroup(savedStudyGroup)
                .user(findUser)
                .userType(UserType.L)
                .build();

        UserGroup savedUserGroup = userGroupRepository.save(userGroup);

        return mapToResponse(savedUserGroup);
    }

    private StudyGroupResponse mapToResponse(UserGroup savedUserGroup) {
        return StudyGroupResponse.builder()
                .studyGroupId(savedUserGroup.getStudyGroup().getId())
                .title(savedUserGroup.getStudyGroup().getTitle())
                .content(savedUserGroup.getStudyGroup().getContent())
                .build();
    }

    private StudyGroup mapToEntity(StudyGroupRequest studyGroupRequest) {
        return StudyGroup.builder()
                .title(studyGroupRequest.getTitle())
                .content(studyGroupRequest.getContent())
                .build();
    }

    private StudyGroupResponse mapToDto(StudyGroup savedGroup) {
        return StudyGroupResponse.builder()
                .studyGroupId(savedGroup.getId())
                .title(savedGroup.getTitle())
                .content(savedGroup.getContent())
                .build();
    }


    @Cacheable(key = "#studyGroupId" ,value = STUDYGROUP_LIST, cacheManager = "redisCacheManager")
    @Transactional(readOnly = true)
    @Override
    public StudyGroupResponse getById(Long studyGroupId) {

        StudyGroup studyGroup = studyGroupRepository.findById(studyGroupId)
                .orElseThrow(() -> {
                    log.error("studyGroup 대상이 없습니다. studyGroupId: {}", studyGroupId);
                    throw new WSApiException(ErrorCode.NO_FOUND_ENTITY, "can't find a StudyGroup by " +
                            " studyGroupId: " + studyGroupId);
                });

        return mapToDto(studyGroup);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse.StudyGroup getAll(Pageable pageable) {

        Page<StudyGroupResponse> studyGroupResponsePage = studyGroupRepository.findAll(pageable).map(this::mapToDto);
        return PageResponse.StudyGroup.builder()
                .content(studyGroupResponsePage.getContent())
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalElements(studyGroupResponsePage.getTotalElements())
                .totalPages(studyGroupResponsePage.getTotalPages())
                .last(studyGroupResponsePage.isLast())
                .build();
    }

    @Transactional
    @Override
    public Long updateById(Long userId, Long studyGroupId, StudyGroupRequest studyGroupRequest) {

        StudyGroup studyGroup = validateStudyGroup(userId, studyGroupId);

        studyGroup.update(studyGroupRequest.getTitle(), studyGroupRequest.getContent());
        log.info("studyGroup: {}", studyGroup);

        return studyGroup.getId();

    }

    @CacheEvict(key = "#studyGroupId", value = STUDYGROUP_LIST, cacheManager = "redisCacheManager")
    @Transactional
    @Override
    public void deleteById(Long userId, Long studyGroupId) {
        StudyGroup studyGroup = validateStudyGroup(userId, studyGroupId);
        log.info("studyGroup: {}", studyGroup);

        Reservation reservation = reservationQueryRepository.findByUserIdAndStudyGroupId(userId, studyGroupId);
        log.info("reservation: {}", reservation);

        if (Optional.of(reservation).isPresent()) {
            // 외래키를 가진 reservation이 먼저 삭제되어야만 studyGroup도 지울 수 있음!
            reservationRepository.delete(reservation);
            studyGroupRepository.delete(studyGroup);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<StudyGroupResponse> getAllByUserId(Long userId) {

        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("user 대상이 없습니다. userId: {}", userId);
                    throw new WSApiException(ErrorCode.NO_FOUND_ENTITY, "can't find a User by " +
                            " userId: " + userId);
                });

        List<UserGroup> userGroups = userGroupRepository.findByUserId(findUser.getId());

        List<Long> studyGroupsIds = userGroups.stream()
                .map(d -> d.getStudyGroup().getId())
                .collect(Collectors.toList());

        List<StudyGroup> studyGroups = studyGroupRepository.findByIdIn(studyGroupsIds);
        return studyGroups.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

    }

    private StudyGroup validateStudyGroup(Long userId, Long studyGroupId) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("user 대상이 없습니다. userId: {}", userId);
                    throw new WSApiException(ErrorCode.NO_FOUND_ENTITY, "can't find a User by " +
                            " userId: " + userId);
                });

        StudyGroup findStudyGroup = studyGroupRepository.findById(studyGroupId)
                .orElseThrow(() -> {
                    log.error("studyGroup 대상이 없습니다. studyGroupId: {}", studyGroupId);
                    throw new WSApiException(ErrorCode.NO_FOUND_ENTITY, "can't find a StudyGroup by " +
                            " studyGroupId: " + studyGroupId);
                });

        UserType userType = userGroupRepository.findUserTypeByUserIdAndSGId(userId, studyGroupId).get();
        if (!userType.equals(UserType.L)) {
            log.error("userType이 Leader가 아닙니다.");
            throw new WSApiException(ErrorCode.INVALID_REQUEST, "StudyGroup modification is possible only if the user is the leader.");
        }
        return findStudyGroup;
    }
}
