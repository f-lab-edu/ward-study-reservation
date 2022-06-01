package com.dsg.wardstudy.service.studyGroup;

import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.domain.user.User;
import com.dsg.wardstudy.domain.user.UserGroup;
import com.dsg.wardstudy.dto.studyGroup.StudyGroupRequest;
import com.dsg.wardstudy.dto.studyGroup.StudyGroupResponse;
import com.dsg.wardstudy.exception.ErrorCode;
import com.dsg.wardstudy.exception.WSApiException;
import com.dsg.wardstudy.repository.studyGroup.StudyGroupRepository;
import com.dsg.wardstudy.repository.user.UserGroupRepository;
import com.dsg.wardstudy.repository.user.UserRepository;
import com.dsg.wardstudy.type.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.dsg.wardstudy.config.redis.RedisCacheKey.STUDYGROUP_LIST;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudyGroupServiceImpl implements StudyGroupService {

    private final StudyGroupRepository studyGroupRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;

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

        // UserType L(리더)로 등록
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
                .userGroup(savedUserGroup)
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
    public List<StudyGroupResponse> getAll() {

        List<StudyGroup> studyGroups = studyGroupRepository.findAll();
        return studyGroups.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Long updateById(Long userId, Long studyGroupId, StudyGroupRequest studyGroupRequest) {

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
            throw new WSApiException(ErrorCode.INVALID_REQUEST, "Reservation modification is possible only if the user is the leader.");
        }

        findStudyGroup.update(studyGroupRequest.getTitle(), studyGroupRequest.getContent());
        log.info("findStudyGroup: {}", findStudyGroup);

        return findStudyGroup.getId();

    }

    @CacheEvict(key = "#studyGroupId", value = STUDYGROUP_LIST, cacheManager = "redisCacheManager")
    @Transactional
    @Override
    public void deleteById(Long studyGroupId) {
        studyGroupRepository.deleteById(studyGroupId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<StudyGroupResponse> getAllByUserId(Long userId) {
        List<UserGroup> userGroups = userGroupRepository.findByUserId(userId);

        List<Long> studyGroupsIds = userGroups.stream()
                .map(d -> d.getStudyGroup().getId())
                .collect(Collectors.toList());

        List<StudyGroup> studyGroups = studyGroupRepository.findByIdIn(studyGroupsIds);
        return studyGroups.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

    }
}
