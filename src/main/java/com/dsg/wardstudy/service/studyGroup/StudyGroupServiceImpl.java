package com.dsg.wardstudy.service.studyGroup;

import com.dsg.wardstudy.domain.studyGroup.QStudyGroup;
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
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.dsg.wardstudy.config.redis.RedisCacheKey.STUDY_GROUP_LIST;

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
                    log.error("user ????????? ????????????. userId: {}", userId);
                    throw new WSApiException(ErrorCode.NO_FOUND_ENTITY, "can't find a User by " +
                            " userId: " + userId);
                });

        StudyGroup studyGroup = mapToEntity(studyGroupRequest);
        StudyGroup savedStudyGroup = studyGroupRepository.save(studyGroup);

        // studyGroup ????????? UserType L(??????)??? ??????
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


    @Transactional(readOnly = true)
    @Override
    public StudyGroupResponse getById(Long studyGroupId) {

        StudyGroup studyGroup = studyGroupRepository.findById(studyGroupId)
                .orElseThrow(() -> {
                    log.error("studyGroup ????????? ????????????. studyGroupId: {}", studyGroupId);
                    throw new WSApiException(ErrorCode.NO_FOUND_ENTITY, "can't find a StudyGroup by " +
                            " studyGroupId: " + studyGroupId);
                });

        return mapToDto(studyGroup);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse.StudyGroup getAll(Pageable pageable, String type, String keyword) {
        // ????????????
        BooleanBuilder booleanBuilder = getSearch(type, keyword);
        log.info("booleanBuilder getSearch: {}", booleanBuilder);

        Page<StudyGroupResponse> studyGroupResponsePage = studyGroupRepository.findAll(booleanBuilder, pageable)
                .map(this::mapToDto);
        return PageResponse.StudyGroup.builder()
                .content(studyGroupResponsePage.getContent())
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalElements(studyGroupResponsePage.getTotalElements())
                .totalPages(studyGroupResponsePage.getTotalPages())
                .last(studyGroupResponsePage.isLast())
                .build();
    }

    private BooleanBuilder getSearch(String type, String keyword) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QStudyGroup qStudyGroup = QStudyGroup.studyGroup;

        BooleanExpression booleanExpression = qStudyGroup.id.gt(0L);

        booleanBuilder.and(booleanExpression);

        // ?????? ????????? ?????? ??????
        if (!StringUtils.hasText(type)) {
            return booleanBuilder;
        }

        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if(type.contains("t")) {
            conditionBuilder.or(qStudyGroup.title.contains(keyword));
        }
        if(type.contains("c")) {
            conditionBuilder.or(qStudyGroup.content.contains(keyword));
        }

        // ?????? ?????? ??????
        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;
    }

    @CacheEvict(key = "#userId", value = STUDY_GROUP_LIST, cacheManager = "redisCacheManager")
    @Transactional
    @Override
    public Long updateById(Long userId, Long studyGroupId, StudyGroupRequest studyGroupRequest) {

        StudyGroup studyGroup = validateStudyGroup(userId, studyGroupId);

        studyGroup.update(studyGroupRequest.getTitle(), studyGroupRequest.getContent());
        log.info("studyGroup: {}", studyGroup);

        return studyGroup.getId();

    }

    @Transactional
    @Override
    public void deleteById(Long userId, Long studyGroupId) {
        // ???????????? ?????? reservation??? ??????????????? ?????? ?????????????????? studyGroup??? ?????? ??? ??????!
        reservationQueryRepository.findByUserIdAndStudyGroupId(userId, studyGroupId)
                .ifPresent(reservation -> {
                    log.info("reservation: {}", reservation);
                    reservationRepository.delete(reservation);
                });
        Optional<StudyGroup> studyGroup = validateDeleteStudyGroup(userId, studyGroupId);
        log.info("studyGroup: {}", studyGroup);
        studyGroup.ifPresent(studyGroupRepository::delete);
    }

    @Cacheable(key = "#userId", value = STUDY_GROUP_LIST, cacheManager = "redisCacheManager")
    @Transactional(readOnly = true)
    @Override
    public List<StudyGroupResponse> getAllByUserId(Long userId) {

        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("user ????????? ????????????. userId: {}", userId);
                    throw new WSApiException(ErrorCode.NO_FOUND_ENTITY, "can't find a User by userId: " + userId);
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

    private Optional<StudyGroup> validateDeleteStudyGroup(Long userId, Long studyGroupId) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("user ????????? ????????????. userId: {}", userId);
                    throw new WSApiException(ErrorCode.NO_FOUND_ENTITY, "can't find a User by userId: " + userId);
                });

        userGroupRepository.findUserTypeByUserIdAndSGId(userId, studyGroupId)
                .ifPresent(userType -> {
                    if (!userType.equals(UserType.L)) {
                        log.error("userType??? Leader??? ????????????.");
                        throw new WSApiException(ErrorCode.INVALID_REQUEST, "StudyGroup modification is possible only if the user is the leader.");
                    }
                });
        return studyGroupRepository.findById(studyGroupId);
    }

    private StudyGroup validateStudyGroup(Long userId, Long studyGroupId) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("user ????????? ????????????. userId: {}", userId);
                    throw new WSApiException(ErrorCode.NO_FOUND_ENTITY, "can't find a User by userId: " + userId);
                });

        StudyGroup findStudyGroup = studyGroupRepository.findById(studyGroupId)
                .orElseThrow(() -> {
                    log.error("studyGroup ????????? ????????????. studyGroupId: {}", studyGroupId);
                    throw new WSApiException(ErrorCode.NO_FOUND_ENTITY, "can't find a StudyGroup by " +
                            " studyGroupId: " + studyGroupId);
                });

        userGroupRepository.findUserTypeByUserIdAndSGId(userId, studyGroupId)
                .ifPresent(userType -> {
                    if (!userType.equals(UserType.L)) {
                        log.error("userType??? Leader??? ????????????.");
                        throw new WSApiException(ErrorCode.INVALID_REQUEST, "StudyGroup modification is possible only if the user is the leader.");
                    }
                });
        return findStudyGroup;
    }
}
